package com.example.asset_monitoring.exception;

public class AssetNotFoundException extends RuntimeException{
    public AssetNotFoundException(Long id){
        super("Asset with ID: " + id + " not found.");
    }
}