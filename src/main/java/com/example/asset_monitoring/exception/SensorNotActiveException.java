package com.example.asset_monitoring.exception;

public class SensorNotActiveException extends RuntimeException{
    public SensorNotActiveException(Long id){
        super("Sensor " + id + " not active");
    }
}