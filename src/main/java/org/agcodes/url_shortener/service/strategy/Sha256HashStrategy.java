package org.agcodes.url_shortener.service.strategy;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import org.agcodes.url_shortener.config.AppProperties;
import org.springframework.stereotype.Component;

@Component
public class Sha256HashStrategy implements UrlHashStrategy {

  private final int hashLength;
  public Sha256HashStrategy(AppProperties appProperties) {
    this.hashLength = appProperties.getHash().getLength();
  }
  // Produces a short id by strategy and taking the first `length` hex chars.
  @Override
  public String generateShortUrl(String input) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
      String hex = HexFormat.of().formatHex(hash);
      if (hashLength <= 0) return hex;
      return hex.substring(0, Math.min(hashLength, hex.length()));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("SHA-256 not available", e);
    }
  }
}
