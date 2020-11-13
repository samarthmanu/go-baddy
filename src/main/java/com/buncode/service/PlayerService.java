package com.buncode.service;

import com.buncode.model.Player;
import com.buncode.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService implements IPlayerService {

    @Autowired
    private PlayerRepository repository;


    @Override
    public List<Player> findAll() {

        return (List<Player>) repository.findAll();
    }

   @Override
    public Player getPlayerByName(String name) {

        return repository.getPlayerByName(name);
    }

    @Override
    public Optional<Player> findById(long player_id) {

        return repository.findById(player_id);
    }

    @Override
    public Player save(Player player) {

        return repository.save(player);
    }

    /*
    @Override
    public List<Player> findAllValid() {

        return repository.findAllValid();
    }

    @Override
    public Integer getTeamChangeCountByPlayer(Player player) {
        return repository.getTeamChangeCountByPlayer(player);
    }

    @Override
    public List<Object[]> getPlayerWithHighestPlayedCount() {
        return repository.getPlayerWithHighestPlayedCount();
    }

    @Override
    public List<Object[]> getPlayerWithLowestPlayedCount() {
        return repository.getPlayerWithLowestPlayedCount();
    }

    @Override
    public List<Object[]> getPlayerWithHighestWinCount() {
        return repository.getPlayerWithHighestWinCount();
    }

    @Override
    public List<Object[]> getPlayerWithHighestLossCount() {
        return repository.getPlayerWithHighestLossCount();
    }

    @Override
    public List<Object[]> getPlayerWithHighestAttendance() {
        return repository.getPlayerWithHighestAttendance();
    }

    @Override
    public List<Object[]> getPlayerWithLowestAttendance() {
        return repository.getPlayerWithLowestAttendance();
    }

    @Override
    public List<Object[]> getBestWinningStreak() {
        return repository.getBestWinningStreak();
    }

    @Override
    public List<Object[]> getBestLosingStreak() {
        return repository.getBestLosingStreak();
    }

    @Override
    public PlayerStats getPlayerStats(Player player) {
        return repository.getPlayerStats(player.getPlayer_id());
    }*/
}