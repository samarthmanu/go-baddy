package com.buncode.controller;

import com.buncode.exception.YouShallNotPassException;
import com.buncode.model.*;
import com.buncode.service.IGameV2Service;
import com.buncode.service.IPlayerService;
import com.buncode.service.ITeamService;
import com.buncode.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.List;

@Controller
public class TeamController {

    @Autowired
    private IPlayerService playerService;

    @Autowired
    private ITeamService teamService;

    @Autowired
    private IGameV2Service gameV2Service;

    @GetMapping("/updateTeam")
    public String getTeam(@RequestParam(value = "id", required = false) Long team_id, Model model) {
        List<Player> players = playerService.findAllValid();
        List<Team> teams = teamService.findAll();
        model.addAttribute("players", players);
        model.addAttribute("teams", teams);

        if (teams.size() == 0) {
            return "Looks like no teams configured";
        }

        //player info
        Team team = team_id == null ? teams.get(0) : teamService.findById(team_id).get();
        model.addAttribute("team", team);

        return "updateTeam";
    }

    @GetMapping("/newTeam")
    public String newTeam(@RequestParam(value = "id", required = false) Long team_id, Model model) {
        List<Player> players = playerService.findAllValid();
        model.addAttribute("players", players);

        return "newTeam";
    }

    @GetMapping("/teamStats")
    public String getTeamStats(@RequestParam(value = "id", required = false) Long team_id, Model model) {

        List<Team> teams = teamService.findAll();
        model.addAttribute("teams", teams);

        if (teams.size() == 0) {
            return "Looks like no teams configured";
        }

        //team info
        Team team = team_id == null ? teams.get(0) : teamService.findById(team_id).get();
        model.addAttribute("team", team);

        List<GameV2> team_games = gameV2Service.getGamesPlayedByTeam(team);
        //teamStats
        {
            int played = team_games.size();
            int won = gameV2Service.getGamesWonByTeam(team).size();
            double ratio = CommonUtil.calcWinRatio(played, won);

            TeamStat tStat = new TeamStat(team, played, won, ratio);
            model.addAttribute("tStat", tStat);
        }

        //p1 stats
        {
            Player p1 = team.getP1();
            List<GameV2> player_games = gameV2Service.getGamesPlayedByPlayer(p1);

            //player stats
            int played = player_games.size();
            int won = gameV2Service.getGamesWonByPlayer(p1).size();
            double ratio = CommonUtil.calcWinRatio(played, won);

            PlayerStat pStat = new PlayerStat(p1, played, won, ratio);
            model.addAttribute("p1Stat", pStat);
        }

        //p2 stats
        {
            Player p2 = team.getP2();
            List<GameV2> player_games = gameV2Service.getGamesPlayedByPlayer(p2);

            //player stats
            int played = player_games.size();
            int won = gameV2Service.getGamesWonByPlayer(p2).size();
            double ratio = CommonUtil.calcWinRatio(played, won);

            PlayerStat pStat = new PlayerStat(p2, played, won, ratio);
            model.addAttribute("p2Stat", pStat);
        }

        //match history
        model.addAttribute("games", team_games);

        return "showTeamStats";
    }

    @PostMapping("/newTeam")
    @ResponseBody
    public String newTeam(String name, int p1, int p2, String playing_style, String signature_moves, String alias) throws YouShallNotPassException {

        Player player1 = playerService.findById(p1).get();
        Player player2 = playerService.findById(p2).get();

        if (player1 == null || player2 == null) {
            throw new YouShallNotPassException("Error creating team: error fetching players");
        }

        //check if team already exists with this player combo
        Team team_combo_taken = teamService.getTeamByPlayerCombo(player1, player2);
        if (team_combo_taken != null) {
            throw new YouShallNotPassException("Error creating team: Team already exists with same players combination [Team " + team_combo_taken.getName() + "]");
        }

        //check if team already exists with this name
        Team team_name_taken = teamService.getTeamByName(name);
        if (team_name_taken != null) {
            throw new YouShallNotPassException("Error creating team: Team already exists with same name [Team " + team_name_taken.getName() + "]");
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());

        Team team = new Team(name, playing_style, signature_moves, alias, now, now, player1, player2);
        teamService.save(team);

        String result = MessageFormat.format("<h2>Team [{0}] created successfully</h2>", team.getName());
        return result;
    }

    @PostMapping("/updateTeam")
    @ResponseBody
    public String updateTeam(int team_id, String name, int p1, int p2, String playing_style, String signature_moves, String alias) throws YouShallNotPassException {
        Team team = teamService.findById(team_id).get();
        Player player1 = playerService.findById(p1).get();
        Player player2 = playerService.findById(p2).get();

        if (player1 == null || player2 == null) {
            throw new YouShallNotPassException("Error fetching players");
        }

        //check if team already exists with this player combo
        Team team_combo_taken = teamService.getTeamByPlayerCombo(player1, player2);
        if (team_combo_taken != null) {
            throw new YouShallNotPassException("Error updating team: Team already exists with same players combination [Team " + team_combo_taken.getName() + "]");
        }

        //check if team already exists with this name
        Team team_name_taken = teamService.getTeamByName(name);
        if (team_name_taken != null) {
            throw new YouShallNotPassException("Error updating team: Team already exists with same name [Team " + team_name_taken.getName() + "]");
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());

        synchronized (team) {
            team.setName(name);
            team.setP1(player1);
            team.setP2(player2);
            team.setPlaying_style(playing_style);
            team.setSignature_moves(signature_moves);
            team.setAlias(alias);
            team.setUpdated_on(new Timestamp(System.currentTimeMillis()));
            teamService.save(team);
        }

        return MessageFormat.format("<h2>Team [{0}] updated successfully</h2>", team.getName());
    }

}
