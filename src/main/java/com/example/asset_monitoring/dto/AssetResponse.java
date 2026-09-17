package com.example.asset_monitoring.dto;

import com.example.asset_monitoring.domain.Asset;
import com.example.asset_monitoring.domain.AssetStatus;
import java.time.Instant;

public record AssetResponse(
    Long id,
    String code,
    String name,
    AssetStatus status,
    Instant createdAt,
    int sensorCount
){
    public static from(Asset asset){
        return new AssetResponse(
            asset.getId(),
            asset.getCode(),
            asset.getName(),
            asset.getStatus(),
            asset.getCreatedAt(),
            asset.getSensors().size()
        );
    }
}