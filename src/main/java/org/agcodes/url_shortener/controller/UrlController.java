package org.agcodes.url_shortener.controller;

import java.net.URI;
import org.agcodes.url_shortener.dto.UrlDto;
import org.agcodes.url_shortener.dto.UrlResponseDto;
import org.agcodes.url_shortener.model.Url;
import org.agcodes.url_shortener.service.UrlServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class UrlController {
  @Autowired
  private UrlServiceImpl urlService;
  @PostMapping("/shorten")
  public ResponseEntity<UrlResponseDto> shortenUrl(@RequestBody UrlDto urlDto) {
    if (urlDto.originalUrl() == null || urlDto.originalUrl().isBlank()) {
      return ResponseEntity.badRequest().build();
    }
    Url url = urlService.shortenUrl(urlDto);
    return ResponseEntity.ok(new UrlResponseDto(
        url.getOriginalUrl(),
        url.getShortUrl(),
        url.getExpirationDate().toString()
    ));
  }

  @GetMapping("/{shortUrl}")
  public ResponseEntity<Void> redirect(@PathVariable String shortUrl) {
    String OriginalUrl = urlService.resolve(shortUrl);

    return ResponseEntity
        .status(HttpStatus.FOUND)
        .location(URI.create(OriginalUrl))
        .build();
  }

  @DeleteMapping("/{shortUrl}")
  public ResponseEntity<Void> delete(@PathVariable String shortUrl){
    urlService.delete(shortUrl);
    return ResponseEntity.noContent().build(); // 204 No Content

  }

}
