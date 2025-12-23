package org.agcodes.url_shortener.dto;

public record UrlErrorResponseDto(
    String status,
    String error
) {

}
