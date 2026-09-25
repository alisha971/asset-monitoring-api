package com.example.asset_monitoring.exception;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
    Instant timestamp,
    int status,
    String error,
    String message,
    String path,
    List<String> fieldErrors // for validation failures
) {
    public static ErrorResponse of(
        int status,
        String error,
        String message,
        String path
    ){
        return new ErrorResponse(
            Instant.now(), status, error, message, path, null
        );
    }

    public static ErrorResponse ofValidation(
        int status, 
        String error, 
        String path,
        List<String> fieldErrors
    ){
        return new ErrorResponse(
            Instant.now(), status, error, "Validation Failed", path, fieldErrors
        );
    }
}