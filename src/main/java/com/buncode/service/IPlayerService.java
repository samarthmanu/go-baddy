package com.buncode.service;

import com.buncode.model.Player;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

public interface IPlayerService {

    @Cacheable(cacheNames = "players_cache")
    List<Player> findAll();

    @Cacheable(cacheNames = "players_cache")
    Optional<Player> findById(long player_id);

    @Cacheable(cacheNames = "players_cache")
    List<Player> findAllValid();

    @Cacheable(cacheNames = "players_cache")
    Player getPlayerByName(String name);

    @CacheEvict(value = "players_cache", allEntries = true)
    //@CachePut(cacheNames = "players_cache")
    Player save(Player player);

    /*Integer getTeamChangeCountByPlayer(Player player);

    List<Object[]> getPlayerWithHighestPlayedCount();

    List<Object[]> getPlayerWithLowestPlayedCount();

    List<Object[]> getPlayerWithHighestWinCount();

    List<Object[]> getPlayerWithHighestLossCount();

    List<Object[]> getPlayerWithHighestAttendance();

    List<Object[]> getPlayerWithLowestAttendance();

    List<Object[]> getBestWinningStreak();

    List<Object[]> getBestLosingStreak();

    PlayerStats getPlayerStats(Player player);*/
}