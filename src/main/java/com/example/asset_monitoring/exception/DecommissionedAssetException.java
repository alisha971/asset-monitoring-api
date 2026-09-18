package com.example.asset_monitoring.exception;

public class DecommissionedAssetException extends RuntimeException{
    public DecommissionedAssetException(Long id){
        super("Asset " + id + " already decommisioned");
    }
}