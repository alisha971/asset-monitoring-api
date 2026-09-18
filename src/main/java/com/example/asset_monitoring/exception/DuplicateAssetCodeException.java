package com.example.asset_monitoring.exception;

public class DuplicateAssetCodeException extends RuntimeException{
    public DuplicateAssetCodeException(String code){
        super("Asset already exists with code: " + code);
    }
}