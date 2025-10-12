package com.finpuff.bitfinanace.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Binance WebSocket Trade Stream DTO
 * wss://stream.binance.com:9443/ws/btcusdt@trade
 */
@Getter
@ToString
public class BinanceTradeData {

    @SerializedName("e")
    private String eventType;  // "trade"

    @SerializedName("E")
    private Long eventTime;  // Event time

    @SerializedName("s")
    private String symbol;  // Symbol (e.g., "BTCUSDT")

    @SerializedName("p")
    private String price;  // Price

    @SerializedName("q")
    private String quantity;  // Quantity

    @SerializedName("T")
    private Long tradeTime;  // Trade time

    public BigDecimal getPriceAsBigDecimal() {
        return new BigDecimal(price);
    }

    public BigDecimal getQuantityAsBigDecimal() {
        return new BigDecimal(quantity);
    }
}
