package com.buncode.service;

import com.buncode.model.Player;
import com.buncode.model.Team;
import com.buncode.model.TeamStats;
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
    public Team getTeamByName(String name) {

        return repository.getTeamByName(name);
    }

    @Override
    public Team save(Team team) {

        return repository.save(team);
    }

    @Override
    public void delete(Team team) {

        repository.delete(team);
    }

    /*@Override
    public List<Object[]> getTeamWithLowestPlayedCount() {
        return repository.getTeamWithLowestPlayedCount();
    }

    @Override
    public List<Object[]> getTeamWithHighestPlayedCount() {
        return repository.getTeamWithHighestPlayedCount();
    }

    @Override
    public List<Object[]> getTeamWithHighestWinCount() {
        return repository.getTeamWithHighestWinCount();
    }

    @Override
    public List<Object[]> getTeamWithHighestLossCount() {
        return repository.getTeamWithHighestLossCount();
    }

    @Override
    public List<Object[]> getTeamWithHighestAttendance() {
        return repository.getTeamWithHighestAttendance();
    }

    @Override
    public List<Object[]> getTeamWithLowestAttendance() {
        return repository.getTeamWithLowestAttendance();
    }

    @Override
    public List<TeamStats> getTeamStats(Player player) {
        return repository.getTeamStatsByPlayer(player.getPlayer_id());
    }

    @Override
    public TeamStats getTeamStats(Team team) {
        return repository.getTeamStatsByTeam(team.getTeam_id());
    }*/


}