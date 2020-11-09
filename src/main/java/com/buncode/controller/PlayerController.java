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

import java.sql.Date;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.buncode.util.Constants.NO_DATA;

@Controller
public class PlayerController {

    Long start = 0L, end = 0L;
    float diff = 0, total = 0;
    @Autowired
    private IPlayerService playerService;
    @Autowired
    private IGameV2Service gameV2Service;
    @Autowired
    private ITeamService teamService;
    @Autowired
    private IPointsConfigService pointsConfigService;
    @Autowired
    private ISeasonService seasonService;
    @Autowired
    private TeamController teamController;

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
    public String getPlayerStats(@RequestParam(value = "player_id", required = false) Long player_id,
                                 @RequestParam(value = "season_id", required = false) Long season_id,
                                 @RequestParam(value = "fromDate", required = false) String fromDate,
                                 @RequestParam(value = "toDate", required = false) String toDate,
                                 Model model) throws ParseException {

        start = 0L;
        start = System.currentTimeMillis();

        //fetch from cache 2nd time onwards
        List<Team> all_teams = teamService.findAll();
        List<Player> players = playerService.findAll();
        model.addAttribute("players", players);
        List<Season> seasons = seasonService.findAll();
        model.addAttribute("seasons", seasons);

        if (players.size() == 0) {
            return "Looks like no players configured";
        }

        //player info
        Player player = player_id == null ? players.get(0) : playerService.findById(player_id).get();
        model.addAttribute("player", player);

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

        //matches history
        List<GameV2> playerGames = allGames.stream().filter(gameV2 ->
                gameV2.getPlayers().contains(player)).collect(Collectors.toList());//gameV2Service.getGamesPlayedByPlayer(player);//gameV2Service.getGamesPlayedByPlayer(player);
        model.addAttribute("games", playerGames);//playerGames.stream().limit(25).collect(Collectors.toList()));

            //player stats + fantasy stats
            PlayerStats pStats = calculatePlayerStats(player, playerGames, true);

         // form stats
        if(!playerGames.isEmpty()) {
            pStats.setRecentForm(playerGames.stream().limit(5).
                    map(gameV2 -> {
                        return gameV2.getResult(player).substring(0, 1).toUpperCase();
                    }).collect(Collectors.joining("-")));

            String lastResult = playerGames.stream().findFirst().get().getResult(player);

            int index=1;
            for (;index < playerGames.size(); index++) {
                if(!playerGames.get(index).getResult(player).equals(lastResult)) break;
            }

            pStats.setCurrentStreak(index + lastResult.substring(0, 1).toUpperCase());  //should be like 2W or 3L);
        }else{
            pStats.setRecentForm(NO_DATA);
            pStats.setCurrentStreak(NO_DATA);
        }

            model.addAttribute("pStats", pStats);

            //team stats
        List<Team> player_teams = all_teams.stream().filter(team -> team.getP1().getPlayer_id()==player.getPlayer_id()
                || team.getP2().getPlayer_id()==player.getPlayer_id()).collect(Collectors.toList());//teamService.getTeamsByPlayer(player);

            List<TeamStats> teamStats = new ArrayList<TeamStats>();
            for (Team team : player_teams) {

                List<GameV2> teamGames = allGames.stream().filter(gameV2 ->
                        gameV2.getTeams().contains(team)).collect(Collectors.toList());//gameV2Service.getGamesPlayedByTeam(team);

                TeamStats tStats = teamController.calculateTeamStats(team, teamGames, false);

                teamStats.add(tStats);

            }
            Collections.sort(teamStats, new CustomSort.SortTeamStatsByWinRatio()); //sort by win ratio
            model.addAttribute("teamStats", teamStats);

            end = System.currentTimeMillis();
            diff = (end - start) / 1000F;
            return "showPlayerStats";
        }

        @GetMapping("/newPlayer")
        public String newPlayer (Model model){

            return "newPlayer";
        }

        @PostMapping("/newPlayer")
        @ResponseBody
        public String newPlayer (String name, String playing_style, String signature_moves, String alias, String contact) throws
        YouShallNotPassException {

            if (name==null || name.trim().isEmpty()){
                throw new YouShallNotPassException("Error creating player : name value cant be empty!");
            }

            //check if already existing player with same name
            Player name_taken = playerService.getPlayerByName(name);

            if (name_taken != null) {
                throw new YouShallNotPassException(
                        MessageFormat.format("Error creating player: Another player exists with same name [{0}]", name));
            }

            Timestamp now = new Timestamp(System.currentTimeMillis());

            Player player = new Player(name, playing_style, signature_moves, alias, contact, false, now, now);
            playerService.save(player);

            playerService.findAll(); //refresh players cache
            String result = MessageFormat.format("<h2>Player [{0}] created successfully</h2>", player.getName());
            return result;
        }

