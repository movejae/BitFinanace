package com.finpuff.bitfinanace.repository;

import com.finpuff.bitfinanace.domain.RolexPrice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Rolex Price TimeSeries Repository
 * Provides efficient queries for time-based chart data
 */
@Repository
public interface RolexPriceRepository extends MongoRepository<RolexPrice, String> {

    /**
     * Find prices within time range for chart data
     */
    List<RolexPrice> findByTimestampBetweenOrderByTimestampAsc(Instant start, Instant end);

    /**
     * Find prices by model name within time range for chart data
     */
    List<RolexPrice> findByModelNameAndTimestampBetweenOrderByTimestampAsc(String modelName, Instant start, Instant end);

    /**
     * Find the most recent price (DB level optimization)
     */
    RolexPrice findTopByOrderByTimestampDesc();

    /**
     * Find the most recent price by model name (DB level optimization)
     */
    RolexPrice findTopByModelNameOrderByTimestampDesc(String modelName);
}
