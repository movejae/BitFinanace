package com.finpuff.bitfinanace.controller;

import com.finpuff.bitfinanace.domain.BitcoinPrice;
import com.finpuff.bitfinanace.service.BitcoinPriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Bitcoin Price REST API Controller
 * Provides endpoints for retrieving bitcoin price data
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/bitcoin/prices")
@RequiredArgsConstructor
@Tag(name = "Bitcoin Price", description = "비트코인 시세 조회 API")
public class BitcoinPriceController {

    private final BitcoinPriceService bitcoinPriceService;

    @Operation(summary = "최근 24시간 비트코인 시세 조회", description = "최근 24시간 동안의 모든 비트코인 시세 데이터를 조회합니다.")
    @GetMapping("/day")
    public ResponseEntity<List<BitcoinPrice>> getDayPrices() {
        return ResponseEntity.ok(bitcoinPriceService.getDayPrices());
    }

    @Operation(summary = "최근 30일 비트코인 시세 조회", description = "최근 30일 동안의 모든 비트코인 시세 데이터를 조회합니다.")
    @GetMapping("/month")
    public ResponseEntity<List<BitcoinPrice>> getMonthPrices() {
        return ResponseEntity.ok(bitcoinPriceService.getMonthPrices());
    }

    @Operation(summary = "최근 7일 비트코인 시세 조회", description = "최근 7일 동안의 모든 비트코인 시세 데이터를 조회합니다.")
    @GetMapping("/week")
    public ResponseEntity<List<BitcoinPrice>> getWeekPrices() {
        return ResponseEntity.ok(bitcoinPriceService.getWeekPrices());
    }

    @Operation(summary = "최신 비트코인 시세 조회", description = "가장 최근의 비트코인 시세 데이터 1건을 조회합니다.")
    @GetMapping("/latest")
    public ResponseEntity<BitcoinPrice> getLatestPrice() {
        return ResponseEntity.ok(bitcoinPriceService.getLatestPrice());
    }
}
