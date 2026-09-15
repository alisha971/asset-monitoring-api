package com.example.asset_monitoring.domain;

public enum AlertState {
    NORMAL, WARNING, CRITICAL, ACKNOWLEDGEMENT, RESOLVED;

    public boolean isValidTransition(AlertState next){
        switch(this){
            case NORMAL:
                return (next == WARNING || next == CRITICAL);

            case WARNING:
                return (next == CRITICAL || next == ACKNOWLEDGEMENT || next == NORMAL);

            case CRITICAL:
                return (next == ACKNOWLEDGEMENT);

            case ACKNOWLEDGEMENT:
                return (next == RESOLVED);

            case RESOLVED:
                return (next == NORMAL);

            default:
                return false;
        }
    }
}

