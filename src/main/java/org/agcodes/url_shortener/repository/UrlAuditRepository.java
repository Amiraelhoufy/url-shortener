package org.agcodes.url_shortener.repository;

import org.agcodes.url_shortener.model.UrlAudit;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlAuditRepository extends MongoRepository<UrlAudit, String> {

}
