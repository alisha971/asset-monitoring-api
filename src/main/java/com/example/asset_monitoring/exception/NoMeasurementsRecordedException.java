package com.example.asset_monitoring.exception;

public class NoMeasurementsRecordedException extends RuntimeException{
    public NoMeasurementsRecordedException(Long id){
        super("No measurements recorded for sensor " + id);
    }
}