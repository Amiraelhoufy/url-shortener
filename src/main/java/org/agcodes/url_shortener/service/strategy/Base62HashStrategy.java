package org.agcodes.url_shortener.service.strategy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

// simple encoder
@Component
public class Base62HashStrategy implements UrlHashStrategy {
  private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final int BASE = ALPHABET.length();
  @Override
  public String generateShortUrl(String input) {
    long hash = Math.abs(input.hashCode());
    StringBuilder sb = new StringBuilder();

    while (hash > 0) {
      int rem = (int) (hash % BASE);
      sb.append(ALPHABET.charAt(rem));
      hash /= BASE;
    }

    return sb.reverse().toString();
  }

}
