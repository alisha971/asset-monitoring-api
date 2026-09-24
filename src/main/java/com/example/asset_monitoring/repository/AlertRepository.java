package com.example.asset_monitoring.repository;

import com.example.asset_monitoring.domain.AlertState;
import java.util.Optional;
import java.util.List;
import com.example.asset_monitoring.domain.Alert;

public interface AlertRepository{
    Alert save(Alert alert);
    Optional<Alert> findById(Long id);
    List<Alert> findBySensorId(Long sensorId);
    List<Alert> findBySensorIdAndState(Long sensorId, AlertState state);
}