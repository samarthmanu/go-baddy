package com.buncode.service;

import com.buncode.model.Season;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

public interface ISeasonService {

    @Cacheable(cacheNames = "season_cache")
    List<Season> findAll();

    @Cacheable(cacheNames = "season_cache")
    Optional<Season> findById(long rule_id);

    Season getCurrentSeason();

}