package org.agcodes.url_shortener.service;

import org.agcodes.url_shortener.dto.UrlDto;
import org.agcodes.url_shortener.model.Url;

public interface UrlService {
  public Url shortenUrl(UrlDto urlDto);
  public String resolve(String id);
  public void delete(String id);
}
