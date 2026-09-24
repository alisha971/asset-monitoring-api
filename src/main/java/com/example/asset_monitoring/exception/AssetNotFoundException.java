package com.example.asset_monitoring.exception;

public class AssetNotFoundException extends RuntimeException{
    private final Long assetId;
    public AssetNotFoundException(Long id){
        super("Asset with ID: " + id + " not found.");
        this.assetId = id;
    }
    public Long getAssetId(){return assetId;}
}