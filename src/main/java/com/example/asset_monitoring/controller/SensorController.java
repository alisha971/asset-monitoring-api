package com.example.asset_monitoring.controller;

import java.util.List;
import java.net.URI;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

import com.example.asset_monitoring.dto.SensorRequest;
import com.example.asset_monitoring.dto.SensorResponse;
import com.example.asset_monitoring.service.SensorService;

@RestController
@RequestMapping("/api/v1/assets")
public class SensorController{

    private final SensorService sensorService;

    public SensorController(SensorService sensorService){
        this.sensorService = sensorService;
    }

    // e1 - list sensors for an asset - GET - list()
    @GetMapping("/{assetId}/sensors")
    public List<SensorResponse> getForAsset(@PathVariable Long assetId){
        return sensorService.findByAssetId(assetId);
    }

    // e2 - create sensor - POST - create()
    @PostMapping("/{assetId}/sensors")
    public ResponseEntity<SensorResponse> create(@PathVariable Long assetId, @Valid @RequestBody SensorRequest request){
        SensorResponse created = sensorService.create(assetId, request);
        return ResponseEntity.created(URI.create("/api/v1/assets/" + created.assetId() + "/sensors/" + created.id()))
        .body(created);
    }

    // e3 - decactivate sensor - PATCH - deactivate()
    @PatchMapping("/sensors/{sensorId}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long sensorId){
        sensorService.deactivate(sensorId);
        return ResponseEntity.noContent().build();
    }

}

