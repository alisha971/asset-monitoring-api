package com.example.asset_monitoring.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.asset_monitoring.domain.Asset;
import com.example.asset_monitoring.domain.AssetStatus;
import com.example.asset_monitoring.domain.Sensor;
import com.example.asset_monitoring.dto.SensorRequest;
import com.example.asset_monitoring.dto.SensorResponse;
import com.example.asset_monitoring.repository.AssetRepository;
import com.example.asset_monitoring.repository.SensorRepository;
import com.example.asset_monitoring.exception.*;

@Service
public class SensorService{

    private final SensorRepository sensorRepository;
    private final AssetRepository assetRepository;
    private static final Logger log = LoggerFactory.getLogger(SensorService.class);

    public SensorService(SensorRepository sensorRepository, AssetRepository assetRepository){
        this.sensorRepository = sensorRepository;
        this.assetRepository = assetRepository;
    }

    // use case 1: create sensor
    public SensorResponse create(Long assetId, SensorRequest request){
        Asset asset = getAssetOrThrow(assetId);

        if(asset.getStatus() == AssetStatus.DECOMMISSIONED){
            throw new DecommissionedAssetException(assetId);
        }
        
        if(sensorRepository.findBySerial(request.serial()).isPresent()){
            throw new DuplicateSensorSerialException(request.serial());
        }

        Sensor sensor = new Sensor(request.serial(), request.type(), request.warning(), request.critical());
        asset.addSensor(sensor);

        Sensor saved = sensorRepository.save(sensor);

        log.info("created sensor {} with serial {} for asset {}", saved.getId(), saved.getSerial(), saved.getAsset().getId());

        return SensorResponse.from(saved);
    }

    // use case 2: find by asset id
    public List<SensorResponse> findByAssetId(Long assetId){
        getAssetOrThrow(assetId);
        return sensorRepository.findByAssetId(assetId)
        .stream().map(SensorResponse::from).toList();
    }

    // use case 3: deactivate(Long sensorId)
    public void deactivate(Long sensorId){
        Sensor sensor = getSensorOrThrow(sensorId);
        sensor.deactivate();
        sensorRepository.save(sensor);
        log.info("Deactivated Sensor {}, Active Status: {}", sensor.getId(), sensor.isActive());
    }

    // use case 4: updateThresholds(Long sensorId, BigDecimal warning, BigDecimal critical)
    public SensorResponse updateThresholds(Long sensorId, BigDecimal warning, BigDecimal critical){
        Sensor sensor = getSensorOrThrow(sensorId);
        sensor.setThresholds(warning, critical);
        sensorRepository.save(sensor);
        return SensorResponse.from(sensor);
    }
    
    Sensor getSensorOrThrow(Long sensorId){
        return sensorRepository.findById(sensorId).orElseThrow(
            () -> new SensorNotFoundException(sensorId));
    }

    Asset getAssetOrThrow(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new AssetNotFoundException(id));
    }
}