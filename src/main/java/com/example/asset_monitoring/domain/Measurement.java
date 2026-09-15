package com.example.asset_monitoring.domain;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.Objects;

public class Measurement {
    private Long id;
    private final Sensor sensor;
    private final BigDecimal value;
    private final String unit;
    private final Instant recordedAt;

    public Measurement(BigDecimal value, Sensor sensor, String unit, Instant recordedAt){
        this.sensor = Objects.requireNonNull(sensor);
        this.unit = (unit == null || unit.isBlank()) ? sensor.getType().defaultUnit() : unit;
        this.value = Objects.requireNonNull(value);
        this.recordedAt = (recordedAt == null) ? Instant.now() : recordedAt;
    }

    // getters and setters
    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}
    public Sensor getSensor(){return sensor;}
    public Long getSensorId(){return sensor.getId();}
    public BigDecimal getValue(){return value;}
    public String getUnit(){return unit;}
    public Instant getRecordedAt(){return recordedAt;}

    @Override public String toString(){
        return "Measurement{id: %s, sensor: %s, value: %s %s, recordedAt: %s}".formatted(id, sensor.getSerial(), value, unit, recordedAt);
    }

}
