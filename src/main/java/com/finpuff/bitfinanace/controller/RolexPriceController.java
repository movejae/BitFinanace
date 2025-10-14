package com.finpuff.bitfinanace.controller;

import com.finpuff.bitfinanace.dto.RolexPriceResponse;
import com.finpuff.bitfinanace.service.RolexPriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rolex Price REST API Controller
 * Provides endpoints for retrieving rolex price data
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/rolex/prices")
@RequiredArgsConstructor
@Tag(name = "Rolex Price", description = "롤렉스 시세 조회 API")
public class RolexPriceController {

    private final RolexPriceService rolexPriceService;

    @Operation(summary = "최근 24시간 롤렉스 시세 조회", description = "특정 모델의 최근 24시간 동안의 롤렉스 시세 데이터를 조회합니다.")
    @GetMapping("/day")
    public ResponseEntity<List<RolexPriceResponse>> getDayPrices(
            @RequestParam(name = "modelName") String modelName) {
        return ResponseEntity.ok(
                rolexPriceService.getDayPrices(modelName).stream()
                        .map(RolexPriceResponse::from)
                        .toList()
        );
    }

    @Operation(summary = "최근 30일 롤렉스 시세 조회", description = "특정 모델의 최근 30일 동안의 롤렉스 시세 데이터를 조회합니다.")
    @GetMapping("/month")
    public ResponseEntity<List<RolexPriceResponse>> getMonthPrices(
            @RequestParam(name = "modelName") String modelName) {
        return ResponseEntity.ok(
                rolexPriceService.getMonthPrices(modelName).stream()
                        .map(RolexPriceResponse::from)
                        .toList()
        );
    }

    @Operation(summary = "최근 7일 롤렉스 시세 조회", description = "특정 모델의 최근 7일 동안의 롤렉스 시세 데이터를 조회합니다.")
    @GetMapping("/week")
    public ResponseEntity<List<RolexPriceResponse>> getWeekPrices(
            @RequestParam(name = "modelName") String modelName) {
        return ResponseEntity.ok(
                rolexPriceService.getWeekPrices(modelName).stream()
                        .map(RolexPriceResponse::from)
                        .toList()
        );
    }

    @Operation(summary = "최신 롤렉스 시세 조회", description = "특정 모델의 가장 최근 롤렉스 시세 데이터 1건을 조회합니다.")
    @GetMapping("/latest")
    public ResponseEntity<RolexPriceResponse> getLatestPrice(
            @RequestParam(name = "modelName") String modelName) {
        return ResponseEntity.ok(RolexPriceResponse.from(rolexPriceService.getLatestPrice(modelName)));
    }
}
