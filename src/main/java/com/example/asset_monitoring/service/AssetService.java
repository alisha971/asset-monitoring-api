package com.example.asset_monitoring.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.asset_monitoring.domain.Asset;
import com.example.asset_monitoring.domain.AssetStatus;
import com.example.asset_monitoring.dto.AssetRequest;
import com.example.asset_monitoring.dto.AssetResponse;
import com.example.asset_monitoring.repository.AssetRepository;
import com.example.asset_monitoring.exception.*;

@Service
public class AssetService{
    private final AssetRepository assetRepository;
    private static final Logger log = LoggerFactory.getLogger(AssetService.class);

    public AssetService(AssetRepository assetRepository){
        this.assetRepository = assetRepository;
    }

    // use case 1: create asset
    public AssetResponse create(AssetRequest request){
        if (assetRepository.existsByCode(request.code())){
            throw new DuplicateAssetCodeException(request.code());
        };

        Asset asset = new Asset(request.code(), request.name());

        Asset saved = assetRepository.save(asset);

        log.info("created asset {} with code {}", saved.getId(), saved.getCode());

        return AssetResponse.from(saved);
    }

    // use case 2: find one by id
    public AssetResponse findById(Long assetId){
        return AssetResponse.from(getAssetOrThrow(assetId));
    }

    // use case 3: find all assets
    public List<AssetResponse> findAll(AssetStatus status){
        return assetRepository.findAll().stream()
        .filter(a-> status == null || a.getStatus() == status)
        .map(AssetResponse::from).toList();
    }

    // use case 4: update/rename asset
    public AssetResponse update(Long id, AssetRequest request){
        Asset asset = getAssetOrThrow(id);
        asset.rename(request.name());
        return AssetResponse.from(assetRepository.save(asset));
    }

    // use case 5: decommision the asset
    public void decommission(Long id){
        Asset asset = getAssetOrThrow(id);
        asset.decommission();
        assetRepository.save(asset);
        log.info("decommissioned asset {}", id);
    }

    // use case 6: delete an asset
    public void delete(Long id){
        getAssetOrThrow(id);
        assetRepository.deleteById(id);
    }

    // throw exception if asset does not exist
    Asset getAssetOrThrow(Long id){
        return assetRepository.findById(id).orElseThrow(
            () -> new AssetNotFoundException(id));
    }
     
}