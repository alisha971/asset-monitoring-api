package com.example.asset_monitoring.repository;

import com.example.asset_monitoring.domain.*;
import java.util.Optional;
import java.util.List;

public interface AssetRepository {
    Asset save(Asset asset);
    Optional<Asset> findById(Long id);
    Optional<Asset> findByCode(String code);
    List<Asset> findAll();
    void deleteById(Long id);
    boolean existsByCode(String code);

}
