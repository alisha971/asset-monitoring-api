package com.example.asset_monitoring.repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import com.example.asset_monitoring.domain.Alert;
import com.example.asset_monitoring.domain.AlertState;

import org.springframework.stereotype.Repository;

@Repository
class InMemoryAlertRepository implements AlertRepository{

    private final AtomicLong id = new AtomicLong(0);
    private final Map<Long, Alert> store = new ConcurrentHashMap<>();
    private final Map<Long, List<Alert>> alertsBySensor = new ConcurrentHashMap<>();

    @Override 
    public Alert save(Alert alert){
        if(alert.getId() == null){
            alert.setId(id.incrementAndGet());
        }
        store.put(alert.getId(), alert);

        alertsBySensor.computeIfAbsent(
            alert.getSensor().getId(),
            k -> Collections.synchronizedList(new ArrayList<>())
        ).add(alert);

        return alert;

    }

    @Override
    public Optional<Alert> findById(Long id){
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Alert> findBySensorId(Long sensorId){
        return sorted(alertsBySensor.getOrDefault(sensorId, List.of()));
    }

    @Override
    public List<Alert> findBySensorIdAndState(Long sensorId, AlertState state){
        List<Alert> result = new ArrayList<>();
        for(Alert alert:findBySensorId(sensorId)){
            if (alert.getState() == state) result.add(alert);
        }
        return sorted(result);
    }

    private List<Alert> sorted(List<Alert> list){
        List<Alert> copy = new ArrayList<>(list);
        copy.sort(
            Comparator.comparing(Alert::raisedAt).reversed()
        );
        return Collections.unmodifiableList(copy);
    }

}