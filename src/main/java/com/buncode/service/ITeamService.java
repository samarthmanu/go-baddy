package com.buncode.service;

import com.buncode.model.Player;
import com.buncode.model.Team;
import com.buncode.model.TeamStats;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ITeamService {

    @Cacheable(cacheNames = "teams_cache")
    List<Team> findAll();

    @Cacheable(cacheNames = "teams_cache")
    Optional<Team> findById(long team_id);

    @Cacheable(cacheNames = "teams_cache")
    List<Team> getTeamsByPlayer(Player player);

    @Cacheable(cacheNames = "teams_cache")
    Team getTeamByPlayerCombo(Player player1, Player player2);

    @Cacheable(cacheNames = "teams_cache")
    Team getTeamByName(String name);

    @CacheEvict(value = "teams_cache", allEntries = true)
    Team save(Team team);

    @CacheEvict(value = "teams_cache", allEntries = true)
    void delete(Team team);

    /*List<Object[]> getTeamWithLowestPlayedCount();

    List<Object[]> getTeamWithHighestPlayedCount();

    List<Object[]> getTeamWithHighestWinCount();

    List<Object[]> getTeamWithHighestLossCount();

    List<Object[]> getTeamWithHighestAttendance();

    List<Object[]> getTeamWithLowestAttendance();

    List<TeamStats> getTeamStats(Player player);

    TeamStats getTeamStats(Team team);*/

}