package com.buncode.controller;

import com.buncode.exception.YouShallNotPassException;
import com.buncode.model.*;
import com.buncode.service.*;
import com.buncode.util.CommonUtil;
import com.buncode.util.Constants;
import com.buncode.util.CustomSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.text.TabableView;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class TeamController {

    @Autowired
    private IPlayerService playerService;

    @Autowired
    private IPointsConfigService pointsConfigService;

    @Autowired
    private ITeamService teamService;

    @Autowired
    private IGameV2Service gameV2Service;

    @Autowired
    private PlayerController playerController;

    @Autowired
    private ISeasonService seasonService;

    Long start = 0L, end = 0L;
    float diff = 0, total = 0;

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
    public String getTeamStats(@RequestParam(value = "team_id", required = false) Long team_id,
                               @RequestParam(value = "season_id", required = false) Long season_id,
                               @RequestParam(value = "fromDate", required = false) String fromDate,
                               @RequestParam(value = "toDate", required = false) String toDate,
                               Model model) throws ParseException {

        start = 0L;
        start = System.currentTimeMillis();

        List<Team> teams = teamService.findAll();
        model.addAttribute("teams", teams);
        List<Season> seasons = seasonService.findAll();
        model.addAttribute("seasons", seasons);

        if (teams.size() == 0) {
            return "Looks like no teams configured";
        }

        //team info
        Team team = team_id == null ? teams.get(0) : teamService.findById(team_id).get();
        model.addAttribute("team", team);

        List<GameV2> allGames = null;
        Season season = null;
        if(season_id==null) {
            season = seasons.get(seasons.size()-1) ;
            allGames = gameV2Service.findAllValidBySeason(season);
            model.addAttribute("season_id", season.getSeason_id());
        }else if(season_id==-1) {
            allGames = gameV2Service.findAllValid();  //all-time
            model.addAttribute("season_id", -1);
        }else if (season_id==0){
            allGames = gameV2Service.findAllValidByDateRange(
                    CommonUtil.stringToTimeStamp(fromDate + " 00:00:01"),
                    CommonUtil.stringToTimeStamp(toDate + " 23:59:59"));  //custom range
            model.addAttribute("fromDate", fromDate);
            model.addAttribute("toDate", toDate);
            model.addAttribute("season_id", 0);
        }else{
            season = seasonService.findById(season_id).get();
            allGames = gameV2Service.findAllValidBySeason(season); //season wise
            model.addAttribute("season_id", season.getSeason_id());
        }

        //match history
        List<GameV2> team_games = allGames.stream().filter(gameV2 ->
                gameV2.getTeams().contains(team)).collect(Collectors.toList());//gameV2Service.getGamesPlayedByTeam(team);
        model.addAttribute("games", team_games);//team_games.stream().limit(25).collect(Collectors.toList())); //show only last 25 games

        //teamStats
        {
            //team stats + fantasy stats
            TeamStats tStats = calculateTeamStats(team, team_games, true);

            // form stats
            if(!team_games.isEmpty()) {
                tStats.setRecentForm(team_games.stream().limit(5).
                        map(gameV2 -> {
                            return gameV2.getResult(team).substring(0, 1).toUpperCase();
                        }).collect(Collectors.joining("-")));

                String lastResult = team_games.stream().findFirst().get().getResult(team);

                int index=1;
                if(team_games.stream().
                        filter(gameV2 -> !gameV2.getResult(team).equals(lastResult)).findFirst().isPresent()) {
                    index = team_games.indexOf(
                            team_games.stream().
                                    filter(gameV2 -> !gameV2.getResult(team).equals(lastResult)).findFirst().get()); //find first non matching result
                }
                tStats.setCurrentStreak(index + lastResult.substring(0, 1).toUpperCase());  //should be like 2W or 3L);
            }else{
                tStats.setRecentForm(" <No Data> ");
                tStats.setCurrentStreak(" <No Data> ");
            }

            model.addAttribute("tStats", tStats);
        }

        //player stats
        List<PlayerStats> playerStats = new ArrayList<PlayerStats>();
        //p1 stats
        {
            Player p1 = team.getP1();

            List<GameV2> p1Games = allGames.stream().filter(gameV2 ->
                    gameV2.getPlayers().contains(p1)).collect(Collectors.toList());

            PlayerStats p1Stat = playerController.calculatePlayerStats(p1, p1Games, false);
            /*

            int played = gameV2Service.getGamesPlayedCountByPlayer(p1);
            int won = gameV2Service.getGamesWonCountByPlayer(p1);

            PlayerStats p1Stat = new PlayerStats(p1, played, won, (played-won));*/
            playerStats.add(p1Stat);
        }

        //p2 stats
        {
            Player p2 = team.getP2();

            List<GameV2> p2Games = allGames.stream().filter(gameV2 ->
                    gameV2.getPlayers().contains(p2)).collect(Collectors.toList());

            PlayerStats p2Stat = playerController.calculatePlayerStats(p2, p2Games, false);

            /*int played = gameV2Service.getGamesPlayedCountByPlayer(p2);
            int won = gameV2Service.getGamesWonCountByPlayer(p2);

            PlayerStats p2Stat = new PlayerStats(p2, played, won, (played-won));*/
            playerStats.add(p2Stat);
        }
        Collections.sort(playerStats, new CustomSort.SortPlayerStatsByWinRatio());
        model.addAttribute("playerStats", playerStats);

        end = System.currentTimeMillis();
        diff = (end - start) / 1000F;

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
            throw new YouShallNotPassException("Error creating team: Another team exists with same players combination [Team " + team_combo_taken.getName() + "]");
        }

        if (name==null || name.trim().isEmpty()){
            throw new YouShallNotPassException("Error creating team : name value cant be empty!");
        }

        //check if team already exists with this name
        Team team_name_taken = teamService.getTeamByName(name);
        if (team_name_taken != null) {
            throw new YouShallNotPassException("Error creating team: Another team exists with same name [Team " + team_name_taken.getName() + "]");
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
        if (team_combo_taken != null && team_combo_taken!=team) {
            throw new YouShallNotPassException("Error updating team: Another team exists with same players combination [Team " + team_combo_taken.getName() + "]");
        }

        if (name==null || name.trim().isEmpty()){
            throw new YouShallNotPassException("Error updating team : name value cant be empty!");
        }

        //check if team already exists with this name
        Team team_name_taken = teamService.getTeamByName(name);
        if (team_name_taken != null && team_combo_taken!=team) {
            throw new YouShallNotPassException("Error updating team: Another team exists with same name [Team " + team_name_taken.getName() + "]");
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

    public TeamStats calculateTeamStats(Team team, List<GameV2> teamGames, boolean calcFstats){

        //player fantasy points
        //List<FantasyStat> fStats = new ArrayList<FantasyStat>();
        int fTotal = 0;

        //fetch counts
        //List<GameV2> teamGames = gameV2Service.getGamesPlayedByTeam(team);
        List<Date> team_matchDays = teamGames
                .stream()
                .filter(CommonUtil.distinctByKey(GameV2::getPlayed_on))
                .map(GameV2::getPlayed_on)
                .collect(Collectors.toList());

        int attendance_count = team_matchDays.size();//gameV2Service.getMatchdayCountByTeam(team);
        int played_count = teamGames.size();//gameV2Service.getGamesPlayedCountByTeam(team);
        int won_count = teamGames.stream().filter(game -> game.getResult(team).equals("WON")).collect(Collectors.toList()).size();//gameV2Service.getGamesWonCountByTeam(team);
        int lost_count = (played_count - won_count);
        int deuce_count = teamGames.stream().filter(game -> game.getScore1()+game.getScore2()>40).collect(Collectors.toList()).size();//gameV2Service.getDeuceGamesCountByTeam(team);

        TeamStats tStats = new TeamStats(team, played_count, won_count, lost_count);

        if(!calcFstats){
            return tStats;
        }

        //fantasy points
        Map<String, FantasyStat> fMap = new LinkedHashMap<>();
        tStats.setfMap(fMap);

        //for each match day
        {
            PointsConfig pts_config = pointsConfigService.findById(Constants.POINTS_RULE_ID_FOR_MATCHDAYS_ATTENDED).get();
            int pts_total = attendance_count * pts_config.getMultiplier();
            FantasyStat fStat = new FantasyStat(pts_config.getRule(), attendance_count, pts_config.getMultiplier(), pts_total);
            fMap.put("matchday_pts", fStat);
            fTotal += pts_total;
        }

        //for each game played
        {
            PointsConfig pts_config = pointsConfigService.findById(Constants.POINTS_RULE_ID_FOR_GAMES_PLAYED).get();
            int pts_total = played_count * pts_config.getMultiplier();
            FantasyStat fStat = new FantasyStat(pts_config.getRule(), played_count, pts_config.getMultiplier(), pts_total);
            fMap.put("played_pts", fStat);
            fTotal += pts_total;
        }

        //for each game won
        {
            PointsConfig pts_config = pointsConfigService.findById(Constants.POINTS_RULE_ID_FOR_GAMES_WON).get();
            int pts_total = won_count * pts_config.getMultiplier();
            FantasyStat fStat = new FantasyStat(pts_config.getRule(), won_count, pts_config.getMultiplier(), pts_total);
            fMap.put("won_pts", fStat);
            fTotal += pts_total;
        }

        //for each loss
            /*
            {
                PointsConfig pts_config = pointsConfigService.findById(Constants.POINTS_RULE_ID_FOR_GAMES_LOST).get();
                int pts_total = lost_count * pts_config.getMultiplier();
                FantasyStat fStat = new FantasyStat(pts_config.getRule(), lost_count, pts_config.getMultiplier(), pts_total);
                fMap.put("lost_pts", fStat);
                fTotal += pts_total;
            }
            */

        //for deuce games
        {
            PointsConfig pts_config = pointsConfigService.findById(Constants.POINTS_RULE_ID_FOR_DEUCE_GAMES).get();
            int pts_total = deuce_count * pts_config.getMultiplier();
            FantasyStat fStat = new FantasyStat(pts_config.getRule(), deuce_count, pts_config.getMultiplier(), pts_total);
            fMap.put("deuce_pts", fStat);
            fTotal += pts_total;
        }

        //store last item as total points
        {
            Map points_map = new HashMap<String, Integer>();
            points_map.put("total", fTotal);
            FantasyStat fStat = new FantasyStat("", 0, 0, fTotal);
            fMap.put("total_pts", fStat);
        }

        return tStats;
    }

}
