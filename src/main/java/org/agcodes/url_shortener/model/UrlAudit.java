package org.agcodes.url_shortener.model;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "url_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlAudit {

  @Id
  private String id;

  private String eventType; // URL_CREATED, URL_RESOLVED, URL_DELETED
  private String shortUrl;
  private String originalUrl;
  private Date timestamp;

}
