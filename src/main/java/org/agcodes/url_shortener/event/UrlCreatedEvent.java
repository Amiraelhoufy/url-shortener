package org.agcodes.url_shortener.event;

import org.agcodes.url_shortener.model.Url;

public record UrlCreatedEvent(Url url) {
}