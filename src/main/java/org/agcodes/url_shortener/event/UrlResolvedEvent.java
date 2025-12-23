package org.agcodes.url_shortener.event;

public record UrlResolvedEvent(String shortUrl, String originalUrl) {
}