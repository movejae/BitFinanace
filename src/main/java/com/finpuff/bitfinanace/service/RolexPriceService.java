package com.finpuff.bitfinanace.service;

import com.finpuff.bitfinanace.domain.RolexPrice;
import com.finpuff.bitfinanace.repository.RolexPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Rolex Price Service
 * Handles business logic for retrieving rolex price data
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RolexPriceService {

    private final RolexPriceRepository rolexPriceRepository;

    /**
     * Get rolex prices for the last day (24 hours) by model name
     */
    public List<RolexPrice> getDayPrices(String modelName) {
        Instant now = Instant.now();
        Instant oneDayAgo = now.minus(1, ChronoUnit.DAYS);
        return rolexPriceRepository.findByModelNameAndTimestampBetweenOrderByTimestampAsc(modelName, oneDayAgo, now);
    }

    /**
     * Get rolex prices for the last week (7 days) by model name
     */
    public List<RolexPrice> getWeekPrices(String modelName) {
        Instant now = Instant.now();
        Instant oneWeekAgo = now.minus(7, ChronoUnit.DAYS);
        return rolexPriceRepository.findByModelNameAndTimestampBetweenOrderByTimestampAsc(modelName, oneWeekAgo, now);
    }

    /**
     * Get rolex prices for the last month (30 days) by model name
     */
    public List<RolexPrice> getMonthPrices(String modelName) {
        Instant now = Instant.now();
        Instant oneMonthAgo = now.minus(30, ChronoUnit.DAYS);
        return rolexPriceRepository.findByModelNameAndTimestampBetweenOrderByTimestampAsc(modelName, oneMonthAgo, now);
    }

    /**
     * Get latest rolex price by model name
     */
    public RolexPrice getLatestPrice(String modelName) {
        return rolexPriceRepository.findTopByModelNameOrderByTimestampDesc(modelName);
    }
}
