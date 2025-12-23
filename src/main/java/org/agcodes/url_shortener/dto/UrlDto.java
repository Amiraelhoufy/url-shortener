package org.agcodes.url_shortener.dto;

import java.time.LocalDateTime;
import java.util.Optional;

public record UrlDto(
    String originalUrl,
    LocalDateTime expirationDate // can be null if not provided
) {

}
