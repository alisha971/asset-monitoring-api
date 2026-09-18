package com.example.asset_monitoring.dto;

import com.example.asset_monitoring.domain.SensorType;
import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public record SensorRequest(

    @NotBlank(message = "Serial is required!")
    @Pattern(
        regexp = "^S\\d+$",
        message = "Serial must in pattern S followed by digits"
    )
    @Size(max=64)
    String serial,

    @NotNull(message = "Type is required")
    SensorType type,

    @NotNull(message = "Warning threshold is required")
    BigDecimal warning,

    @NotNull(message = "Critical threshold is required")
    BigDecimal critical
){}