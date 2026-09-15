package com.example.asset_monitoring.domain;

import java.time.Instant;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class Asset {
    private Long id;
    private final String code; // should not be mutable after declaration
    private String name;
    private AssetStatus status;
    private final Instant createdAt;
    private final List<Sensor> sensors = new ArrayList<>();

    // constructor
    public Asset(String code, String name){
         this.code = requireText(code, "Code");
         this.name = requireText(name, "Name");
         this.status = AssetStatus.ACTIVE;
         this.createdAt = Instant.now(); 
    }

    // helper
    private static String requireText(String value, String field){
        if(value == null || value.isBlank()){
            throw new IllegalArgumentException(field + "cannot be blank");
        }
        return value.trim();
    }

    // getters
    public Long getId(){ return id;}
    public String getCode(){ return code;}
    public String getName(){ return name;}
    public AssetStatus getStatus(){ return status;}
    public Instant getCreatedAt(){ return createdAt;}
    public List<Sensor> getSensors(){ return List.copyOf(sensors);} //return an immutable copy of list

    // setters
    public void setId(Long Id){ this.id = Id;} //set by reporistory on save


    // add sensor to list
    public void addSensor(Sensor newSensor){
        Objects.requireNonNull(newSensor);
        if (status == AssetStatus.DECOMMISSIONED){
            throw new IllegalStateException("Cannot add sensor to decommissioned asset");
        }
        sensors.add(newSensor);
        newSensor.attachTo(this); // function to also add asset to sensor
    }

    public void decommission(){
        if(this.status == AssetStatus.DECOMMISSIONED) return;
        this.status = AssetStatus.DECOMMISSIONED;
        sensors.forEach(Sensor::deactivate);
    }

    public void putUnderMaintenance(){
        if (status == AssetStatus.DECOMMISSIONED){
            throw new IllegalStateException("Cannot put decommissioned asset to maintenance");
        }
        this.status = AssetStatus.MAINTENANCE;
    }

    public void activate(){
        if (status == AssetStatus.DECOMMISSIONED){
            throw new IllegalStateException("A decommisioned asset cannot be reactivated");
        }
        this.status = AssetStatus.ACTIVE;
    }

    public void rename(String newName){
        if (status == AssetStatus.DECOMMISSIONED){
            throw new IllegalStateException("A decommisioned asset cannot be renamed");
        }
        this.name = requireText(newName, "Name");
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Asset other)) return false;
        return code.equals(other.code);
    }

    @Override public int hashCode(){return code.hashCode();}

    @Override
    public String toString(){
        return "Asset{id = %s, code = %s, name = %s, status = %s".formatted(id, code , name, status);
    }
}

