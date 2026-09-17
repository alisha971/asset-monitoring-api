package com.example.asset_monitoring.repository;

import com.example.asset_monitoring.domain.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.time.Instant;

public interface MeasurementRepository {

    Measurement save(Measurement m);
    Optional<Measurement> findById(Long id);
    List<Measurement> findBySensorId(Long sensorId);
    List<Measurement> findByAssetId(Long assetId);
    List<Measurement> findBySensorIdAndByRecordedAtBetween(Long sensorId, Instant from, Instant to);
    List<Measurement> findAboveThreshold(Long assetId, BigDecimal threshold);
    Optional<Measurement> findLatestBySensorId(Long sensorId);

}
