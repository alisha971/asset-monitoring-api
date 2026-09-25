package com.example.asset_monitoring.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler{

    // 404 Not Found
    @ExceptionHandler({
        AssetNotFoundException.class,
        SensorNotFoundException.class,
        AlertNotFoundException.class,
        NoMeasurementsRecordedException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex, WebRequest request){
        return build(HttpStatus.NOT_FOUND, ex, request);
    }

    // 409 Conflict: Duplicate resources/invalid state transition
    @ExceptionHandler({
        DuplicateAssetCodeException.class,
        DuplicateSensorSerialException.class,
        IllegalAlertTransitionException.class,
        SensorNotActiveException.class,
        DecommissionedAssetException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex, WebRequest request){
        return build(HttpStatus.CONFLICT, ex, request);
    }

    // 400 Bad request: invalid input
    @ExceptionHandler({
        InvalidRecordAtTimeException.class,
        IllegalArgumentException.class,
        IllegalStateException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex, WebRequest request){
        return build(HttpStatus.BAD_REQUEST, ex, request);
    }

    // 400 Bad request - bean validation @Valid @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, WebRequest request){
        List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
        .map(
            fe -> fe.getField() + ": " + fe.getDefaultMessage()
        )
        .toList();

        ErrorResponse body = ErrorResponse.ofValidation(
            HttpStatus.BAD_REQUEST.value(), 
            HttpStatus.BAD_REQUEST.getReasonPhrase(), 
            path(request), 
            fieldErrors
        );

        return ResponseEntity.badRequest().body(body);
    }

    // 500 catch all 
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, WebRequest request){
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, Exception ex, WebRequest request){
        ErrorResponse body = ErrorResponse.of(
            status.value(),
            status.getReasonPhrase(),
            ex.getMessage(),
            path(request)
        );
        return ResponseEntity.status(status).body(body);
    }

    private String path(WebRequest request){
        String description = request.getDescription(false); // returns uri
        return description.startsWith("uri=") ? description.substring(4) : description;
    }


}