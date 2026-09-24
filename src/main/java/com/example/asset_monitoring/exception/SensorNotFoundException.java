package com.example.asset_monitoring.exception;

public class SensorNotFoundException extends RuntimeException{
    private final Long sensorId;
    public SensorNotFoundException(Long id){
        super("Sensor " + id +" not found");
        this.sensorId = id;
    }
    public Long getSensorId(){return sensorId;}
}