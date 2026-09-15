package com.example.asset_monitoring.domain;

public enum SensorType {
    TEMPERATURE("degC"), 
    VIBRATION("mm/s"), 
    FLOW("L/min"), 
    PRESSURE("bar");

    private final String defaultUnit;

    SensorType(String defaultUnit){this.defaultUnit = defaultUnit;}
    public String defaultUnit(){return defaultUnit;}
}
