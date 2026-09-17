package com.example.asset_monitoring.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

import com.example.asset_monitoring.domain.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Map;

@Repository
public class InMemorySensorRepository implements SensorRepository{

    private final Map<Long, Sensor> store = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong(0);

    @Override
    public Sensor save(Sensor sensor){
        if(sensor.getId() == null){
            Long nextId = id.incrementAndGet();
            sensor.setId(nextId);
        }
        store.put(sensor.getId(), sensor);
        return sensor;
    };

    @Override
    public Optional<Sensor> findById(Long id){
        return Optional.ofNullable(store.get(id));
    };

    @Override
    public Optional<Sensor> findBySerial(String serial){
        if (serial == null) return Optional.empty();
        return store.values().stream().filter(sensor -> serial.equals(sensor.getSerial())).findFirst();
    };

    @Override
    public List<Sensor> findAll(){
        return List.copyOf(store.values());
    };

    @Override
    public List<Sensor> findByAssetId(Long assetId){ 
        return store.values().stream().filter(s -> assetId.equals(s.getAsset().getId())).toList();
    }

    @Override
    public void deleteById(Long id){
        store.remove(id);
    };  
    
}
