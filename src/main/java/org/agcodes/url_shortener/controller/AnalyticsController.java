package org.agcodes.url_shortener.controller;

import lombok.RequiredArgsConstructor;
import org.agcodes.url_shortener.dto.DailyStats;
import org.agcodes.url_shortener.service.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

  private final AnalyticsService analyticsService;

  @GetMapping("/created/count")
  public long totalCreated() {
    return analyticsService.countCreated();
  }

  @GetMapping("/created/daily")
  public Iterable<DailyStats> urlsCreatedPerDay() {
    return analyticsService.urlsCreatedPerDay();
  }

}