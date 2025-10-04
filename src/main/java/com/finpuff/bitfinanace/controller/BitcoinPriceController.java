package com.finpuff.bitfinanace.controller;

import com.finpuff.bitfinanace.service.BitcoinPriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Bitcoin Price REST API Controller
 * Provides endpoints for retrieving bitcoin price data
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/bitcoin/prices")
@RequiredArgsConstructor
public class BitcoinPriceController {

    private final BitcoinPriceService bitcoinPriceService;

    /**
     * Get bitcoin prices for the last day
     * GET /api/v1/bitcoin/prices/day
     */
    @GetMapping("/day")
    public ResponseEntity<?> getDayPrices() {
        // TODO: Implement day price logic
        return ResponseEntity.ok().build();
    }

    /**
     * Get bitcoin prices for the last month
     * GET /api/v1/bitcoin/prices/month
     */
    @GetMapping("/month")
    public ResponseEntity<?> getMonthPrices() {
        // TODO: Implement month price logic
        return ResponseEntity.ok().build();
    }

    /**
     * Get bitcoin prices for the last week
     * GET /api/v1/bitcoin/prices/week
     */
    @GetMapping("/week")
    public ResponseEntity<?> getWeekPrices() {
        // TODO: Implement week price logic
        return ResponseEntity.ok().build();
    }

    /**
     * Get latest bitcoin price
     * GET /api/v1/bitcoin/prices/latest
     */
    @GetMapping("/latest")
    public ResponseEntity<?> getLatestPrice() {
        // TODO: Implement latest price logic
        return ResponseEntity.ok().build();
    }
}
