package org.agcodes.url_shortener.service.strategy;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.agcodes.url_shortener.service.strategy.UrlHashStrategy;
import org.springframework.stereotype.Component;

@Component
public class UrlHashStrategyFactory {

  private final Map<String, UrlHashStrategy> strategies;

  public UrlHashStrategyFactory(List<UrlHashStrategy> strategies) {
    this.strategies = strategies.stream()
        .collect(Collectors.toMap(
            s -> s.getClass().getSimpleName().toLowerCase().replace("hashstrategy", ""),
            Function.identity()
        ));
  }

  public UrlHashStrategy getStrategy(String name) {
    return strategies.get(name);
  }
}
