package org.agcodes.url_shortener.exception;

public class UrlExpiredException extends RuntimeException{
  public UrlExpiredException() {
    super("Short URL has expired");
  }
  public UrlExpiredException(String message) {
    super(message);
  }

}
