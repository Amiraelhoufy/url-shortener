package org.agcodes.url_shortener.service;

import com.mongodb.DuplicateKeyException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.agcodes.url_shortener.config.AppProperties;
import org.agcodes.url_shortener.dto.UrlDto;
import org.agcodes.url_shortener.event.UrlCreatedEvent;
import org.agcodes.url_shortener.event.UrlDeletedEvent;
import org.agcodes.url_shortener.event.UrlResolvedEvent;
import org.agcodes.url_shortener.exception.UrlExpiredException;
import org.agcodes.url_shortener.exception.UrlNotFoundException;
import org.agcodes.url_shortener.service.strategy.UrlHashStrategy;
import org.agcodes.url_shortener.model.Url;
import org.agcodes.url_shortener.repository.UrlRepository;
import org.agcodes.url_shortener.service.strategy.UrlHashStrategyFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UrlServiceImpl implements UrlService{

  private final AppProperties appProperties;
  private final UrlRepository urlRepository;
  private final StringRedisTemplate redisTemplate;
  // Inject a specific implementation (e.g., via config or @Qualifier)
//  private final UrlHashStrategy urlHashStrategy;
  private final UrlHashStrategyFactory strategyFactory;
  private final ApplicationEventPublisher eventPublisher;

  public UrlHashStrategy hashStrategy() {
    return strategyFactory.getStrategy(appProperties.getHash().getStrategy());
  }

  @Override
  public Url shortenUrl(UrlDto urlDto) {
    String originalUrl = urlDto.originalUrl();

    // Defensive validation — protects data integrity even if other controllers call the service
    if (originalUrl == null || originalUrl.isBlank()) {
      throw new IllegalArgumentException("Original URL cannot be null or empty");
    }

    // 1. Mongo check by ORIGINAL URL (important)
    Optional<Url> existing = urlRepository.findByOriginalUrl(originalUrl);
    if (existing.isPresent()) {
      return existing.get();
    }

    // 2. Generate short code
    String shortUrl = hashStrategy().generateShortUrl(originalUrl);
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime expiration = urlDto.expirationDate() != null
        ? urlDto.expirationDate()
        : now.plusDays(appProperties.getUrl().getExpirationDays());

    // 3. Create new Url mapping
    Url url = new Url();
    url.setShortUrl(shortUrl);
    url.setOriginalUrl(originalUrl);
    url.setCreatedAt(now);
    url.setExpirationDate(expiration);

    // 4. Collision Safe = two requests try to save the same URL at the same time
    try {
      Url saved = urlRepository.save(url);

      // 🔔 publish event
      eventPublisher.publishEvent(new UrlCreatedEvent(saved));
    } catch (DuplicateKeyException ex) {
      // Another request already created it
      return urlRepository.findByOriginalUrl(originalUrl)
          .orElseThrow(() -> new IllegalStateException("URL exists but cannot be retrieved"));
    }

    // 5. Save in MongoDB
    long ttlSeconds = ChronoUnit.SECONDS.between(now, expiration);
    // RedisKey
    redisTemplate.opsForValue()
        .set("short:" + shortUrl, originalUrl, ttlSeconds, TimeUnit.SECONDS);

    return url;

  }

  @Override
  public String resolve(String shortUrl) {
    String redisKey = "short:" + shortUrl;

    // 1. Redis first
    String cached = redisTemplate.opsForValue().get(redisKey);
    if (cached != null) {
      return cached;
    }

    // 2. Mongo fallback
    Url url = urlRepository.findByShortUrl(shortUrl)
        .orElseThrow(() -> new UrlNotFoundException("Short URL not found"));

    // 3. Expiration check (business rule)
    if (url.getExpirationDate().isBefore(LocalDateTime.now())) {
      throw new UrlExpiredException("Short URL has expired");
    }

    // 4. Cache again
    long ttl = ChronoUnit.SECONDS.between(LocalDateTime.now(), url.getExpirationDate());
    redisTemplate.opsForValue().set(redisKey, url.getOriginalUrl(), ttl, TimeUnit.SECONDS);

    // 🔔 publish event
    eventPublisher.publishEvent(
        new UrlResolvedEvent(shortUrl, url.getOriginalUrl())
    );
    return url.getOriginalUrl();
  }

  @Override
  public void delete(String shortUrl) {
    // 1. Check MongoDB
    Url url = urlRepository.findByShortUrl(shortUrl)
        .orElseThrow(()-> new UrlNotFoundException("Short Url not found"));

    // 2. delete from MongoDB & Redis cache
    urlRepository.delete(url);
    redisTemplate.delete("short:" + shortUrl);

    // 🔔 publish event
    eventPublisher.publishEvent(new UrlDeletedEvent(shortUrl));

  }

}
