package com.example.asset_monitoring.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import java.net.URI;

import org.springframework.web.bind.annotation.*;

import com.example.asset_monitoring.dto.MeasurementRequest;
import com.example.asset_monitoring.dto.MeasurementResponse;
import com.example.asset_monitoring.service.MeasurementService;

@RestController
@RequestMapping("/api/v1/assets")
public class MeasurementController{

    private final MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService){
        this.measurementService = measurementService;
    }

    // e1 - list measurements for a sensor - GET - forSensor()
    @GetMapping("/sensors/{sensorId}/measurements")
    public List<MeasurementResponse> list(@PathVariable Long sensorId){
        return measurementService.findForSensor(sensorId);
    }

    // e2 - record measurement - POST - record()
    @PostMapping("/sensors/{sensorId}/measurements")
    public ResponseEntity<MeasurementResponse> record(
        @PathVariable Long sensorId,
        @Valid @RequestBody MeasurementRequest request){
            MeasurementResponse recorded = measurementService.record(sensorId, request);
            return ResponseEntity.created(URI.create("/api/v1/assets/sensors/" + recorded.sensorId() + "/measurements/" + recorded.id()))
            .body(recorded);
        }

    // e3 - latest measurements - GET - latest()
    @GetMapping("/sensors/{sensorId}/latest")
    public MeasurementResponse latest(@PathVariable Long sensorId){
        return measurementService.findLatest(sensorId);
    }

}

