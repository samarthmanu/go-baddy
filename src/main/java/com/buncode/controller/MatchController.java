package com.buncode.controller;

import com.buncode.exception.YouShallNotPassException;
import com.buncode.model.Game;
import com.buncode.model.GameV2;
import com.buncode.model.Player;
import com.buncode.model.Team;
import com.buncode.service.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Controller
public class MatchController {

    @Autowired
    private IPlayerService playerService;

    @Autowired
    private IGameService gameService;

    @Autowired
    private IGameV2Service gameV2Service;

    @Autowired
    private ITeamService teamService;

    @Autowired
    private IAdminConfigService adminConfigService;

    @GetMapping("/matchHist")
    public String findGames(Model model) {

        List<GameV2> games = gameV2Service.findAll();
        model.addAttribute("games", games);

        return "showMatchHist";
    }

    @GetMapping("/updateMatch")
    public String updateMatch(@RequestParam(value = "id", required = true) Long game_id, Model model) throws YouShallNotPassException {

        if (adminConfigService.getAdminConfig().isMatch_lock()) {
            throw new YouShallNotPassException("Match update/creation is currently locked. Please contact admin");
        }

        List<Player> players = playerService.findAllValid();
        model.addAttribute("players", players);

        List<Team> teams = teamService.findAll();
        model.addAttribute("teams", teams);

        //need to send as list so that we can update the saved game score in front end
        List<Map> numList = new ArrayList<>();
        for (int i : IntStream.rangeClosed(0, 30).toArray()) {
            Map map = new HashMap<Integer, Integer>();
            map.put("id", i);
            map.put("value", i);

            numList.add(map);
        }
        model.addAttribute("scores", numList);

        //game info
        GameV2 game = gameV2Service.findById(game_id).get();
        model.addAttribute("game", game);

        return "updateMatch";
    }

    @GetMapping("/newMatch")
    public String newMatch(Model model) throws YouShallNotPassException {

        if (adminConfigService.getAdminConfig().isMatch_lock()) {
            throw new YouShallNotPassException("Match update/creation is currently locked. Please contact admin");
        }

        List<Player> players = playerService.findAllValid();
        List<Team> teams = teamService.findAll();
        model.addAttribute("players", players);
        model.addAttribute("teams", teams);

        return "newMatch";
    }

    @PostMapping("/newMatch")
    @ResponseBody
    public String newMatch(int p1, int p2, int t1, int score1, int p3, int p4, int t2, int score2, Model model) throws YouShallNotPassException {

        Player player1 = null, player2 = null, player3 = null, player4 = null;

        Team team1 = t1 <= 0 ? null : teamService.findById(t1).get();
        Team team2 = t2 <= 0 ? null : teamService.findById(t2).get();

        if (team1 == null) {
            player1 = p1 <= 0 ? null : playerService.findById(p1).get();
            player2 = p2 <= 0 ? null : playerService.findById(p2).get();

            team1 = teamService.getTeamByPlayerCombo(player1, player2);
        } else {
            player1 = team1.getP1();
            player2 = team1.getP2();
        }

        if (team2 == null) {
            player3 = p3 <= 0 ? null : playerService.findById(p3).get();
            player4 = p4 <= 0 ? null : playerService.findById(p4).get();

            team2 = teamService.getTeamByPlayerCombo(player3, player4);
        } else {
            player3 = team2.getP1();
            player4 = team2.getP2();
        }

        //check players
        if (player1==null){
            throw new YouShallNotPassException("Error saving match: Could not set player1");
        }else if (player2==null){
            throw new YouShallNotPassException("Error saving match: Could not set player2");
        }if (player3==null){
            throw new YouShallNotPassException("Error saving match: Could not set player3");
        }if (player4==null){
            throw new YouShallNotPassException("Error saving match: Could not fetch player4");
        }

        //check duplicates
        if (player1 != null && (player1 == player2 || player1 == player3 || player1 == player4)) {
            throw new YouShallNotPassException("Player: " + player1.getName() + " used twice or more");
        } else if (player2 != null && (player2 == player3 || player2 == player4)) {
            throw new YouShallNotPassException("Player: " + player2.getName() + " used twice or more");
        } else if (player3 != null && player3 == player4) {
            throw new YouShallNotPassException("Player: " + player3.getName() + " used twice or more");
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());

        Game game = new Game(player1, player2, player3, player4, score1, score2, false, now, now);
        gameService.save(game);

        String result = MessageFormat.format("<h2>Match [#{0}] created successfully</h2>", game.getGame_id());
        return result;

    }

    @PostMapping("/updateMatch")
    @ResponseBody
    public String updateMatch(Long game_id, int p1, int p2, int t1, int score1, int p3, int p4, int t2, int score2, boolean invalidate) throws YouShallNotPassException {
        Game game = gameService.findById(game_id).get();

        Player player1 = null, player2 = null, player3 = null, player4 = null;

        Team team1 = t1 <= 0 ? null : teamService.findById(t1).get();
        Team team2 = t2 <= 0 ? null : teamService.findById(t2).get();

        if (team1 == null) {
            player1 = p1 <= 0 ? null : playerService.findById(p1).get();
            player2 = p2 <= 0 ? null : playerService.findById(p2).get();

            team1 = teamService.getTeamByPlayerCombo(player1, player2);
        } else {
            player1 = team1.getP1();
            player2 = team1.getP2();
        }

        if (team2 == null) {
            player3 = p3 <= 0 ? null : playerService.findById(p3).get();
            player4 = p4 <= 0 ? null : playerService.findById(p4).get();

            team2 = teamService.getTeamByPlayerCombo(player3, player4);
        } else {
            player3 = team2.getP1();
            player4 = team2.getP2();
        }

        //check players
        if (player1==null){
            throw new YouShallNotPassException("Error updating match: Could not set player1");
        }else if (player2==null){
            throw new YouShallNotPassException("Error updating match: Could not set player2");
        }if (player3==null){
            throw new YouShallNotPassException("Error updating match: Could not set player3");
        }if (player4==null){
            throw new YouShallNotPassException("Error updating match: Could not fetch player4");
        }

        //check duplicates
        if (player1 != null && (player1 == player2 || player1 == player3 || player1 == player4)) {
            throw new YouShallNotPassException("Player: " + player1.getName() + " used twice or more");
        } else if (player2 != null && (player2 == player3 || player2 == player4)) {
            throw new YouShallNotPassException("Player: " + player2.getName() + " used twice or more");
        } else if (player3 != null && player3 == player4) {
            throw new YouShallNotPassException("Player: " + player3.getName() + " used twice or more");
        }

        synchronized (game) {
            game.setP1(player1);
            game.setP2(player2);
            game.setScore1(score1);
            game.setP3(player3);
            game.setP4(player4);
            game.setScore2(score2);
            game.setInvalidate(invalidate);
            game.setUpdated_on(new Timestamp(System.currentTimeMillis()));
            gameService.save(game);
        }
        String result = MessageFormat.format("<h2>Match [#{0}] updated successfully</h2>", game.getGame_id());
        return result;

    }

}
