package com.example.asset_monitoring.exception;

public class AlertNotFoundException extends RuntimeException{
    public AlertNotFoundException(Long id){
        super("Alert " + id + " doesn't exist");
    }
}