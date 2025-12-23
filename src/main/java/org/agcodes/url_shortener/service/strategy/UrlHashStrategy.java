package org.agcodes.url_shortener.service.strategy;

public interface UrlHashStrategy {
  String generateShortUrl(String input);
}
