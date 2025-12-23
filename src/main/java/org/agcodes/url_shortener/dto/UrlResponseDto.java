package org.agcodes.url_shortener.dto;

public record UrlResponseDto(
    String originalUrl,
    String shortUrl,
    String expirationDate
) {

}