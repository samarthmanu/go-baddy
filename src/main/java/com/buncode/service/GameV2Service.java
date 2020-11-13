package com.buncode.service;

import com.buncode.model.GameV2;
import com.buncode.model.Season;
import com.buncode.repository.GameV2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
public class GameV2Service implements IGameV2Service {

    @Autowired
    private GameV2Repository repository;

    @Override
    public Optional<GameV2> findById(Long game_id) {
        return repository.findById(game_id);
    }

    @Override
    public List<GameV2> findAll() {
        return (List<GameV2>) repository.findAll();
    }

    @Override
    public List<GameV2> findAllBySeason(Season season) {
        return repository.findAllBySeason(season);
    }

    @Override
    public List<GameV2> findAllByDateRange(Timestamp fromDate, Timestamp toDate) throws ParseException {
        return repository.findAllByDateRange(fromDate, toDate);
    }

    /*@Override
    public List<GameV2> findAllValid() {
        return repository.findAllValid();
    }

    @Override
    public List<GameV2> findAllValidByDateRange(Timestamp fromDate, Timestamp toDate) throws ParseException {
        return repository.findAllValidByDateRange(fromDate, toDate);
    }

    @Override
    public List<GameV2> findAllValidBySeason(Season season) {
        return repository.findAllValidBySeason(season);
    }

    @Override
    public List<GameV2> getGamesPlayedByPlayer(Player player) {
        return repository.getGamesPlayedByPlayer(player);
    }

    @Override
    public Integer getGamesWonCountByPlayer(Player player) {
        return repository.getGamesWonCountByPlayer(player);
    }

    @Override
    public Integer getGamesWonCountByTeam(Team team) {
        return repository.getGamesWonCountByTeam(team);
    }

    @Override
    public Integer getMatchdayCountByPlayer(Player player) {
        return repository.getMatchdayCountByPlayer(player);
    }

    @Override
    public Integer getMatchdayCountByTeam(Team team) {
        return repository.getMatchdayCountByTeam(team);
    }

    @Override
    public Integer getDeuceGamesCountByPlayer(Player player) {
        return repository.getDeuceGamesCountByPlayer(player);
    }

    @Override
    public Integer getDeuceGamesCountByTeam(Team team) {
        return repository.getDeuceGamesCountByTeam(team);
    }

    @Override
    public Integer getGamesPlayedCountByPlayer(Player player) {

        return repository.getGamesPlayedCountByPlayer(player);
    }

    @Override
    public Integer getGamesPlayedCountByTeam(Team team) {

        return repository.getGamesPlayedCountByTeam(team);
    }

    @Override
    public List<GameV2> getGamesPlayedByTeam(Team team) {
        return repository.getGamesPlayedByTeam(team);
    }

    @Override
    public List<GameV2> getGamesWonByPlayer(Player player) {
        return repository.getGamesWonByPlayer(player);
    }

    @Override
    public List<GameV2> getGamesLostByPlayer(Player player) {
        return repository.getGamesLostByPlayer(player);
    }

    @Override
    public List<GameV2> getGamesWonByTeam(Team team) {
        return repository.getGamesWonByTeam(team);
    }

    @Override
    public List<GameV2> getGamesLostByTeam(Team team) {
        return repository.getGamesLostByTeam(team);
    }

    @Override
    public List<GameV2> getHighestScore() {
        return repository.getHighestScore();
    }*/

}