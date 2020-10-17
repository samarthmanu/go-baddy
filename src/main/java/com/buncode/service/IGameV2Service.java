package com.buncode.service;

import com.buncode.model.GameV2;
import com.buncode.model.Player;
import com.buncode.model.Team;

import java.util.List;
import java.util.Optional;

public interface IGameV2Service {

    Optional<GameV2> findById(Long game_id);

    List<GameV2> findAll();

    List<GameV2> getGamesPlayedByPlayer(Player player);

    List<GameV2> getGamesWonByPlayer(Player player);

    List<GameV2> getGamesLostByPlayer(Player player);

    List<GameV2> getGamesPlayedByTeam(Team team);

    List<GameV2> getGamesWonByTeam(Team team);

    List<GameV2> getGamesLostByTeam(Team team);

}