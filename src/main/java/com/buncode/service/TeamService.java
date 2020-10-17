package com.buncode.service;

import com.buncode.model.Player;
import com.buncode.model.Team;
import com.buncode.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService implements ITeamService {

    @Autowired
    private TeamRepository repository;

    @Override
    public List<Team> findAll() {

        return (List<Team>) repository.findAll();
    }

    @Override
    public Optional<Team> findById(long team_id) {

        return repository.findById(team_id);
    }

    @Override
    public List<Team> getTeamsByPlayer(Player player) {

        return  repository.getTeamsByPlayer(player);
    }

    @Override
    public Team getTeamByPlayerCombo(Player player1, Player player2) {

        return repository.getTeamByPlayerCombo(player1, player2);
    }

    @Override
    public Team save(Team team) {

        return repository.save(team);
    }

    @Override
    public void delete(Team team) {

        repository.delete(team);
    }

    @Override
    public Team getTeamByName(String name) {

        return repository.getTeamByName(name);
    }
}