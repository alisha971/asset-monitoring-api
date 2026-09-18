package com.example.asset_monitoring.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.example.asset_monitoring.domain.AssetStatus;
import com.example.asset_monitoring.dto.AssetRequest;
import com.example.asset_monitoring.dto.AssetResponse;
import com.example.asset_monitoring.dto.MeasurementResponse;
import com.example.asset_monitoring.service.AssetService;
import com.example.asset_monitoring.service.MeasurementService;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetController{

    private final AssetService assetService;
    private final MeasurementService measurementService;

    public AssetController(AssetService assetService, MeasurementService measurementService){
        this.assetService = assetService;
        this.measurementService = measurementService;
    }

    // e1 - list assets - GET
    @GetMapping
    public List<AssetResponse> list(@RequestParam(required = false) AssetStatus status){
        return assetService.findAll(status);
    }

    // e2 - get one asset - GET
    @GetMapping("/{id}")
    public AssetResponse get(@PathVariable Long id){
        return assetService.findById(id);
    }

    // e3 - create asset - POST
    @PostMapping
    public ResponseEntity<AssetResponse> create(@Valid @RequestBody AssetRequest request){
        AssetResponse created = assetService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/assets/" + created.id()))
        .body(created);
    }

    // e4 - update asset - PUT
    @PutMapping("/{id}")
    public AssetResponse update(@PathVariable Long id, @Valid @RequestBody AssetRequest request){
        return assetService.update(id, request);
    }

    // e5 - decommission asset - PATCH
    @PatchMapping("/{id}/decommission")
    public AssetResponse decommission(@PathVariable Long id){
        assetService.decommission(id);
        return assetService.findById(id);
    }

    // e6 - delete asset - DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        assetService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // e7 - query measurements for an asset - GET
    @GetMapping("/{assetId}/measurements")
    public List<MeasurementResponse> measurements(@PathVariable Long assetId){
        return measurementService.findByAsset(assetId);
    }

}

