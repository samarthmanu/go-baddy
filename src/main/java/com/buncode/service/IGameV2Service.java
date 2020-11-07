package com.buncode.service;

import com.buncode.model.GameV2;
import com.buncode.model.Season;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface IGameV2Service {

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "games_cache")
    List<GameV2> findAll();

    /*@Cacheable(cacheNames = "games_cache")
    List<GameV2> findAllBySeason(Season season);

    @Cacheable(cacheNames = "games_cache")
    List<GameV2> findAllByDateRange(Timestamp fromDate, Timestamp toDate) throws ParseException;*/

    @Cacheable(cacheNames = "games_cache")
    List<GameV2> findAllValid();

    @Cacheable(cacheNames = "games_cache")
    List<GameV2> findAllValidBySeason(Season season);

    //@Cacheable(cacheNames = "games_cache")
    List<GameV2> findAllValidByDateRange(Timestamp fromDate, Timestamp toDate) throws ParseException;

    //@Cacheable(cacheNames = "games_cache")
    @Transactional(readOnly = true)
    Optional<GameV2> findById(Long game_id);

    /*
    //@Cacheable(value = "games_cache", key = "#player.getCacheKey('_played')")
    //@Cacheable(cacheNames = "games_cache")
    @Transactional(readOnly = true)
    List<GameV2> getGamesPlayedByPlayer(Player player);

    //@Cacheable(value = "games_cache", key = "#team.getCacheKey('_played')")
    //@Cacheable(cacheNames = "games_cache")
    @Transactional(readOnly = true)
    List<GameV2> getGamesPlayedByTeam(Team team);

    //@Cacheable(value = "counts_cache", key = "#player.getCacheKey('_mCount')")
    @Transactional(readOnly = true)
    Integer getMatchdayCountByPlayer(Player player);

    //@Cacheable(value = "counts_cache", key = "#player.getCacheKey('_pCount')")
    @Transactional(readOnly = true)
    Integer getGamesPlayedCountByPlayer(Player player);

    //@Cacheable(value = "counts_cache", key = "#player.getCacheKey('_wCount')")
    @Transactional(readOnly = true)
    Integer getGamesWonCountByPlayer(Player player);

    //@Cacheable(value = "counts_cache", key = "#player.getCacheKey('_dCount')")
    @Transactional(readOnly = true)
    Integer getDeuceGamesCountByPlayer(Player player);

    //@Cacheable(value = "counts_cache", key = "#team.getCacheKey('_mCount')")
    @Transactional(readOnly = true)
    Integer getMatchdayCountByTeam(Team team);

    //@Cacheable(value = "counts_cache", key = "#team.getCacheKey('_pCount')")
    @Transactional(readOnly = true)
    Integer getGamesPlayedCountByTeam(Team team);

    //@Cacheable(value = "counts_cache", key = "#team.getCacheKey('_wCount')")
    @Transactional(readOnly = true)
    Integer getGamesWonCountByTeam(Team team);

    //@Cacheable(value = "counts_cache", key = "#team.getCacheKey('_dCount')")
    @Transactional(readOnly = true)
    Integer getDeuceGamesCountByTeam(Team team);


    List<GameV2> getGamesWonByPlayer(Player player);

    List<GameV2> getGamesLostByPlayer(Player player);

    List<GameV2> getGamesWonByTeam(Team team);

    List<GameV2> getGamesLostByTeam(Team team);

    List<GameV2> getHighestScore();

     */

}