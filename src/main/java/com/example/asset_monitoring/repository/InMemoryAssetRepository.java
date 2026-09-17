package com.example.asset_monitoring.repository;

import org.springframework.stereotype.Repository;

import com.example.asset_monitoring.domain.*;
import java.util.Optional;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Map;

@Repository
public class InMemoryAssetRepository implements AssetRepository {

    private final Map<Long, Asset> store = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong(0);

    @Override
    public Asset save(Asset asset){
        if(asset.getId() == null){
            long nextId = id.incrementAndGet();
            asset.setId(nextId);
        }
        store.put(asset.getId(), asset);
        return asset;
    };

    @Override
    public Optional<Asset> findById(Long id){
        return Optional.ofNullable(store.get(id));
            
    };

    @Override
    public Optional<Asset> findByCode(String code){
        if(code == null) return Optional.empty();

        return store.values().stream().filter(asset-> code.equals(asset.getCode())).findFirst();
    };

    @Override
    public List<Asset> findAll(){
        return List.copyOf(store.values());
    };

    @Override
    public void deleteById(Long id){
        store.remove(id);
    };

    @Override
    public boolean existsByCode(String code){
        return findByCode(code).isPresent();
    };
}
