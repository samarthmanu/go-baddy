package com.buncode.service;

import com.buncode.model.GameV2;
import com.buncode.model.Player;
import com.buncode.model.Team;
import com.buncode.repository.GameV2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<GameV2> getGamesPlayedByPlayer(Player player) {

        return repository.getGamesPlayedByPlayer(player);
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
    public List<GameV2> getGamesPlayedByTeam(Team team) {

        return repository.getGamesPlayedByTeam(team);
    }

    @Override
    public List<GameV2> getGamesWonByTeam(Team team) {

        return repository.getGamesWonByTeam(team);
    }

    @Override
    public List<GameV2> getGamesLostByTeam(Team team) {

        return repository.getGamesLostByTeam(team);
    }

}