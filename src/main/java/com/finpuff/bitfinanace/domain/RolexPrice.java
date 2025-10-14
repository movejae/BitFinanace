package com.finpuff.bitfinanace.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TimeSeries;
import org.springframework.data.mongodb.core.timeseries.Granularity;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Rolex Price TimeSeries Document
 * MongoDB TimeSeries Collection for efficient time-based queries and chart data
 */
@Getter
@Builder
@Document(collection = "rolex_prices")
@TimeSeries(timeField = "timestamp", metaField = "metadata", granularity = Granularity.SECONDS)
public class RolexPrice {

    @Id
    private String id;

    /**
     * Price timestamp
     */
    private Instant timestamp;

    /**
     * Metadata for TimeSeries
     */
    private Metadata metadata;

    /**
     * Rolex watch price in KRW
     */
    private BigDecimal price;

    /**
     * Model name (e.g., "Submariner", "Daytona")
     */
    private String modelName;

    /**
     * Reference number (e.g., "126610LN")
     */
    private String referenceNumber;

    /**
     * Condition (e.g., "NEW", "USED", "VINTAGE")
     */
    private String condition;

    @Getter
    @Builder
    public static class Metadata {
        private String brand;        // "ROLEX"
        private String category;     // "WATCH"
        private String source;       // "chrono24", "kream", etc.
    }
}