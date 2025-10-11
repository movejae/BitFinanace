package com.finpuff.bitfinanace.service;

import com.finpuff.bitfinanace.domain.BitcoinPrice;
import com.finpuff.bitfinanace.dto.BinanceTradeData;
import com.finpuff.bitfinanace.repository.BitcoinPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Bitcoin Price Service
 * Handles business logic for saving and retrieving bitcoin price data
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BitcoinPriceService {

    private final BitcoinPriceRepository bitcoinPriceRepository;

    public void savePriceData(BinanceTradeData tradeData) {
        try {
            BitcoinPrice bitcoinPrice = BitcoinPrice.builder()
		            .price(tradeData.getPriceAsBigDecimal())
                    .volume(tradeData.getQuantityAsBigDecimal())
                    .eventTime(tradeData.getEventTime())
                    .tradeTime(tradeData.getTradeTime())
		            .timestamp(Instant.ofEpochMilli(tradeData.getTradeTime()))
		            .metadata(BitcoinPrice.Metadata.builder()
				            .symbol(tradeData.getSymbol())
				            .eventType(tradeData.getEventType())
				            .source("binance")
				            .build())
                    .build();

            bitcoinPriceRepository.save(bitcoinPrice);
            log.debug("Saved to MongoDB: {}", bitcoinPrice.getPrice());

        } catch (Exception e) {
            log.error("Failed to save to MongoDB", e);
        }
    }

    /**
     * Get bitcoin prices for the last day (24 hours)
     */
    public List<BitcoinPrice> getDayPrices() {
        Instant now = Instant.now();
        Instant oneDayAgo = now.minus(1, ChronoUnit.DAYS);
        return bitcoinPriceRepository.findByTimestampBetweenOrderByTimestampAsc(oneDayAgo, now);
    }

    /**
     * Get bitcoin prices for the last week (7 days)
     */
    public List<BitcoinPrice> getWeekPrices() {
        Instant now = Instant.now();
        Instant oneWeekAgo = now.minus(7, ChronoUnit.DAYS);
        return bitcoinPriceRepository.findByTimestampBetweenOrderByTimestampAsc(oneWeekAgo, now);
    }

    /**
     * Get bitcoin prices for the last month (30 days)
     */
    public List<BitcoinPrice> getMonthPrices() {
        Instant now = Instant.now();
        Instant oneMonthAgo = now.minus(30, ChronoUnit.DAYS);
        return bitcoinPriceRepository.findByTimestampBetweenOrderByTimestampAsc(oneMonthAgo, now);
    }

    /**
     * Get latest bitcoin price
     */
    public BitcoinPrice getLatestPrice() {
        return bitcoinPriceRepository.findTopByOrderByTimestampDesc();
    }
}
