package org.agcodes.url_shortener.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "urls")
@Getter
@Setter
@NoArgsConstructor  // Generates a no-args constructor
@AllArgsConstructor // Generates an all-args constructor
public class Url {
  @Id
  private String id;
  @Indexed(unique = true)
  private String originalUrl;
  @Indexed(unique = true)
  private String shortUrl;
  private LocalDateTime createdAt;
  private LocalDateTime expirationDate;
}
