package org.agcodes.url_shortener.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.agcodes.url_shortener.dto.DailyStats;
import org.agcodes.url_shortener.model.UrlAudit;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

  private final MongoTemplate mongoTemplate;

  public long countCreated() {
    Query query = Query.query(
        Criteria.where("eventType").is("URL_CREATED")
    );
    return mongoTemplate.count(query, UrlAudit.class);
  }

  public List<DailyStats> urlsCreatedPerDay() {
    Aggregation agg = Aggregation.newAggregation(
        Aggregation.match(Criteria.where("eventType").is("URL_CREATED")),

        Aggregation.project()
            .and(DateOperators.dateOf("timestamp")
                .toString("%Y-%m-%d")
                .withTimezone(DateOperators.Timezone.valueOf("UTC")))
            .as("day"),

        Aggregation.group("day").count().as("count"),

        Aggregation.project("count")
            .and("_id").as("day")
            .andExclude("_id"),

        Aggregation.sort(Sort.Direction.ASC, "day")
    );

    return mongoTemplate.aggregate(
        agg,
        "url_audit",
        DailyStats.class
    ).getMappedResults();

  }

}
