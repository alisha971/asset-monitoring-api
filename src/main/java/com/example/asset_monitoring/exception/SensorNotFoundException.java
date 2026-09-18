package com.example.asset_monitoring.exception;

public class SensorNotFoundException extends RuntimeException{
    public SensorNotFoundException(Long id){
        super("Sensor " + id +" not found");
    }
}