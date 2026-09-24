package com.example.asset_monitoring.dto;

import java.time.Instant;

import com.example.asset_monitoring.domain.Alert;
import com.example.asset_monitoring.domain.AlertSeverity;
import com.example.asset_monitoring.domain.AlertState;
import com.example.asset_monitoring.domain.Sensor;

public record AlertResponse(
    Long id,
    Long sensorId,
    String sensorSerial,
    AlertState state,
    AlertSeverity severity,
    Instant raisedAt,
    Instant ackAt,
    Instant resolvedAt
){
    public static AlertResponse from(Alert alert){

        return new AlertResponse(
            alert.getId(),
            alert.getSensor().getId(),
            alert.getSensor().getSerial(),
            alert.getState(),
            alert.getAlertSeverity(),
            alert.raisedAt(),
            alert.ackAt(),
            alert.resolvedAt()
        );
    }
}