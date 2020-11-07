package com.buncode.service;

import com.buncode.model.Game;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.util.List;
import java.util.Optional;

public interface IGameService {

    List<Game> findAll();

    Optional<Game> findById(Long game_id);

    /*@Caching(evict = {
            @CacheEvict(cacheNames = "games_cache", key="#game.p1"),
            @CacheEvict(cacheNames = "games_cache", key="#game.p2")
    })*/
    @CacheEvict(value = "games_cache", allEntries = true)
    //@CachePut(cacheNames = "config_cache")
    Game save(Game game);

}