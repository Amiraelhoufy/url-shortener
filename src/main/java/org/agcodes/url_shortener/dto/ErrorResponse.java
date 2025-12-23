package org.agcodes.url_shortener.dto;

public record ErrorResponse(
    String code,
    String message
) {}
