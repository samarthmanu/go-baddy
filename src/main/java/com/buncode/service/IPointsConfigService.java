package com.buncode.service;

import com.buncode.model.PointsConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

public interface IPointsConfigService {

    @Cacheable(cacheNames = "points_config_cache")
    List<PointsConfig> findAll();

    @Cacheable(cacheNames = "points_config_cache")
    public Optional<PointsConfig> findById(long rule_id);

}