package com.finpuff.bitfinanace.dto;

import com.finpuff.bitfinanace.domain.BitcoinPrice;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Bitcoin Price Response DTO
 */
@Getter
@Builder
public class BitcoinPriceResponse {

    private String id;
    private Instant timestamp;
    private BigDecimal price;
    private BigDecimal volume;
    private String symbol;
    private Long eventTime;
    private Long tradeTime;

    public static BitcoinPriceResponse from(BitcoinPrice bitcoinPrice) {
        return BitcoinPriceResponse.builder()
                .id(bitcoinPrice.getId())
                .timestamp(bitcoinPrice.getTimestamp())
                .price(bitcoinPrice.getPrice())
                .volume(bitcoinPrice.getVolume())
                .symbol(bitcoinPrice.getMetadata() != null ? bitcoinPrice.getMetadata().getSymbol() : null)
                .eventTime(bitcoinPrice.getEventTime())
                .tradeTime(bitcoinPrice.getTradeTime())
                .build();
    }
}
