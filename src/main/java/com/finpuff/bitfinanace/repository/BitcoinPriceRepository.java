package com.finpuff.bitfinanace.repository;

import com.finpuff.bitfinanace.domain.BitcoinPrice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Bitcoin Price TimeSeries Repository
 * Provides efficient queries for time-based chart data
 */
@Repository
public interface BitcoinPriceRepository extends MongoRepository<BitcoinPrice, String> {

    /**
     * Find prices within time range for chart data
     */
    List<BitcoinPrice> findByTimestampBetweenOrderByTimestampAsc(Instant start, Instant end);

    /**
     * Find the most recent price (DB level optimization)
     */
    BitcoinPrice findTopByOrderByTimestampDesc();
}
