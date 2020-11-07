package com.buncode.controller;

import com.buncode.model.*;
import com.buncode.service.*;
import com.buncode.util.CommonUtil;
import com.buncode.util.CustomSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LeaderboardController {

    @Autowired
    private IPlayerService playerService;

    @Autowired
    private IGameV2Service gameV2Service;

    @Autowired
    private IPointsConfigService pointsConfigService;

    @Autowired
    private PlayerController playerController;

    @Autowired
    private TeamController teamController;

    @Autowired
    private ISeasonService seasonService;

    @Autowired
    private ITeamService teamService;

    @GetMapping("/leaderboard")
    public String getLeaderBoard(@RequestParam(value = "season_id", required = false) Long season_id,
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

        //player leaderboard
        {
            List<Player> players = playerService.findAllValid();
            List playerStats = new ArrayList<PlayerStats>();
            for (Player player : players) {

                List<GameV2> playerGames = allGames.stream().filter(gameV2 ->
                        gameV2.getPlayers().contains(player)).collect(Collectors.toList());//gameV2Service.getGamesPlayedByPlayer(player);//gameV2Service.getGamesPlayedByPlayer(player);

                /*if(playerGames.size()==0 || player.isInvalidate()){
                    continue; //skip if invalidated player / no games played
                }*/

                //player stats + fantasy stats
                PlayerStats pStats = playerController.calculatePlayerStats(player, playerGames, true);
                playerStats.add(pStats);
            }

            Collections.sort(playerStats, new CustomSort.SortPlayerStatsByWinRatio()); //sort by win ratio
            model.addAttribute("playerStats", playerStats);

            ArrayList players_pts = new ArrayList(playerStats);
            Collections.sort(players_pts, new CustomSort.SortPlayerStatsByPoints()); //sort by total points
            model.addAttribute("players_pts", players_pts);

        }

        end = System.currentTimeMillis();
        diff = (end - start) / 1000F;
        start = System.currentTimeMillis();

        //team leaderboard
        {

            List<Team> teams = teamService.findAll();
            List teamStats = new ArrayList<TeamStats>();
            for (Team team : teams) {

                List<GameV2> team_games = allGames.stream().filter(gameV2 ->
                        gameV2.getTeams().contains(team)).collect(Collectors.toList());//gameV2Service.getGamesPlayedByTeam(team);

                /*if(team_games.size()==0){
                    continue; //skip if no games played
                }*/

                //team stats + fantasy stats
                TeamStats tStats = teamController.calculateTeamStats(team, team_games, true);
                model.addAttribute("tStats", tStats);

                teamStats.add(tStats);
            }

            //Sort by win ratio descending
            Collections.sort(teamStats, new CustomSort.SortTeamStatsByWinRatio()); //sort by win ratio
            model.addAttribute("teamStats", teamStats);

            ArrayList teams_pts = new ArrayList(teamStats);
            Collections.sort(teams_pts, new CustomSort.SortTeamStatsByPoints()); //sort by total points
            model.addAttribute("teams_pts", teams_pts);

        }

        //pointsConfig
        model.addAttribute("pointsConfig", pointsConfigService.findAll());

        end = System.currentTimeMillis();
        diff = (end - start) / 1000F;

        return "showLeaderboard";
    }

}
