package com.example.asset_monitoring.service;

import java.time.Instant;
import java.util.List;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.asset_monitoring.repository.AssetRepository;
import com.example.asset_monitoring.repository.MeasurementRepository;
import com.example.asset_monitoring.repository.SensorRepository;
import com.example.asset_monitoring.domain.Alert;
import com.example.asset_monitoring.domain.AlertSeverity;
import com.example.asset_monitoring.domain.Measurement;
import com.example.asset_monitoring.domain.Sensor;
import com.example.asset_monitoring.domain.Asset;
import com.example.asset_monitoring.dto.MeasurementRequest;
import com.example.asset_monitoring.dto.MeasurementResponse;
import com.example.asset_monitoring.exception.*;

@Service
public class MeasurementService{

    private final AssetRepository assetRepository;
    private final SensorRepository sensorRepository;
    private final MeasurementRepository measurementRepository;

    public MeasurementService(AssetRepository assetRepository, SensorRepository sensorRepository, MeasurementRepository measurementRepository){
        this.assetRepository = assetRepository;
        this.sensorRepository = sensorRepository;
        this.measurementRepository = measurementRepository;
    }

    // use case 1: record measurement
    public MeasurementResponse record(Long sensorId, MeasurementRequest request){
        // check if sensor exists -> else sensor not foun exception
        Sensor sensor = getSensorOrThrow(sensorId);
        // check if sensor is active -> else sensor not active exception
        if(!sensor.isActive()){
            throw new SensorNotActiveException(sensorId);
        }
        // measurement in future?
        if(request.recordedAt().isAfter(Instant.now())){
            throw new IllegalRecordAtTimeException(request.recordedAt());
        }
        
        Measurement measurement = new Measurement(request.value(), sensor, request.unit(), request.recordedAt());

        Measurement saved = measurementRepository.save(measurement);

        AlertSeverity severity = sensor.severityOf(saved.getValue());

        if(!sensor.isWithinLimits(saved.getValue())){
            // check if alert already exists - code - escalate

            // if no alert exists
            // Alert alert = new Alert(saved.getSensor(), severity);
        }

        return MeasurementResponse.from(saved, severity);
    }


    // use case 2: find measurements for sensor - getBySensorId()
    public List<MeasurementResponse> findForSensor(Long sensorId){
        Sensor sensor = getSensorOrThrow(sensorId);
        return measurementRepository.findBySensorId(sensorId)
        .stream()
        .map(m -> MeasurementResponse.from(m, sensor.severityOf(m.getValue())))
        .toList();
    }

    // use case 3: find latest measurements
    public MeasurementResponse findLatest(Long sensorId){
        Sensor sensor = getSensorOrThrow(sensorId);
        Measurement m = measurementRepository.findLatestBySensorId(sensorId).orElseThrow(
            () -> new NoMeasurementsRecordedException(sensorId)
        );
        return MeasurementResponse.from(m, sensor.severityOf(m.getValue()));
    }

    // use case 4: find measurements for asset
    public List<MeasurementResponse> findByAsset(Long assetId){
        Asset asset = getAssetOrThrow(assetId);
        return measurementRepository.findByAssetId(assetId)
        .stream()
        .map(m -> MeasurementResponse.from(m, m.getSensor().severityOf(m.getValue())))
        .toList();
    }
    
    Sensor getSensorOrThrow(Long id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new SensorNotFoundException(id));
    }

    Asset getAssetOrThrow(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new AssetNotFoundException(id));
    }
}