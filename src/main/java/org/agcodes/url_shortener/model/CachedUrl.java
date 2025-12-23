package org.agcodes.url_shortener.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Redis cache object (lightweight)
// Serializable → required for Redis
// Contains only what you need no extra not required fields (better performance)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CachedUrl implements Serializable {

  private String id;
  private String originalUrl;

}
