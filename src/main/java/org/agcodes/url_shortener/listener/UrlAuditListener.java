package org.agcodes.url_shortener.listener;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.agcodes.url_shortener.event.UrlCreatedEvent;
import org.agcodes.url_shortener.event.UrlDeletedEvent;
import org.agcodes.url_shortener.event.UrlResolvedEvent;
import org.agcodes.url_shortener.model.UrlAudit;
import org.agcodes.url_shortener.repository.UrlAuditRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UrlAuditListener {
  private final UrlAuditRepository repository;
  @Async
  @EventListener
  public void onUrlCreated(UrlCreatedEvent event) {
    save("URL_CREATED",
        event.url().getShortUrl(),
        event.url().getOriginalUrl());
  }
  @Async
  @EventListener
  public void onUrlResolved(UrlResolvedEvent event) {
    save("URL_RESOLVED",
        event.shortUrl(),
        event.originalUrl());
  }
  @Async
  @EventListener
  public void onUrlDeleted(UrlDeletedEvent event) {
    save("URL_DELETED",
        event.shortUrl(),
        null);
  }

  private void save(String type, String shortUrl, String originalUrl) {
    repository.save(new UrlAudit(
        null,
        type,
        shortUrl,
        originalUrl,
        Date.from(LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant())
    ));
  }
}
