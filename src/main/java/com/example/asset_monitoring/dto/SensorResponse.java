package com.example.asset_monitoring.dto;

import java.math.BigDecimal;

import com.example.asset_monitoring.domain.Sensor;
import com.example.asset_monitoring.domain.SensorType;
import jakarta.validation.constraints.*;

public record SensorResponse(
    Long id,
    String serial,
    SensorType type,
    boolean active,
    BigDecimal warning,
    BigDecimal critical,
    Long assetId
){
    public static SensorResponse from(Sensor sensor){
        return new SensorResponse(
            sensor.getId(),
            sensor.getSerial(),
            sensor.getType(),
            sensor.isActive(),
            sensor.getWarningThreshold(),
            sensor.getCriticalThreshold(),
            sensor.getAsset().getId()
        );
    }
}