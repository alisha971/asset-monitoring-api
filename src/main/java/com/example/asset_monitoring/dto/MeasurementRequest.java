package com.example.asset_monitoring.dto;

import java.math.BigDecimal;
import java.time.Instant;
import jakarta.validation.constraints.*;

import com.example.asset_monitoring.domain.Sensor;

public record MeasurementRequest(

    @NotNull(message = "Value is required")
    @DecimalMin(value = "-1000.0")
    @DecimalMax(value = "100000.0")
    BigDecimal value,

    @Size(max=16)
    String unit,

    @PastOrPresent(message = "recordedAt cannot be in future")
    Instant recordedAt
){}