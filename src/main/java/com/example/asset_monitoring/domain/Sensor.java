package com.example.asset_monitoring.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Sensor {

    private Long id;
    private String serial;
    private SensorType type;
    private boolean active;
    private BigDecimal warningThreshold;
    private BigDecimal criticalThreshold;
    private Asset asset;

    public Sensor(String serial, SensorType type, BigDecimal warningThreshold, BigDecimal criticalThreshold){
        this.serial = Objects.requireNonNull(serial);
        this.type = Objects.requireNonNull(type);
        this.active = true;
        setThresholds(warningThreshold, criticalThreshold);
    }

    // set the thresholds
    public void setThresholds(BigDecimal warningThreshold, BigDecimal criticalThreshold){
        // throw illegal arguement exception for warning>critical
        if(warningThreshold != null && criticalThreshold != null && warningThreshold.compareTo(criticalThreshold)>=0){
            throw new IllegalArgumentException("Warning must be below critical threshold");
        } 
        this.warningThreshold = warningThreshold;
        this.criticalThreshold = criticalThreshold;
    }
    
    // setters
    public void setId(Long Id){this.id = Id;}
    // getters
    public Long getId(){return id;}
    public SensorType getType(){return type;}
    public String getSerial(){return serial;}
    public BigDecimal getWarningThreshold(){return warningThreshold;}
    public BigDecimal getCriticalThreshold(){return criticalThreshold;}
    public Asset getAsset(){return asset;}
    public boolean isActive(){return active;}
    
    // to deactivate the sensor when asset gets decommisioned
    public void deactivate(){this.active = false;}
    // to activate the sensor
    public void activate(){this.active = true;}
    // function to set asset of the sensor
    void attachTo(Asset asset){this.asset = asset;}
    
    // to check severity of the sensor
    public AlertSeverity severityOf(BigDecimal value){
        Objects.requireNonNull(value);
        if (criticalThreshold!=null && value.compareTo(criticalThreshold)>=0){
            return AlertSeverity.CRITICAL;
        }
        if (warningThreshold!=null && value.compareTo(warningThreshold)>=0){
            return AlertSeverity.WARNING;
        } 
        return AlertSeverity.NORMAL;
    }

    // to check if sensor is within limits (i.e. is normal)
    public boolean isWithinLimits(BigDecimal value){
        return severityOf(value) == AlertSeverity.NORMAL;
    }

    // override toString, hashCode and equals inbuilt functions
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Sensor other)) return false;
        return serial.equals(other.serial);
    }

    @Override
    public int hashCode(){ return serial.hashCode();}

    @Override
    public String toString(){
        return "Sensor: {id:%s, serial: %s, type: %s, active: %s, warning: %s, critical: %s".formatted(id, serial, type, active, warningThreshold, criticalThreshold);
    }
}
