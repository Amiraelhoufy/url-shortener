package org.agcodes.url_shortener.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

  private Redis redis = new Redis();
  private Url url = new Url();
  private Hash hash = new Hash();

  @Getter
  @Setter
  public static class Redis {
    private String host;
    private int port;
  }
  @Getter
  @Setter
  public static class Url {
    private int expirationDays;
    private String baseDomain;
  }

  @Getter
  @Setter
  public static class Hash {
    private int length;
    private String strategy;
  }
}
