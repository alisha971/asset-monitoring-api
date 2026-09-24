package com.example.asset_monitoring.domain;

public enum AlertState {
    NORMAL, WARNING, CRITICAL, ACKNOWLEDGED, RESOLVED;

    public boolean isValidTransition(AlertState next){
        switch(this){
            case NORMAL:
                return (next == WARNING || next == CRITICAL);

            case WARNING:
                return (next == CRITICAL || next == ACKNOWLEDGED || next == NORMAL);

            case CRITICAL:
                return (next == ACKNOWLEDGED);

            case ACKNOWLEDGED:
                return (next == RESOLVED || next == CRITICAL);

            case RESOLVED:
                return (next == NORMAL);

            default:
                return false;
        }
    }
}

