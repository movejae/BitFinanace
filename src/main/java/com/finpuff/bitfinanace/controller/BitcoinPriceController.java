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
        return ResponseEntity.ok(bitcoinPriceService.getDayPrices());
    }

    /**
     * Get bitcoin prices for the last month
     * GET /api/v1/bitcoin/prices/month
     */
    @GetMapping("/month")
    public ResponseEntity<?> getMonthPrices() {
        return ResponseEntity.ok(bitcoinPriceService.getMonthPrices());
    }

    /**
     * Get bitcoin prices for the last week
     * GET /api/v1/bitcoin/prices/week
     */
    @GetMapping("/week")
    public ResponseEntity<?> getWeekPrices() {
        return ResponseEntity.ok(bitcoinPriceService.getWeekPrices());
    }

    /**
     * Get latest bitcoin price
     * GET /api/v1/bitcoin/prices/latest
     */
    @GetMapping("/latest")
    public ResponseEntity<?> getLatestPrice() {
        return ResponseEntity.ok(bitcoinPriceService.getLatestPrice());
    }
}
