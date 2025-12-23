package org.agcodes.url_shortener.repository;

import java.util.Optional;
import org.agcodes.url_shortener.model.Url;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends MongoRepository<Url,String> {
  Optional<Url> findByShortUrl(String shortUrl);

  Optional<Url> findByOriginalUrl(String originalUrl);
}
