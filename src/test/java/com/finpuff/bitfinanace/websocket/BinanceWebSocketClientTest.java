package com.finpuff.bitfinanace.websocket;

import com.finpuff.bitfinanace.dto.BinanceTradeData;
import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Binance WebSocket Client 단위 테스트
 * Spring Boot 컨텍스트 없이 순수 Java로 WebSocket 연결 테스트
 */
class BinanceWebSocketClientTest {

    private static final String BINANCE_WS_URL = "wss://stream.binance.com:9443/ws/btcusdt@trade";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Test
    void testBinanceWebSocketConnection() throws Exception {
        // Given
        Gson gson = new Gson();
        CountDownLatch latch = new CountDownLatch(5);  // 5개의 메시지를 받을 때까지 대기

        // When
        WebSocketClient client = new WebSocketClient(new URI(BINANCE_WS_URL)) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("✅ Connected to Binance WebSocket");
            }

            @Override
            public void onMessage(String message) {
                try {
                    BinanceTradeData tradeData = gson.fromJson(message, BinanceTradeData.class);
                    String currentTime = LocalDateTime.now().format(TIME_FORMATTER);

                    System.out.printf("[%s] BTC Price: $%s | Volume: %s BTC%n",
                            currentTime,
                            tradeData.getPrice(),
                            tradeData.getQuantity());

                    // 검증
                    assertNotNull(tradeData.getPrice(), "Price should not be null");
                    assertNotNull(tradeData.getQuantity(), "Quantity should not be null");
                    assertEquals("BTCUSDT", tradeData.getSymbol(), "Symbol should be BTCUSDT");

                    latch.countDown();

                } catch (Exception e) {
                    fail("Failed to parse message: " + e.getMessage());
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("❌ WebSocket closed - Code: " + code + ", Reason: " + reason);
            }

            @Override
            public void onError(Exception ex) {
                fail("WebSocket error: " + ex.getMessage());
            }
        };

        // 연결 시작
        client.connect();

        // Then - 5개의 메시지를 받을 때까지 최대 30초 대기
        boolean received = latch.await(30, TimeUnit.SECONDS);
        assertTrue(received, "Should receive at least 5 messages within 30 seconds");

        // 정리
        client.close();
        System.out.println("✅ Test completed successfully - Received 5 messages");
    }

    @Test
    void testBinanceWebSocketConnectionWithTimeout() throws Exception {
        // Given
        CountDownLatch latch = new CountDownLatch(1);

        // When
        WebSocketClient client = new WebSocketClient(new URI(BINANCE_WS_URL)) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("✅ Connected successfully");
                latch.countDown();
            }

            @Override
            public void onMessage(String message) {
                // 메시지 수신만 확인
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("❌ Connection closed");
            }

            @Override
            public void onError(Exception ex) {
                fail("Connection failed: " + ex.getMessage());
            }
        };

        client.connect();

        // Then - 10초 내에 연결되어야 함
        boolean connected = latch.await(10, TimeUnit.SECONDS);
        assertTrue(connected, "Should connect within 10 seconds");

        // 정리
        client.close();
        System.out.println("✅ Connection test passed");
    }
}