        @PostMapping("/updatePlayer")
        @ResponseBody
        public String updatePlayer ( int player_id, String name, String playing_style, String signature_moves, String
        alias, String contact) throws YouShallNotPassException {
            Player player = playerService.findById(player_id).get();

            if (name==null || name.trim().isEmpty()){
                throw new YouShallNotPassException("Error updating player : name value cant be empty!");
            }

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
            playerService.findAll(); //refresh players cache
            return MessageFormat.format("<h2>Player [{0}] updated successfully</h2>", player.getName());
        }

    @PostMapping("/invalidatePlayer")
    @ResponseBody
    public String invalidatePlayer(Long player_id, boolean invalidate) throws YouShallNotPassException {

        Player player = playerService.findById(player_id).get();
        Boolean newInvalidate = !invalidate; //toggle flag

        synchronized (player) {

            player.setInvalidate(newInvalidate);
            player.setUpdated_on(new Timestamp(System.currentTimeMillis()));
            playerService.save(player);
        }

        playerService.findAll(); //refresh players cache
        return(MessageFormat.format("<h2>Player [{0}] validity updated from [{1}] to [{2}] successfully</h2>", player.getName(), !invalidate, !newInvalidate));
    }

        public PlayerStats calculatePlayerStats(Player player, List<GameV2> playerGames, boolean calcFstats){

            //player fantasy points
            //List<FantasyStat> fStats = new ArrayList<FantasyStat>();
            int fTotal = 0;

            start=System.currentTimeMillis();
            //fetch counts
            /*List<GameV2> playerGames = gameV2Service.findAll().stream().filter(gameV2 ->
                    gameV2.getPlayers().contains(player.getPlayer_id())).collect(Collectors.toList());//gameV2Service.getGamesPlayedByPlayer(player);*/

            Map<String, Long> teamChangeMap = playerGames
                    .stream().collect(Collectors.groupingBy(g->g.getPlayed_on().toString() + "_playedWith_" + g.getPartner(player), Collectors.counting()));

            int attendance_count = playerGames
                    .stream()
                    .filter(CommonUtil.distinctByKey(GameV2::getPlayed_on))
                    .map(GameV2::getPlayed_on)
                    .collect(Collectors.toList()).size();//gameV2Service.getMatchdayCountByPlayer(player);

            int played_count = playerGames.size();//gameV2Service.getGamesPlayedCountByPlayer(player);

            int won_count = playerGames.stream().filter(game -> game.getResult(player).equals("WON")).collect(Collectors.toList()).size();//gameV2Service.getGamesWonCountByPlayer(player);

            int lost_count = (played_count - won_count);

            int deuce_count = playerGames.stream().filter(game -> game.getScore1()+game.getScore2()>40).collect(Collectors.toList()).size();//gameV2Service.getDeuceGamesCountByPlayer(player);

            int teamchange_count = teamChangeMap.size();//playerService.getTeamChangeCountByPlayer(player);

            end = System.currentTimeMillis();
            diff = (end - start) / 1000F;

            PlayerStats pStats = new PlayerStats(player, played_count, won_count, lost_count);

            if(!calcFstats){
                return pStats;
            }

            //fantasy points
            Map<String, FantasyStat> fMap = new LinkedHashMap<>();
            pStats.setfMap(fMap);

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

            //for every team change in a a match day
            {
                PointsConfig pts_config = pointsConfigService.findById(Constants.POINTS_RULE_ID_FOR_TEAMS_CHANGED).get();
                int pts_total = teamchange_count * pts_config.getMultiplier();
                FantasyStat fStat = new FantasyStat(pts_config.getRule(), teamchange_count, pts_config.getMultiplier(), pts_total);
                fMap.put("teamchange_pts", fStat);
                fTotal += pts_total;
            }

            //store last item as total points
            {
                Map points_map = new HashMap<String, Integer>();
                points_map.put("total", fTotal);
                FantasyStat fStat = new FantasyStat("", 0, 0, fTotal);
                fMap.put("total_pts", fStat);
            }

            return pStats;

        }

    }
