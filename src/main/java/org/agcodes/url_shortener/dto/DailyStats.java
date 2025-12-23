package org.agcodes.url_shortener.dto;


public record DailyStats(
    String day,
    Number count
) {
  public long countAsLong() {
    return count.longValue();
  }
}