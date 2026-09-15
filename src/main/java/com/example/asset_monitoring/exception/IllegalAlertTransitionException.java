package com.example.asset_monitoring.exception;

import com.example.asset_monitoring.domain.AlertState;

public class IllegalAlertTransitionException extends RuntimeException{
    public IllegalAlertTransitionException(AlertState state, AlertState nextState){
        super("Cannot transition from " + state + " to " + nextState); 
    }
}

