package com.example.asset_monitoring.exception;

public class DuplicateSensorSerialException extends RuntimeException{
    public DuplicateSensorSerialException(String serial){
        super("Sensor with serial " + serial + " already exists");
    }
}