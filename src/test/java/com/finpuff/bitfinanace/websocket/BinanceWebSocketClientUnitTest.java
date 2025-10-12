package com.finpuff.bitfinanace.websocket;

import com.finpuff.bitfinanace.dto.BinanceTradeData;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Binance WebSocket 클라이언트 단위 테스트
 * 실제 연결 없이 메시지 파싱 및 처리 로직만 검증
 */
class BinanceWebSocketClientUnitTest {

    private Gson gson;

    @BeforeEach
    void setUp() {
        gson = new Gson();
    }

    @Test
    @DisplayName("Binance 메시지를 정상적으로 파싱할 수 있어야 한다")
    void shouldParseValidBinanceMessage() {
        // Given - 실제 Binance WebSocket 메시지 형식
        String validMessage = """
                {
                    "e": "trade",
                    "E": 1234567890,
                    "s": "BTCUSDT",
                    "p": "50000.12345678",
                    "q": "0.00123456",
                    "T": 1234567890
                }
                """;

        // When
        BinanceTradeData tradeData = gson.fromJson(validMessage, BinanceTradeData.class);

        // Then
        assertNotNull(tradeData, "Trade data should not be null");
        assertEquals("trade", tradeData.getEventType(), "Event type should be 'trade'");
        assertEquals("BTCUSDT", tradeData.getSymbol(), "Symbol should be 'BTCUSDT'");
        assertEquals("50000.12345678", tradeData.getPrice(), "Price should match");
        assertEquals("0.00123456", tradeData.getQuantity(), "Quantity should match");
    }

    @Test
    @DisplayName("가격을 BigDecimal로 변환할 수 있어야 한다")
    void shouldConvertPriceToBigDecimal() {
        // Given
        String message = """
                {
                    "e": "trade",
                    "E": 1234567890,
                    "s": "BTCUSDT",
                    "p": "117435.44000000",
                    "q": "0.00009000",
                    "T": 1234567890
                }
                """;

        // When
        BinanceTradeData tradeData = gson.fromJson(message, BinanceTradeData.class);
        BigDecimal price = tradeData.getPriceAsBigDecimal();
        BigDecimal quantity = tradeData.getQuantityAsBigDecimal();

        // Then
        assertNotNull(price, "Price BigDecimal should not be null");
        assertNotNull(quantity, "Quantity BigDecimal should not be null");
        assertTrue(price.compareTo(BigDecimal.ZERO) > 0, "Price should be positive");
        assertTrue(quantity.compareTo(BigDecimal.ZERO) > 0, "Quantity should be positive");
        assertEquals(new BigDecimal("117435.44000000"), price, "Price value should match");
        assertEquals(new BigDecimal("0.00009000"), quantity, "Quantity value should match");
    }

    @Test
    @DisplayName("잘못된 JSON 형식은 예외를 발생시켜야 한다")
    void shouldThrowExceptionForInvalidJson() {
        // Given
        String invalidMessage = "{ invalid json }";

        // When & Then
        assertThrows(Exception.class, () -> gson.fromJson(invalidMessage, BinanceTradeData.class), "Invalid JSON should throw exception");
    }

    @Test
    @DisplayName("필수 필드가 누락된 메시지를 처리할 수 있어야 한다")
    void shouldHandleMessageWithMissingFields() {
        // Given - price 필드가 누락된 메시지
        String messageWithMissingFields = """
                {
                    "e": "trade",
                    "s": "BTCUSDT"
                }
                """;

        // When
        BinanceTradeData tradeData = gson.fromJson(messageWithMissingFields, BinanceTradeData.class);

        // Then
        assertNotNull(tradeData, "Trade data should be parsed even with missing fields");
        assertEquals("trade", tradeData.getEventType());
        assertEquals("BTCUSDT", tradeData.getSymbol());
        assertNull(tradeData.getPrice(), "Missing price field should be null");
    }

