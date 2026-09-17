package com.example.asset_monitoring.repository;

import java.util.Optional;
import java.util.List;

import com.example.asset_monitoring.domain.*;

public interface SensorRepository {

    Sensor save(Sensor sensor);
    Optional<Sensor> findById(Long id);
    Optional<Sensor> findBySerial(String serial);
    List<Sensor> findAll();
    List<Sensor> findByAssetId(Long assetId);
    void deleteById(Long id);    
}
