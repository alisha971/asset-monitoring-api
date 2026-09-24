package com.example.asset_monitoring.service;

import java.util.List;

import com.example.asset_monitoring.repository.AlertRepository;
import com.example.asset_monitoring.repository.SensorRepository;
import com.example.asset_monitoring.domain.Alert;
import com.example.asset_monitoring.domain.AlertSeverity;
import com.example.asset_monitoring.domain.Sensor;
import com.example.asset_monitoring.dto.AlertResponse;
import com.example.asset_monitoring.exception.AlertNotFoundException;
import com.example.asset_monitoring.exception.SensorNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class AlertService{

    private final AlertRepository alertRepository;
    private final SensorRepository sensorRepository;

    public AlertService(AlertRepository alertRepository, SensorRepository sensorRepository){
        this.alertRepository = alertRepository;
        this.sensorRepository = sensorRepository;
    }

    public AlertResponse raiseOrEscalate(Sensor sensor, AlertSeverity severity){
        
        List<Alert> alerts = alertRepository.findBySensorId(sensor.getId());

        for(Alert alert : alerts){
            if(alert.isOpen()){
                alert.escalate(severity);
                return AlertResponse.from(alertRepository.save(alert));
            }
        }

        Alert alert = new Alert(sensor, severity);
        Alert saved = alertRepository.save(alert);
        return AlertResponse.from(saved);
    }

    public AlertResponse findById(Long id){
        return AlertResponse.from(getAlertOrThrow(id));
    }

    public List<AlertResponse> findForSensor(Long sensorId){
        Sensor sensor = getSensorOrThrow(sensorId);
        return alertRepository.findBySensorId(sensorId)
        .stream()
        .map(alert -> AlertResponse.from(alert))
        .toList();
    }

    public AlertResponse acknowledge(Long id){
        Alert alert = getAlertOrThrow(id);
        alert.acknowledge();
        return AlertResponse.from(alertRepository.save(alert));
    }

    public AlertResponse resolve(Long id){
        Alert alert = getAlertOrThrow(id);
        alert.resolve();
        return AlertResponse.from(alertRepository.save(alert));
    }

    Alert getAlertOrThrow(Long alertId){
        return alertRepository.findById(alertId)
                .orElseThrow(() -> new AlertNotFoundException(alertId));
    }

    Sensor getSensorOrThrow(Long sensorId){
        return sensorRepository.findById(sensorId).orElseThrow(
            () -> new SensorNotFoundException(sensorId));
    }

}