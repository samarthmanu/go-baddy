package com.buncode.controller;

import com.buncode.exception.YouShallNotPassException;
import com.buncode.model.*;
import com.buncode.service.*;
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
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class GameController {

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

    @Autowired
    private ISeasonService seasonService;

    @GetMapping("/matchHist")
    public String findGames(@RequestParam(value = "season_id", required = false) Long season_id,
                            @RequestParam(value = "fromDate", required = false) String fromDate,
                            @RequestParam(value = "toDate", required = false) String toDate,
                            Model model) throws ParseException {

        long start = 0, end = 0;
        float diff = 0;
        start = System.currentTimeMillis();

        List<Season> seasons = seasonService.findAll();
        model.addAttribute("seasons", seasons);

        List<GameV2> allGames = null;
        Season season = null;
        if (season_id == null) {
            season = seasons.get(seasons.size() - 1);
            allGames = gameV2Service.findAllValidBySeason(season);
            model.addAttribute("season_id", season.getSeason_id());
        } else if (season_id == -1) {
            allGames = gameV2Service.findAllValid();  //all-time
            model.addAttribute("season_id", -1);
        } else if (season_id == 0) {
            allGames = gameV2Service.findAllValidByDateRange(
                    CommonUtil.stringToTimeStamp(fromDate + " 00:00:01"),
                    CommonUtil.stringToTimeStamp(toDate + " 23:59:59"));  //custom range
            model.addAttribute("fromDate", fromDate);
            model.addAttribute("toDate", toDate);
            model.addAttribute("season_id", 0);
        } else {
            season = seasonService.findById(season_id).get();
            allGames = gameV2Service.findAllValidBySeason(season); //season wise
            model.addAttribute("season_id", season.getSeason_id());
        }

        model.addAttribute("games", allGames);

        List<Date> matchDays = allGames
                .stream()
                .filter(CommonUtil.distinctByKey(GameV2::getPlayed_on))
                .map(GameV2::getPlayed_on)
                .collect(Collectors.toList());

        Date lastMatchday = matchDays.stream().findFirst().orElse(null);
        Date firstMatchday = matchDays.stream().skip(matchDays.size() - 1).findFirst().orElse(null);

        model.addAttribute("matchdays_first", firstMatchday!=null? firstMatchday.toString() :" <No data> ");
        model.addAttribute("matchdays_last", firstMatchday!=null? lastMatchday.toString() :" <No data> ");
        model.addAttribute("matchdays_count",matchDays.size());
        model.addAttribute("games_count",allGames.size());
        model.addAttribute("games_avg",Math.round((allGames.size()/matchDays.size()) * 100.0) / 100.0);

        end = System.currentTimeMillis();
        diff = (end - start) / 1000F;

        return "showMatchHist";
    }

    @GetMapping("/updateMatch")
    public String updateMatch(@RequestParam(value = "id", required = true) Long game_id, Model model) throws YouShallNotPassException {

        /*if (adminConfigService.getAdminConfig().isMatch_lock()) {
            throw new YouShallNotPassException("Match update/creation is currently locked. Please contact admin");
        }*/

        model.addAttribute("adminConfig", adminConfigService.getAdminConfig());

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
        Game game = gameService.findById(game_id).get();
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
        Player na_player = playerService.getPlayerByName("N/A");

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
        if (player1 != null && !player1.equals(na_player) && (player1.equals(player2) || player1.equals(player3) || player1.equals(player4))) {
            throw new YouShallNotPassException("Player [" + player1.getName() + "] used twice or more");
        } else if (player2 != null && !player2.equals(na_player) && (player2.equals(player3) || player2.equals(player4))) {
            throw new YouShallNotPassException("Player [" + player2.getName() + "] used twice or more");
        } else if (player3 != null && !player3.equals(na_player) && player3.equals(player4)) {
            throw new YouShallNotPassException("Player [" + player3.getName() + "] used twice or more");
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());

        Game game = new Game(player1, player2, player3, player4, score1, score2, false, now, now);
        gameService.save(game);

        String result = MessageFormat.format("<h2>Match [#{0}] created successfully</h2>", game.getGame_id());
        return result;

    }

    @PostMapping("/updateMatch")
    @ResponseBody
    public String updateMatch(Long game_id, int p1, int p2, int t1, int score1, int p3, int p4, int t2, int score2) throws YouShallNotPassException {
        Game game = gameService.findById(game_id).get();

        Player player1 = null, player2 = null, player3 = null, player4 = null;
        Player na_player = playerService.getPlayerByName("N/A");

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
        if (player1 != null && !player1.equals(na_player) && (player1.equals(player2) || player1.equals(player3) || player1.equals(player4))) {
            throw new YouShallNotPassException("Player [" + player1.getName() + "] used twice or more");
        } else if (player2 != null && !player2.equals(na_player) && (player2.equals(player3) || player2.equals(player4))) {
            throw new YouShallNotPassException("Player [" + player2.getName() + "] used twice or more");
        } else if (player3 != null && !player3.equals(na_player) && player3.equals(player4)) {
            throw new YouShallNotPassException("Player [" + player3.getName() + "] used twice or more");
        }

        synchronized (game) {
            game.setP1(player1);
            game.setP2(player2);
            game.setScore1(score1);
            game.setP3(player3);
            game.setP4(player4);
            game.setScore2(score2);
            game.setUpdated_on(new Timestamp(System.currentTimeMillis()));
            gameService.save(game);
        }
        gameV2Service.findAll(); //refresh games cache
        return(MessageFormat.format("<h2>Match [#{0}] updated successfully</h2>", game.getGame_id()));

    }

    @PostMapping("/invalidateMatch")
    @ResponseBody
    public String invalidateMatch(Long game_id, boolean invalidate) throws YouShallNotPassException {

        Game game = gameService.findById(game_id).get();
        Boolean newInvalidate = !invalidate; //toggle flag

        synchronized (game) {

            game.setInvalidate(newInvalidate);
            game.setUpdated_on(new Timestamp(System.currentTimeMillis()));
            gameService.save(game);
        }
        gameV2Service.findAll(); //refresh games cache
        return(MessageFormat.format("<h2>Match [#{0}] validity updated from [{1}] to [{2}] successfully</h2>", game.getGame_id(), !invalidate, !newInvalidate));
    }

}
