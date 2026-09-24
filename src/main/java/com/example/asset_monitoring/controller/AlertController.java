package com.example.asset_monitoring.controller;

import com.example.asset_monitoring.service.AlertService;
import com.example.asset_monitoring.dto.AlertResponse;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/assets")
public class AlertController{

    private final AlertService alertService;
    
    public AlertController(AlertService alertService){
        this.alertService = alertService;
    }

    @GetMapping("/sensors/{sensorId}/alerts")
    public List<AlertResponse> list(@PathVariable Long sensorId){
        return alertService.findForSensor(sensorId);
    }

    @GetMapping("/sensors/{sensorId}/alerts/{alertId}")
    public AlertResponse getAlert(@PathVariable Long alertId){
        return alertService.findById(alertId);
    }

    @PatchMapping("/sensors/{sensorId}/alerts/{alertId}/acknowledge")
    public AlertResponse acknowledge(@PathVariable Long alertId){
        return alertService.acknowledge(alertId);
    }

    @PatchMapping("/sensors/{sensorId}/alerts/{alertId}/resolve")
    public AlertResponse resolve(@PathVariable Long alertId){
        return alertService.resolve(alertId);
    }
    
}