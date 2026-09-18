package com.example.asset_monitoring.exception;

import java.time.Instant;

public class IllegalRecordAtTimeException extends RuntimeException{
    public IllegalRecordAtTimeException(Instant recordedAt){
        super("The measurement recorded at time " + recordedAt + " is in future and cannot be accepted");
    }
}