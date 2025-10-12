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
 * Bitcoin Price TimeSeries Document
 * MongoDB TimeSeries Collection for efficient time-based queries and chart data
 */
@Getter
@Builder
@Document(collection = "bitcoin_prices")
@TimeSeries(timeField = "timestamp", metaField = "metadata", granularity = Granularity.SECONDS)
public class BitcoinPrice {


    @Id
    private String id;

    /**
     * Trade timestamp
     */
    private Instant timestamp;

    /**
     * Metadata for TimeSeries
     */
    private Metadata metadata;

    /**
     * Trade price in USDT
     */
    private BigDecimal price;

    /**
     * Trade volume in BTC
     */
    private BigDecimal volume;

    /**
     * Trade event time from Binance
     */
    private Long eventTime;

    /**
     * Trade time from Binance
     */
    private Long tradeTime;

    @Getter
    @Builder
    public static class Metadata {
        private String symbol;      // "BTCUSDT"
        private String eventType;   // "trade"
        private String source;      // "binance"
    }
}
