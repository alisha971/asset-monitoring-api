package com.example.asset_monitoring.repository;

import com.example.asset_monitoring.domain.*;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.List;
import java.math.BigDecimal;

// an index, time filtering, threshold filtering, and sorting

@Repository
public class InMemoryMeasurementRepository implements MeasurementRepository {
    
    private final Map<Long, Measurement> store = new ConcurrentHashMap<>();
    private final Map<Long, List<Measurement>> bySensorId = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong(0);

    @Override
    public Measurement save(Measurement m){
        if (m.getId() == null){
            Long nextId = id.incrementAndGet();
            m.setId(nextId);
        }
        store.put(m.getId(), m);

        // manage sensor id index
        bySensorId.computeIfAbsent(
            m.getSensorId(),
            k -> Collections.synchronizedList(new ArrayList<>())
        ).add(m);
        
        return m;
    };

    @Override
    public Optional<Measurement> findById(Long id){
        return Optional.ofNullable(store.get(id));
    };

    @Override
    public List<Measurement> findBySensorId(Long sensorId){
        return sorted(bySensorId.getOrDefault(sensorId, List.of()));
    };

    @Override
    public List<Measurement> findByAssetId(Long assetId){
        List<Measurement> result = new ArrayList<>();
        for(Measurement m : store.values()){
            Long aId = m.getSensor().getAsset().getId();
            if(aId.equals(assetId)) result.add(m);
        }
        return sorted(result);
    };

    @Override
    public List<Measurement> findBySensorIdAndByRecordedAtBetween(Long sensorId, Instant from, Instant to){
        List<Measurement> result = new ArrayList<>();
        for (Measurement m : bySensorId.getOrDefault(sensorId, List.of())){
            Instant t = m.getRecordedAt();
            if(!t.isBefore(from) && !t.isAfter(to)) result.add(m);
        }
        return sorted(result);  
    };

    @Override
    public List<Measurement> findAboveThreshold(Long assetId, BigDecimal threshold){
        List<Measurement> result = new ArrayList<>();
        for(Measurement m : findByAssetId((assetId))){
            if(m.getValue().compareTo(threshold) > 0) result.add(m);
        }
        return sorted(result);
    };

    @Override 
    public Optional<Measurement> findLatestBySensorId(Long sensorId){
        return bySensorId.getOrDefault(sensorId, List.of()).stream().max(Comparator.comparing(Measurement::getRecordedAt));
    };

    private List<Measurement> sorted(List<Measurement> input){
        List<Measurement> copy = new ArrayList<>(input);

        copy.sort(
            Comparator.comparing(Measurement::getRecordedAt).reversed()
        );

        return Collections.unmodifiableList(copy);
    }
}
