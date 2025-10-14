package com.finpuff.bitfinanace.dto;

import com.finpuff.bitfinanace.domain.RolexPrice;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Rolex Price Response DTO
 */
@Getter
@Builder
public class RolexPriceResponse {

    private String id;
    private Instant timestamp;
    private BigDecimal price;
    private String modelName;
    private String referenceNumber;
    private String condition;
    private String brand;
    private String source;

    public static RolexPriceResponse from(RolexPrice rolexPrice) {
        return RolexPriceResponse.builder()
                .id(rolexPrice.getId())
                .timestamp(rolexPrice.getTimestamp())
                .price(rolexPrice.getPrice())
                .modelName(rolexPrice.getModelName())
                .referenceNumber(rolexPrice.getReferenceNumber())
                .condition(rolexPrice.getCondition())
                .brand(rolexPrice.getMetadata() != null ? rolexPrice.getMetadata().getBrand() : null)
                .source(rolexPrice.getMetadata() != null ? rolexPrice.getMetadata().getSource() : null)
                .build();
    }
}