    @Test
    @DisplayName("다양한 가격 범위를 정확하게 파싱할 수 있어야 한다")
    void shouldParseDifferentPriceRanges() {
        // Given - 다양한 가격대
        String[] messages = {
                """
                {"e":"trade","s":"BTCUSDT","p":"100000.00000000","q":"0.001"}
                """,
                """
                {"e":"trade","s":"BTCUSDT","p":"50000.12345678","q":"1.5"}
                """,
                """
                {"e":"trade","s":"BTCUSDT","p":"0.00000001","q":"1000000"}
                """
        };

        // When & Then
        for (String message : messages) {
            BinanceTradeData tradeData = gson.fromJson(message, BinanceTradeData.class);
            assertNotNull(tradeData.getPrice(), "Price should not be null");
            assertNotNull(tradeData.getQuantity(), "Quantity should not be null");
            assertTrue(tradeData.getPriceAsBigDecimal().compareTo(BigDecimal.ZERO) > 0,
                    "Price should be positive: " + tradeData.getPrice());
        }
    }

    @Test
    @DisplayName("Symbol이 BTCUSDT가 아닌 경우도 처리할 수 있어야 한다")
    void shouldHandleDifferentSymbols() {
        // Given
        String[] symbols = {"BTCUSDT", "ETHUSDT", "BNBUSDT"};

        for (String symbol : symbols) {
            String message = String.format("""
                    {
                        "e": "trade",
                        "s": "%s",
                        "p": "1000.00",
                        "q": "1.0"
                    }
                    """, symbol);

            // When
            BinanceTradeData tradeData = gson.fromJson(message, BinanceTradeData.class);

            // Then
            assertEquals(symbol, tradeData.getSymbol(),
                    "Symbol should be parsed correctly: " + symbol);
        }
    }

    @Test
    @DisplayName("BigDecimal 변환 시 null 체크가 필요하다")
    void shouldHandleNullPriceInBigDecimalConversion() {
        // Given - price가 null인 경우
        String message = """
                {
                    "e": "trade",
                    "s": "BTCUSDT"
                }
                """;

        // When
        BinanceTradeData tradeData = gson.fromJson(message, BinanceTradeData.class);

        // Then & When
        assertThrows(NullPointerException.class, tradeData::getPriceAsBigDecimal, "Null price should throw NPE when converting to BigDecimal");
    }

    @Test
    @DisplayName("타임스탬프 필드를 정상적으로 파싱할 수 있어야 한다")
    void shouldParseTimestampFields() {
        // Given
        String message = """
                {
                    "e": "trade",
                    "E": 1234567890123,
                    "s": "BTCUSDT",
                    "p": "50000.00",
                    "q": "1.0",
                    "T": 1234567890456
                }
                """;

        // When
        BinanceTradeData tradeData = gson.fromJson(message, BinanceTradeData.class);

        // Then
        assertNotNull(tradeData.getEventTime(), "Event time should not be null");
        assertNotNull(tradeData.getTradeTime(), "Trade time should not be null");
        assertEquals(1234567890123L, tradeData.getEventTime(), "Event time should match");
        assertEquals(1234567890456L, tradeData.getTradeTime(), "Trade time should match");
    }

    @Test
    @DisplayName("toString() 메서드가 정상적으로 동작해야 한다")
    void shouldHaveWorkingToString() {
        // Given
        String message = """
                {
                    "e": "trade",
                    "s": "BTCUSDT",
                    "p": "50000.00",
                    "q": "1.0"
                }
                """;

        // When
        BinanceTradeData tradeData = gson.fromJson(message, BinanceTradeData.class);
        String toString = tradeData.toString();

        // Then
        assertNotNull(toString, "toString should not return null");
        assertTrue(toString.contains("BTCUSDT"), "toString should contain symbol");
        assertTrue(toString.contains("50000.00"), "toString should contain price");
    }
}
