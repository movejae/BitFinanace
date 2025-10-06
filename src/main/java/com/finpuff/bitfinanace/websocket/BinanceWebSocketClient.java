package com.finpuff.bitfinanace.websocket;

import com.finpuff.bitfinanace.dto.BinanceTradeData;
import com.finpuff.bitfinanace.service.BitcoinPriceService;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Binance Real-time Bitcoin Price WebSocket Client
 * Receives and displays real-time trade data every second
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BinanceWebSocketClient {

    @Value("${binance.websocket.url}")
    private String binanceWsUrl;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final Gson gson = new Gson();
    private final BitcoinPriceService bitcoinPriceService;
    private WebSocketClient webSocketClient;

    @PostConstruct
    public void connect() {
        try {
            webSocketClient = new WebSocketClient(new URI(binanceWsUrl)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    log.info("Connected to Binance WebSocket");
                }

                @Override
                public void onMessage(String message) {
                    try {
                        BinanceTradeData tradeData = gson.fromJson(message, BinanceTradeData.class);
                        String currentTime = LocalDateTime.now().format(TIME_FORMATTER);

                        // Save to MongoDB via Service
                        bitcoinPriceService.savePriceData(tradeData);

                        log.info("[{}] BTC Price: ${} | Volume: {} BTC", currentTime, tradeData.getPrice(), tradeData.getQuantity());

                    } catch (Exception e) {
                        log.error("Failed to parse message: {}", e.getMessage());
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.warn("WebSocket closed - Code: {}, Reason: {}", code, reason);
                }

                @Override
                public void onError(Exception ex) {
                    log.error("WebSocket error occurred", ex);
                }
            };

            webSocketClient.connect();

        } catch (Exception e) {
            log.error("Failed to connect to Binance WebSocket", e);
        }
    }

    @PreDestroy
    public void disconnect() {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.close();
            log.info("Disconnected from Binance WebSocket");
        }
    }
}
