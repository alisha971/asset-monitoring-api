package com.example.asset_monitoring.domain;

import java.time.Instant;
import java.util.Objects;
import com.example.asset_monitoring.exception.IllegalAlertTransitionException;

public class Alert {
    private Long id;
    private final Sensor sensor;
    private AlertState state;
    private AlertSeverity severity;
    private final Instant raisedAt;
    private Instant ackAt;
    private Instant resolvedAt;

    public Alert(Sensor sensor, AlertSeverity severity){
        if(severity == AlertSeverity.NORMAL){
            throw new IllegalArgumentException("Cannot raise alert at NORMAL severity");
        }
        this.sensor = Objects.requireNonNull(sensor);
        this.severity = Objects.requireNonNull(severity);
        this.state = severity == AlertSeverity.CRITICAL ? AlertState.CRITICAL : AlertState.WARNING;
        this.raisedAt = Instant.now();
    }

    // to increase severity (if warning -> critical)
    public void escalate(AlertSeverity newSeverity){
        Objects.requireNonNull(newSeverity);
        if(!newSeverity.isAbove(this.severity)) return;
        this.severity = newSeverity;
        transitionTo(AlertState.CRITICAL);
    }

    // for state transition
    private void transitionTo(AlertState nextState){
        if(!state.isValidTransition(nextState)){
            throw new IllegalAlertTransitionException(state, nextState);
        }
        this.state = nextState;
    }

    // to change state - acknowledged
    public void acknowledge(){
        if(!isOpen()){
            throw new IllegalStateException("Cannot acknowledge resolved alert");
        }
        transitionTo(AlertState.ACKNOWLEDGED);
        ackAt = Instant.now();
    }

    // to change state - resolved
    public void resolve(){
        if(!isOpen()){
            throw new IllegalStateException("Cannot resolve already resolved alert");
        }
        transitionTo(AlertState.RESOLVED);
        resolvedAt = Instant.now();
    }

    // to check if alert is still active/open
    public boolean isOpen(){
        return state != AlertState.RESOLVED && state != AlertState.NORMAL;
    }

    // getters & setters
    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}
    public Sensor getSensor(){return sensor;}
    public AlertState getState(){return state;}
    public AlertSeverity getAlertSeverity(){return severity;}
    public Instant raisedAt(){return raisedAt;}
    public Instant ackAt(){return ackAt;}
    public Instant resolvedAt(){return resolvedAt;}

    // overridden functions
    @Override
    public boolean equals(Object o){
        if (this==o) return true;
        if(!(o instanceof Alert other)) return false;
        return id!=null && id.equals(other.id);
    }

    @Override
    public int hashCode(){
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString(){
        return "Alert{id: %s, state: %s, severity: %s, sensor: %s, raisedAt: %s}".formatted(id, state, severity, sensor, raisedAt);
    }
}
