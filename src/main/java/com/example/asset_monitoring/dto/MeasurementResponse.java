package com.example.asset_monitoring.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.example.asset_monitoring.domain.AlertSeverity;
import com.example.asset_monitoring.domain.Measurement;

public record MeasurementResponse(
    Long id,
    Long sensorId,
    String sensorSerial,
    BigDecimal value,
    String unit,
    Instant recordedAt,
    AlertSeverity severity
){
    public static MeasurementResponse from(Measurement m, AlertSeverity severity){
        return new MeasurementResponse(
            m.getId(),
            m.getSensorId(),
            m.getSensor().getSerial(),
            m.getValue(),
            m.getUnit(),
            m.getRecordedAt(),
            severity
        );
    }
}