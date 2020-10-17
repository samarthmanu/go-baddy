package com.buncode.service;

import com.buncode.model.Player;
import com.buncode.model.Team;

import java.util.List;
import java.util.Optional;

public interface ITeamService {

    List<Team> findAll();

    Optional<Team> findById(long team_id);

    List<Team> getTeamsByPlayer(Player player);

    Team getTeamByPlayerCombo(Player player1, Player player2);

    Team getTeamByName(String name);

    Team save(Team team);

    void delete(Team team);

}