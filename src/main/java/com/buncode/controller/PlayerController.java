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
import java.util.ArrayList;
import java.util.List;

@Controller
public class PlayerController {

    @Autowired
    private IPlayerService playerService;

    @Autowired
    private IGameV2Service gameV2Service;

    @Autowired
    private ITeamService teamService;

    @GetMapping("/updatePlayer")
    public String getPlayer(@RequestParam(value = "id", required = false) Long player_id, Model model) {
        List<Player> players = playerService.findAll();
        model.addAttribute("players", players);

        if (players.size() == 0) {
            return "Looks like no players configured";
        }

        //player info
        Player player = player_id == null ? players.get(0) : playerService.findById(player_id).get();
        model.addAttribute("player", player);

        return "updatePlayer";
    }

    @GetMapping("/playerStats")
    public String getPlayerStats(@RequestParam(value = "id", required = false) Long player_id, Model model) {
        List<Player> players = playerService.findAll();
        model.addAttribute("players", players);

        if (players.size() == 0) {
            return "Looks like no players configured";
        }

        //player info
        Player player = player_id == null ? players.get(0) : playerService.findById(player_id).get();
        model.addAttribute("player", player);

        List<GameV2> player_games = gameV2Service.getGamesPlayedByPlayer(player);

        //player stats
        {
            int played = player_games.size();
            int won = gameV2Service.getGamesWonByPlayer(player).size();
            double ratio = CommonUtil.calcWinRatio(played, won);

            PlayerStat pStat = new PlayerStat(player, played, won, ratio);
            model.addAttribute("pStat", pStat);
        }

        //team stats
        List<Team> player_teams = teamService.getTeamsByPlayer(player);
        ArrayList teamStats = new ArrayList<TeamStat>();
        for (Team team : player_teams) {
            List<GameV2> team_games = gameV2Service.getGamesPlayedByTeam(team);

            int played = team_games.size();
            int won = gameV2Service.getGamesWonByTeam(team).size();
            double ratio = CommonUtil.calcWinRatio(played, won);

            TeamStat tStat = new TeamStat(team, played, won, ratio);
            teamStats.add(tStat);
        }
        model.addAttribute("teams", teamStats);

        //matches history
        model.addAttribute("games", player_games);

        return "showPlayerStats";
    }

    @GetMapping("/newPlayer")
    public String newPlayer(Model model) {

        return "newPlayer";
    }

    @PostMapping("/newPlayer")
    @ResponseBody
    public String newPlayer(String name, String playing_style, String signature_moves, String alias, String contact) throws YouShallNotPassException {

        //check if already existing player with same name
        Player name_taken = playerService.getPlayerByName(name);

        if (name_taken != null) {
            throw new YouShallNotPassException(
                    MessageFormat.format("Error creating player: Another player exists with same name [{0}]", name));
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());

        Player player = new Player(name, playing_style, signature_moves, alias, contact, false, now, now);
        playerService.save(player);

        String result = MessageFormat.format("<h2>Player [{0}] created successfully</h2>", player.getName());

        return result;
    }

    @PostMapping("/updatePlayer")
    @ResponseBody
    public String updatePlayer(int player_id, String name, String playing_style, String signature_moves, String alias, String contact) throws YouShallNotPassException {
        Player player = playerService.findById(player_id).get();

        //check if another player with same name
        Player name_taken = playerService.getPlayerByName(name);

        if (name_taken != null && name_taken != player) {
            throw new YouShallNotPassException(
                    MessageFormat.format("Error updating player : Another player exists with same name [{0}]", name));
        }

        synchronized (player) {
            player.setName(name);
            player.setPlaying_style(playing_style);
            player.setSignature_moves(signature_moves);
            player.setAlias(alias);
            player.setContact(contact);
            player.setUpdated_on(new Timestamp(System.currentTimeMillis()));
            playerService.save(player);
        }
        return MessageFormat.format("<h2>Player [{0}] updated successfully</h2>", player.getName());
    }

}
