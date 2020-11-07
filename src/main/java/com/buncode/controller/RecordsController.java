package com.buncode.controller;

import com.buncode.model.*;
import com.buncode.service.IGameV2Service;
import com.buncode.service.ISeasonService;
import com.buncode.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class RecordsController {

    public static final String NO_DATA = "&lt;No Data&gt;";

    private class GameStreak {

        private Player player;
        private Team team;
        private String result;
        private List<GameV2> gameList;

        public Team getTeam() {
            return team;
        }

        public void setTeam(Team team) {
            this.team = team;
        }

        public Player getPlayer() {
            return player;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public List<GameV2> getGameList() {
            return gameList;
        }

        public void setGameList(List<GameV2> gameList) {
            this.gameList = gameList;
        }

        public GameStreak(Player player, String result, List<GameV2> gameList) {
            this.player = player;
            this.result = result;
            this.gameList = gameList;
        }

        public GameStreak(Team team, String result, List<GameV2> gameList) {
            this.team = team;
            this.result = result;
            this.gameList = gameList;
        }
    }

    @Autowired
    private ISeasonService seasonService;

    @Autowired
    private IGameV2Service gameV2Service;

    @GetMapping("/records")
    public String showRecords(@RequestParam(value = "season_id", required = false) Long season_id,
                              @RequestParam(value = "fromDate", required = false) String fromDate,
                              @RequestParam(value = "toDate", required = false) String toDate,
                              Model model) throws ParseException {

        Long start = 0L, end = 0L;
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

        List<RecordStat> fameList = new ArrayList<>();
        List<RecordStat> shameList = new ArrayList<>();

        //for player based records
        Map<Player, Integer> playedCount_players = new HashMap<Player, Integer>();
        Map<Player, Integer> wonCount_players = new HashMap<Player, Integer>();
        Map<Player, Integer> lostCount_players = new HashMap<Player, Integer>();
        Map<Player, Integer> deuceCount_players = new HashMap<Player, Integer>();
        Map<Player, Integer> deuceCountWon_players = new HashMap<Player, Integer>();
        Map<Player, Integer> deuceCountLost_players = new HashMap<Player, Integer>();
        Map<Player, List<Date>> attendanceMap_players = new HashMap<Player, List<Date>>();
        Map<Player, List<GameStreak>> streakMap_players = new HashMap<>();

        //for team based records
        Map<Team, Integer> playedCount_teams = new HashMap<Team, Integer>();
        Map<Team, Integer> wonCount_teams = new HashMap<Team, Integer>();
        Map<Team, Integer> lostCount_teams = new HashMap<Team, Integer>();
        Map<Team, Integer> deuceCount_teams = new HashMap<Team, Integer>();
        Map<Team, Integer> deuceCountWon_teams = new HashMap<Team, Integer>();
        Map<Team, Integer> deuceCountLost_teams = new HashMap<Team, Integer>();
        Map<Team, List<Date>> attendanceMap_teams = new HashMap<Team, List<Date>>();
        Map<Team, List<GameStreak>> streakMap_teams = new HashMap<>();

        Integer biggestVictory = -1;
        List<GameV2> biggestVictory_games = new ArrayList<>();

        Integer highestScore = -1;
        List<GameV2> highestScore_games = new ArrayList<>();

        for (GameV2 game : allGames) {

            //biggestVictory
            int scoreDiff = game.getScoreDiff();
            if (scoreDiff > biggestVictory) {
                biggestVictory = scoreDiff;
                biggestVictory_games.clear();
                biggestVictory_games.add(game);
            } else if (scoreDiff == biggestVictory) {
                biggestVictory_games.add(game);
            }

            //highestTotalScore
            int scoreSum=game.getScoreSum();
            if (scoreSum> highestScore) {
                highestScore = scoreSum;
                highestScore_games.clear();
                highestScore_games.add(game);
            } else if(scoreSum == biggestVictory) {
                highestScore_games.add(game);
            }

            //playerCounts
            for (Player player : game.getPlayers()) {

                //init attendance map
                if(!attendanceMap_players.containsKey(player)){
                    attendanceMap_players.put(player, new ArrayList<Date>());
                }

                //init playerStreak map
                if(!streakMap_players.containsKey(player)){
                    List<GameStreak> streak_list = new ArrayList<>();

                    GameStreak newStreak = new GameStreak(player, "TEMP", new ArrayList<GameV2>());
                    streak_list.add(newStreak);
                    streakMap_players.put(player, streak_list);
                }

                //add distinct dates to attendance map
                if(!attendanceMap_players.get(player).contains(game.getPlayed_on())){
                    attendanceMap_players.get(player).add(game.getPlayed_on());
                }

                //add game streak
                List<GameStreak> playerStreaks = streakMap_players.get(player);
                GameStreak current_streak = playerStreaks.get(playerStreaks.size()-1);  //most recently added

                //if same result add onto same streak list
                if(current_streak.result=="TEMP"
                        || current_streak.getResult().equals(game.getResult(player))){
                    current_streak.setResult(game.getResult(player));
                    current_streak.getGameList().add(game);
                }else{
                    //if diff result start new streak list
                    GameStreak newStreak = new GameStreak(player, game.getResult(player), new ArrayList<GameV2>());
                    newStreak.getGameList().add(game);
                    streakMap_players.get(player).add(newStreak);
                }

                incrementCountPlayerMap(playedCount_players, player); //playedCount

                if (game.getScoreSum()>40){
                    incrementCountPlayerMap(deuceCount_players, player); //deuceCount
                }

                if (game.getWinners().contains(player)) {
                    incrementCountPlayerMap(wonCount_players, player); //wonCount
                    if (game.getScoreSum()>40){
                        incrementCountPlayerMap(deuceCountWon_players, player); //deuceCountWon
                    }
                } else {
                    incrementCountPlayerMap(lostCount_players, player); //lostCount
                    if (game.getScoreSum()>40){
                        incrementCountPlayerMap(deuceCountLost_players, player); //deuceCountLost
                    }
                }
            }

            //teamCounts
            for (Team team : game.getTeams()) {

                //init attendance map
                if(!attendanceMap_teams.containsKey(team)){
                    attendanceMap_teams.put(team, new ArrayList<Date>());
                }

                //add distinct dates
                if(!attendanceMap_teams.get(team).contains(game.getPlayed_on())){
                    attendanceMap_teams.get(team).add(game.getPlayed_on());
                }

                //init teamStreak map
                if(team!=null) {
                    if (!streakMap_teams.containsKey(team)) {
                        List<GameStreak> streak_list = new ArrayList<>();

                        GameStreak newStreak = new GameStreak(team, "TEMP", new ArrayList<GameV2>());
                        streak_list.add(newStreak);
                        streakMap_teams.put(team, streak_list);
                    }

                    //add game streak
                    List<GameStreak> teamStreaks = streakMap_teams.get(team);
                    GameStreak current_streak = teamStreaks.get(teamStreaks.size() - 1);  //most recently added

                    //if same result add onto same streak list
                    if (current_streak.result == "TEMP"
                            || current_streak.getResult().equals(game.getResult(team))) {
                        current_streak.setResult(game.getResult(team));
                        current_streak.getGameList().add(game);
                    } else {
                        //if diff result start new streak list
                        GameStreak newStreak = new GameStreak(team, game.getResult(team), new ArrayList<GameV2>());
                        newStreak.getGameList().add(game);
                        streakMap_teams.get(team).add(newStreak);
                    }
                }

                incrementCountTeamMap(playedCount_teams, team); //playedCount

                if (game.getScoreSum()>40){
                    incrementCountTeamMap(deuceCount_teams, team); //deuceCount
                }

                if (game.getWinningTeam() != null && game.getWinningTeam().equals(team)) {
                    incrementCountTeamMap(wonCount_teams, team); //wonCount
                    if (game.getScoreSum()>40){
                        incrementCountTeamMap(deuceCountWon_teams, team); //deuceCountWon
                    }

                } else {
                    incrementCountTeamMap(lostCount_teams, team); //lostCount
                    if (game.getScoreSum()>40){
                        incrementCountTeamMap(deuceCountLost_teams, team); //deuceCountLost
                    }
                }


            }


        } //end of games loop

        end = System.currentTimeMillis();
        diff = (end - start) / 1000F;
        System.out.println(diff);
        start = System.currentTimeMillis();

        //HALL OF FAME

        //highest total points in game
        {
            String biggestTotalDesc = NO_DATA;
            if(highestScore>0) {
                biggestTotalDesc = highestScore_games.stream().
                        map(game -> game.getScoreSum() + " pts --> " + game.getGame_idAsLink() + ": " + game.getWinningResultWithScore()).
                        collect(Collectors.joining("<br>"));
            }
            fameList.add(new RecordStat("Highest total pts (Match)", biggestTotalDesc));
        }

        //biggest win/loss
        {
            // GameV2 biggestVictory = allGames.stream().max(Comparator.comparingInt(GameV2::getScoreDiff)).orElse(null);
            String biggestWinDesc = NO_DATA;
            String biggestLossDesc = NO_DATA;
            if(biggestVictory>0) {
                biggestWinDesc = biggestVictory_games.stream().
                        map(game -> game.getScoreDiff() + " pts --> " + game.getGame_idAsLink() + ": " + game.getWinningResultWithScore()).
                        collect(Collectors.joining("<br>"));
                biggestLossDesc = biggestVictory_games.stream().
                        map(game -> game.getScoreDiff() + " pts --> " + game.getGame_idAsLink() + ": " + game.getLosingResultWithScore()).
                        collect(Collectors.joining("<br>"));
            }
            fameList.add(new RecordStat("Biggest Win (Match)", biggestWinDesc));
            shameList.add(new RecordStat("Biggest Defeat (Match)", biggestLossDesc));
        }

        //most attendance - player
        {
            String playerMostAttendanceDesc = NO_DATA;
            if(attendanceMap_players.size()>0) {
                Map<Player, Integer> attendanceCount_players = attendanceMap_players.entrySet().stream().collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue().size()));

                Integer maxAttendance = attendanceCount_players.get(
                        playedCount_players.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey());

                List<Player> playersMostAttendance = attendanceCount_players.entrySet().stream()
                        .filter(e -> e.getValue() == maxAttendance)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                playerMostAttendanceDesc = playersMostAttendance.stream().
                        map(player -> maxAttendance + " match days --> " + player.getNameAsLink())
                        .collect(Collectors.joining("<br>"));
            }
            fameList.add(new RecordStat("Most attendance (Player)", playerMostAttendanceDesc));
        }

        //most attendance - team
        {
            String teamMostAttendanceDesc = NO_DATA;
            if(attendanceMap_teams.size()>0) {
                Map<Team, Integer> attendanceCount_teams = attendanceMap_teams.entrySet().stream().collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue().size()));

                Integer maxAttendance = attendanceCount_teams.get(
                        attendanceCount_teams.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey());

                List<Team> teamsMostAttendance = attendanceCount_teams.entrySet().stream()
                        .filter(e -> e.getValue() == maxAttendance)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                teamMostAttendanceDesc = teamsMostAttendance.stream().
                        map(team -> maxAttendance + " match days --> " + team.getNameAsLink())
                        .collect(Collectors.joining("<br>"));
            }
            fameList.add(new RecordStat("Most attendance (Team)", teamMostAttendanceDesc));
        }

        //most played - player
        {
            String playerMostPlayedDesc = NO_DATA;
            if(playedCount_players.size()>0) {
                Integer maxPlayed = playedCount_players.get(
                        playedCount_players.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey());

                List<Player> playersMostPlayed = playedCount_players.entrySet().stream()
                        .filter(e -> e.getValue() == maxPlayed)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                playerMostPlayedDesc = playersMostPlayed.stream().
                        map(player -> maxPlayed + " games --> " + player.getNameAsLink())
                        .collect(Collectors.joining("<br>"));
            }
            fameList.add(new RecordStat("Most games played (Player)", playerMostPlayedDesc));
        }

        //most played - team
        {
            String teamMostPlayedDesc = NO_DATA;
            if(playedCount_teams.size()>0) {
                Integer maxPlayed = playedCount_teams.get(
                        playedCount_teams.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey());

                List<Team> teamsMostPlayed = playedCount_teams.entrySet().stream()
                        .filter(e -> e.getValue() == maxPlayed)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                teamMostPlayedDesc = teamsMostPlayed.stream().
                        map(team -> maxPlayed + " games --> " + team.getNameAsLink())
                        .collect(Collectors.joining("<br>"));
            }
            fameList.add(new RecordStat("Most games played (Team)", teamMostPlayedDesc));
        }

        //most won - player
        {
            String playerMostWonDesc = NO_DATA;
            if(wonCount_players.size()>0) {
                Integer maxWon = wonCount_players.get(
                        wonCount_players.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey());

                List<Player> playersMostWon = wonCount_players.entrySet().stream()
                        .filter(e -> e.getValue() == maxWon)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                playerMostWonDesc = playersMostWon.stream().
                        map(player -> maxWon + " games --> " + player.getNameAsLink())
                        .collect(Collectors.joining("<br>"));
            }
            fameList.add(new RecordStat("Most games won (Player)", playerMostWonDesc));
        }

        //most won - team
        {
            String teamMostWonDesc = NO_DATA;
            if(wonCount_teams.size()>0) {
                Integer maxWon = wonCount_teams.get(
                        wonCount_teams.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey());

                List<Team> teamsMostWon = wonCount_teams.entrySet().stream()
                        .filter(e -> e.getValue() == maxWon)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                teamMostWonDesc = teamsMostWon.stream().
                        map(team -> maxWon + " games --> " + team.getNameAsLink())
                        .collect(Collectors.joining("<br>"));
            }
            fameList.add(new RecordStat("Most games won (Team)", teamMostWonDesc));
        }

        //most deuces - player
        {
            String playerMostDeuceDesc = NO_DATA;
            if(deuceCount_players.size()>0) {
                Integer maxDeuce = deuceCount_players.get(
                        deuceCount_players.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey());

                List<Player> playersMostDeuce = deuceCount_players.entrySet().stream()
                        .filter(e -> e.getValue() == maxDeuce)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                playerMostDeuceDesc = playersMostDeuce.stream().
                        map(player -> maxDeuce + " games --> " + player.getNameAsLink())
                        .collect(Collectors.joining("<br>"));
            }
            fameList.add(new RecordStat("Most deuce games (Player)", playerMostDeuceDesc));
        }

        //most deuces - team
        {
            String teamMostDeuceDesc = NO_DATA;
            if(deuceCount_teams.size()>0) {
                Integer maxDeuce = deuceCount_teams.get(
                        deuceCount_teams.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey());

                List<Team> teamsMostDeuce = deuceCount_teams.entrySet().stream()
                        .filter(e -> e.getValue() == maxDeuce)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                teamMostDeuceDesc = teamsMostDeuce.stream().
                        map(team -> maxDeuce + " games --> " + team.getNameAsLink())
                        .collect(Collectors.joining("<br>"));
            }
            fameList.add(new RecordStat("Most deuce games (Team)", teamMostDeuceDesc));
        }

        //most deuces won - player
        {
            String playerMostDeuceWonDesc = NO_DATA;
            if(deuceCountWon_players.size()>0) {
                Integer maxDeuceWon = deuceCountWon_players.get(
                        deuceCountWon_players.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey());

                List<Player> playersMostDeuceWon = deuceCountWon_players.entrySet().stream()
                        .filter(e -> e.getValue() == maxDeuceWon)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                playerMostDeuceWonDesc = playersMostDeuceWon.stream().
                        map(player -> maxDeuceWon + " games --> " + player.getNameAsLink())
                        .collect(Collectors.joining("<br>"));
            }
            fameList.add(new RecordStat("Most deuce games won (Player)", playerMostDeuceWonDesc));
        }

        //most deuces won - team
        {
            String teamMostDeuceWonDesc = NO_DATA;
            if(deuceCountWon_teams.size()>0) {
                Integer maxDeuceWon = deuceCountWon_teams.get(
                        deuceCountWon_teams.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey());

                List<Team> teamsMostDeuceWon = deuceCountWon_teams.entrySet().stream()
                        .filter(e -> e.getValue() == maxDeuceWon)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                teamMostDeuceWonDesc = teamsMostDeuceWon.stream().
                        map(team -> maxDeuceWon + " games --> " + team.getNameAsLink())
                        .collect(Collectors.joining("<br>"));
            }
            fameList.add(new RecordStat("Most deuce games won (Team)", teamMostDeuceWonDesc));
        }

        //longest winning streak - player
        {
            String playerLongestWinStreakDesc = NO_DATA;
            if(streakMap_players.size()>0) {

                Map<Player, List<GameStreak>> streakMapOnlyWins_players = streakMap_players.entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> filterStreaksByResult("WON", e.getValue())));

                int longestWinStreakCount=0;
                List<GameStreak> longestWinStreaks = new ArrayList<>();
                for(List<GameStreak> streaks : streakMapOnlyWins_players.values()){
                    for(GameStreak streak : streaks){
                        int currentSteakCount = streak.getGameList().size();
                        if (currentSteakCount>0 && currentSteakCount>longestWinStreakCount){
                            longestWinStreaks.clear(); //we have a new highest
                            longestWinStreakCount=streak.getGameList().size();
                            longestWinStreaks.add(streak);
                        }else if(streak.getGameList().size()>longestWinStreakCount){
                            longestWinStreakCount=streak.getGameList().size();
                            longestWinStreaks.add(streak); //we have a combined topper
                        }
                    }
                }

                playerLongestWinStreakDesc = longestWinStreaks.stream().
                        map(gameStreak -> {
                            int StreakCnt = gameStreak.getGameList().size();
                            GameV2 startStreak = gameStreak.getGameList().get(StreakCnt-1);
                            GameV2 endStreak = gameStreak.getGameList().get(0);

                            return gameStreak.getGameList().size() + " games --> " + gameStreak.player.getNameAsLink() + ": "
                                    + startStreak.getGame_idAsLink() + " (" + startStreak.getPlayedOn_IST() + ") to "
                                    + endStreak.getGame_idAsLink() + " (" + endStreak.getPlayedOn_IST() + ")";
                        })
                        .collect(Collectors.joining("<br>"));
            }
            fameList.add(new RecordStat("Longest winning streak (Player)", playerLongestWinStreakDesc));
        }

        //longest winning streak - team
        {
            String teamLongestWinStreakDesc = NO_DATA;
            if(streakMap_teams.size()>0) {

                Map<Team, List<GameStreak>> streakMapOnlyWins_teams = streakMap_teams.entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> filterStreaksByResult("WON", e.getValue())));

                int longestWinStreakCount=0;
                List<GameStreak> longestWinStreaks = new ArrayList<>();
                for(List<GameStreak> streaks : streakMapOnlyWins_teams.values()){
                    for(GameStreak streak : streaks){
                        int currentSteakCount = streak.getGameList().size();
                        if (currentSteakCount>0 && currentSteakCount>longestWinStreakCount){
                            longestWinStreaks.clear(); //we have a new highest
                            longestWinStreakCount=streak.getGameList().size();
                            longestWinStreaks.add(streak);
                        }else if(streak.getGameList().size()>longestWinStreakCount){
                            longestWinStreakCount=streak.getGameList().size();
                            longestWinStreaks.add(streak); //we have a combined topper
                        }
                    }
                }

                teamLongestWinStreakDesc = longestWinStreaks.stream().
                        map(gameStreak -> {
                            int StreakCnt = gameStreak.getGameList().size();
                            GameV2 startStreak = gameStreak.getGameList().get(StreakCnt-1);
                            GameV2 endStreak = gameStreak.getGameList().get(0);

                            return gameStreak.getGameList().size() + " games --> " + gameStreak.team.getNameAsLink() + ": "
                                    + startStreak.getGame_idAsLink() + " (" + startStreak.getPlayedOn_IST() + ") to "
                                    + endStreak.getGame_idAsLink() + " (" + endStreak.getPlayedOn_IST() + ")";
                        })
                        .collect(Collectors.joining("<br>"));
            }
            fameList.add(new RecordStat("Longest winning streak (Team)", teamLongestWinStreakDesc));
        }

        //HALL OF SHAME

        //most losses - player
        {
            String playerMostLostDesc = NO_DATA;
            if(lostCount_players.size()>0) {
                Integer maxLost = lostCount_players.get(
                        lostCount_players.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey());

                List<Player> playersMostLost = lostCount_players.entrySet().stream()
                        .filter(e -> e.getValue() == maxLost)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                playerMostLostDesc = playersMostLost.stream().
                        map(player -> maxLost + " games --> " + player.getNameAsLink())
                        .collect(Collectors.joining("<br>"));
            }
            shameList.add(new RecordStat("Most games lost (Player)", playerMostLostDesc));
        }

        //most losses - team
        {
            String teamMostLostDesc = NO_DATA;
            if(lostCount_teams.size()>0) {
                Integer maxLost = lostCount_teams.get(
                        lostCount_teams.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey());

                List<Team> teamsMostLost = lostCount_teams.entrySet().stream()
                        .filter(e -> e.getValue() == maxLost)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                teamMostLostDesc = teamsMostLost.stream().
                        map(team -> maxLost + " games --> " + team.getNameAsLink())
                        .collect(Collectors.joining("<br>"));
            }
            shameList.add(new RecordStat("Most games lost (Team)", teamMostLostDesc));
        }

        //most deuces lost - player
        {
            String playerMostDeuceLostDesc = NO_DATA;
            if(deuceCount_players.size()>0) {
                Integer maxDeuceLost = deuceCount_players.get(
                        deuceCount_players.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey());

                List<Player> playersMostDeuceLost = deuceCount_players.entrySet().stream()
                        .filter(e -> e.getValue() == maxDeuceLost)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                playerMostDeuceLostDesc = playersMostDeuceLost.stream().
                        map(player -> maxDeuceLost + " games --> " + player.getNameAsLink())
                        .collect(Collectors.joining("<br>"));
            }
            shameList.add(new RecordStat("Most deuce games lost (Player)", playerMostDeuceLostDesc));
        }

        //most deuces lost - team
        {
            String teamMostDeuceLostDesc = NO_DATA;
            if(deuceCountLost_teams.size()>0) {
                Integer maxDeuceLost = deuceCountLost_teams.get(
                        deuceCountLost_teams.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey());

                List<Team> teamsMostDeuceLost = deuceCountLost_teams.entrySet().stream()
                        .filter(e -> e.getValue() == maxDeuceLost)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                teamMostDeuceLostDesc = teamsMostDeuceLost.stream().
                        map(team -> maxDeuceLost + " games --> " + team.getNameAsLink())
                        .collect(Collectors.joining("<br>"));
            }
            shameList.add(new RecordStat("Most deuce games lost (Team)", teamMostDeuceLostDesc));
        }

        //least attendance - player
        {
            String playerLeastAttendanceDesc = NO_DATA;
            if(attendanceMap_players.size()>0) {
                Map<Player, Integer> attendanceCount_players = attendanceMap_players.entrySet().stream().collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue().size()));

                Integer minAttendance = attendanceCount_players.get(
                        playedCount_players.entrySet().stream().min(Map.Entry.comparingByValue()).get().getKey());

                List<Player> playersLeastAttendance = attendanceCount_players.entrySet().stream()
                        .filter(e -> e.getValue() == minAttendance)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                playerLeastAttendanceDesc = playersLeastAttendance.stream().
                        map(player -> minAttendance + " match days --> " + player.getNameAsLink())
                        .collect(Collectors.joining("<br>"));
            }
            shameList.add(new RecordStat("Least attendance (Player)", playerLeastAttendanceDesc));
        }

        //least attendance - team
        {
            String teamLeastAttendanceDesc = NO_DATA;
            if(attendanceMap_teams.size()>0) {
                Map<Team, Integer> attendanceCount_teams = attendanceMap_teams.entrySet().stream().collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue().size()));

                Integer minAttendance = attendanceCount_teams.get(
                        attendanceCount_teams.entrySet().stream().min(Map.Entry.comparingByValue()).get().getKey());

                List<Team> teamsLeastAttendance = attendanceCount_teams.entrySet().stream()
                        .filter(e -> e.getValue() == minAttendance)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                teamLeastAttendanceDesc = teamsLeastAttendance.stream().
                        map(team -> minAttendance + " match days --> " + team.getNameAsLink())
                        .collect(Collectors.joining("<br>"));
            }
            shameList.add(new RecordStat("Least attendance (Team)", teamLeastAttendanceDesc));
        }

        //longest losing streak - player
        {
            String playerLongestLostStreakDesc = NO_DATA;
            if(streakMap_players.size()>0) {

                Map<Player, List<GameStreak>> streakMapOnlyLost_players = streakMap_players.entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> filterStreaksByResult("LOST", e.getValue())));

                int longestLostStreakCount=0;
                List<GameStreak> longestLostStreaks = new ArrayList<>();
                for(List<GameStreak> streaks : streakMapOnlyLost_players.values()){
                    for(GameStreak streak : streaks){
                        int currentSteakCount = streak.getGameList().size();
                        if (currentSteakCount>0 && currentSteakCount>longestLostStreakCount){
                            longestLostStreaks.clear(); //we have a new highest
                            longestLostStreakCount=streak.getGameList().size();
                            longestLostStreaks.add(streak);
                        }else if(streak.getGameList().size()>longestLostStreakCount){
                            longestLostStreakCount=streak.getGameList().size();
                            longestLostStreaks.add(streak); //we have a combined topper
                        }
                    }
                }

                playerLongestLostStreakDesc = longestLostStreaks.stream().
                        map(gameStreak -> {
                            int StreakCnt = gameStreak.getGameList().size();
                            GameV2 startStreak = gameStreak.getGameList().get(StreakCnt-1);
                            GameV2 endStreak = gameStreak.getGameList().get(0);

                            return gameStreak.getGameList().size() + " games --> " + gameStreak.player.getNameAsLink() + ": "
                                    + startStreak.getGame_idAsLink() + " (" + startStreak.getPlayedOn_IST() + ") to "
                                    + endStreak.getGame_idAsLink() + " (" + endStreak.getPlayedOn_IST() + ")";
                        })
                        .collect(Collectors.joining("<br>"));
            }
            shameList.add(new RecordStat("Longest losing streak (Player)", playerLongestLostStreakDesc));
        }

        //longest losing streak - team
        {
            String teamLongestLostStreakDesc = NO_DATA;
            if(streakMap_teams.size()>0) {

                Map<Team, List<GameStreak>> streakMapOnlyLost_teams = streakMap_teams.entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> filterStreaksByResult("WON", e.getValue())));

                int longestLostStreakCount=0;
                List<GameStreak> longestLosingStreaks = new ArrayList<>();
                for(List<GameStreak> streaks : streakMapOnlyLost_teams.values()){
                    for(GameStreak streak : streaks){
                        int currentSteakCount = streak.getGameList().size();
                        if (currentSteakCount>0 && currentSteakCount>longestLostStreakCount){
                            longestLosingStreaks.clear(); //we have a new highest
                            longestLostStreakCount=streak.getGameList().size();
                            longestLosingStreaks.add(streak);
                        }else if(streak.getGameList().size()>longestLostStreakCount){
                            longestLostStreakCount=streak.getGameList().size();
                            longestLosingStreaks.add(streak); //we have a combined topper
                        }
                    }
                }

                teamLongestLostStreakDesc = longestLosingStreaks.stream().
                        map(gameStreak -> {
                            int StreakCnt = gameStreak.getGameList().size();
                            GameV2 startStreak = gameStreak.getGameList().get(StreakCnt-1);
                            GameV2 endStreak = gameStreak.getGameList().get(0);

                            return gameStreak.getGameList().size() + " games --> " + gameStreak.team.getNameAsLink() + ": "
                                    + startStreak.getGame_idAsLink() + " (" + startStreak.getPlayedOn_IST() + ") to "
                                    + endStreak.getGame_idAsLink() + " (" + endStreak.getPlayedOn_IST() + ")";
                        })
                        .collect(Collectors.joining("<br>"));
            }
            shameList.add(new RecordStat("Longest losing streak (Team)", teamLongestLostStreakDesc));
        }

        //final
        model.addAttribute("fameRecords", fameList);
        model.addAttribute("shameRecords", shameList);

        end = System.currentTimeMillis();
        diff = (end - start) / 1000F;
        System.out.println(diff);

        return "showRecords";
    }

    private static int sizeOf(List<GameStreak> list) {
        return list.size();
    }

    private static List<GameStreak> filterStreaksByResult(String result, List<GameStreak> gameList) {

        return gameList.stream().filter(gameStreak -> gameStreak.getResult()==result).collect(Collectors.toList());
    }


    public static void incrementCountPlayerMap(Map<Player, Integer> map, Player key) {
        if (map.containsKey(key)) {
            Integer count = map.get(key);
            map.put(key, count+1);
        } else {
            map.put(key, 1);
        }
    }

    public static void incrementCountTeamMap(Map<Team, Integer> map, Team key) {
        if (map.containsKey(key)) {
            Integer count = map.get(key);
            map.put(key, count+1);
        } else {
            map.put(key, 1);
        }
    }

        /*@GetMapping("/records")
    public String showRecords(Model model) {

List<GameV2> highestScore = gameV2Service.getHighestScore();
        Long start=0L, end=0L;
        float diff=0;

        start = System.currentTimeMillis();

        {
            //hall of fame
            List<RecordStat> fameList = new ArrayList<>();

            //biggest win
            String biggestWinDesc = highestScore.stream().
                    map(game -> Math.abs(game.getScore1() - game.getScore2()) + " pts --> " + game.getGame_idAsLink() + ": " + game.getWinningResultWithScore()).
                    collect(Collectors.joining("<br>"));
            fameList.add(new RecordStat("Biggest Win", biggestWinDesc));

            //most played (player)
            List<Object[]> playerMostPlayed = playerService.getPlayerWithHighestPlayedCount();
            String playerMostPlayedDesc = playerMostPlayed.stream().
                    map(row -> {
                        Player p = playerService.findById(Long.parseLong(row[0].toString())).get();
                        BigInteger playedCnt = (BigInteger) row[1];
                        return playedCnt + " games --> " + p.getNameAsLink();
                    }).collect(Collectors.joining("<br>"));
            fameList.add(new RecordStat("Most games played (Player)", playerMostPlayedDesc));

            //most played (team)
            List<Object[]> teamMostPlayed = teamService.getTeamWithHighestPlayedCount();
            String teamMostPlayedDesc = teamMostPlayed.stream().
                    map(row -> {
                        Team t = teamService.findById(Long.parseLong(row[0].toString())).get();
                        BigInteger playedCnt = (BigInteger) row[1];
                        return playedCnt + " games --> " + t.getNameAsLink();
                    }).collect(Collectors.joining("<br>"));
            fameList.add(new RecordStat("Most games played (Team)", teamMostPlayedDesc));

            //most won (player)
            List<Object[]> playerMostWon = playerService.getPlayerWithHighestWinCount();
            String playerMostWonDesc = playerMostWon.stream().
                    map(row -> {
                        Player p = playerService.findById(Long.parseLong(row[0].toString())).get();
                        BigInteger wonCnt = (BigInteger) row[1];
                        return wonCnt + " games --> " + p.getNameAsLink();
                    }).collect(Collectors.joining("<br>"));
            fameList.add(new RecordStat("Most games won (Player)", playerMostWonDesc));

            //most won (team)
            List<Object[]> teamMostWon = teamService.getTeamWithHighestWinCount();
            String teamMostWonDesc = teamMostWon.stream().
                    map(row -> {
                        Team t = teamService.findById(Long.parseLong(row[0].toString())).get();
                        BigInteger wonCnt = (BigInteger) row[1];
                        return wonCnt + " games --> " + t.getNameAsLink();
                    }).collect(Collectors.joining("<br>"));
            fameList.add(new RecordStat("Most games won (Team)", teamMostWonDesc));

            //most attendance - player
            List<Object[]> playerMostAttendance = playerService.getPlayerWithHighestAttendance();
            String playerMostAttendanceDesc = playerMostAttendance.stream().
                    map(row -> {
                        Player p = playerService.findById(Long.parseLong(row[0].toString())).get();
                        BigInteger matchDayCnt = (BigInteger) row[1];
                        return matchDayCnt + " matchdays --> " + p.getNameAsLink();
                    }).collect(Collectors.joining("<br>"));
            fameList.add(new RecordStat("Most attendance (Player)", playerMostAttendanceDesc));

            //most attendance - team
            List<Object[]> teamMostAttendance = teamService.getTeamWithHighestAttendance();
            String teamMostAttendanceDesc = teamMostAttendance.stream().
                    map(row -> {
                        Team t = teamService.findById(Long.parseLong(row[0].toString())).get();
                        BigInteger matchDayCnt = (BigInteger) row[1];
                        return matchDayCnt + " matchdays --> " + t.getNameAsLink();
                    }).collect(Collectors.joining("<br>"));
            fameList.add(new RecordStat("Most attendance (Team)", teamMostAttendanceDesc));

            //best winning streak - player
            List<Object[]> playerBestWinningStreak = playerService.getBestWinningStreak();
            String playerBestWinningStreakDesc = playerBestWinningStreak.stream().
                    map(row -> {
                        Player p = playerService.findById(Long.parseLong(row[0].toString())).get();
                        GameV2 from_game = gameV2Service.findById(Long.parseLong(row[2].toString())).get();
                        GameV2 to_game = gameV2Service.findById(Long.parseLong(row[3].toString())).get();
                        Integer matchCnt = ((BigInteger) row[5]).intValue();
                        return matchCnt + " games --> " + p.getNameAsLink() + ": "
                                + from_game.getGame_idAsLink() + " (" + from_game.getPlayedOn_IST() + ") to "
                                + to_game.getGame_idAsLink() + " (" + to_game.getPlayedOn_IST() + ")";
                    }).collect(Collectors.joining("<br>"));
            fameList.add(new RecordStat("Longest winning streak (Player)", playerBestWinningStreakDesc));

            //best winning streak = team
            fameList.add(new RecordStat("Longest winning streak (Team)", "todo"));

            model.addAttribute("fameRecords", fameList);
        }
        end = System.currentTimeMillis();
        diff = (end - start) / 1000F;
        System.out.println(diff);
        start = System.currentTimeMillis();
        {
            //hall of shame
            List<RecordStat> shameList = new ArrayList<>();

            //biggest defeat
            String biggestDefeatDesc = highestScore.stream().
                    map(game -> Math.abs(game.getScore1() - game.getScore2()) + " pts --> " + game.getGame_idAsLink() + ": " + game.getLosingResultWithScore()).
                    collect(Collectors.joining("<br>"));
            shameList.add(new RecordStat("Biggest Defeat", biggestDefeatDesc));

            //least played - player
            List<Object[]> playerLeastPlayed = playerService.getPlayerWithLowestPlayedCount();
            String playerMostPlayedDesc = playerLeastPlayed.stream().
                    map(row -> {
                        Player p = playerService.findById(Long.parseLong(row[0].toString())).get();
                        BigInteger playedCnt = (BigInteger) row[1];
                        return playedCnt + " games --> " + p.getNameAsLink();
                    }).collect(Collectors.joining("<br>"));
            shameList.add(new RecordStat("Least games played (Player)", playerMostPlayedDesc));

            //least played - team
            List<Object[]> teamLeastPlayed = teamService.getTeamWithLowestPlayedCount();
            String teamLeastPlayedDesc = teamLeastPlayed.stream().
                    map(row -> {
                        Team t = teamService.findById(Long.parseLong(row[0].toString())).get();
                        BigInteger playedCnt = (BigInteger) row[1];
                        return playedCnt + " games --> " + t.getNameAsLink();
                    }).collect(Collectors.joining("<br>"));
            shameList.add(new RecordStat("Least games played (Team)", teamLeastPlayedDesc));

            //most losses - player
            List<Object[]> playerMostLost = playerService.getPlayerWithHighestLossCount();
            String playerMostLostDesc = playerMostLost.stream().
                    map(row -> {
                        Player p = playerService.findById(Long.parseLong(row[0].toString())).get();
                        BigInteger wonCnt = (BigInteger) row[1];
                        return wonCnt + " games --> " + p.getNameAsLink();
                    }).collect(Collectors.joining("<br>"));
            shameList.add(new RecordStat("Most games lost (Player)", playerMostLostDesc));

            //most losses - team
            List<Object[]> teamMostLoss = teamService.getTeamWithHighestLossCount();
            String teamMostLossDesc = teamMostLoss.stream().
                    map(row -> {
                        Team t = teamService.findById(Long.parseLong(row[0].toString())).get();
                        BigInteger wonCnt = (BigInteger) row[1];
                        return wonCnt + " games --> " + t.getNameAsLink();
                    }).collect(Collectors.joining("<br>"));
            shameList.add(new RecordStat("Most games lost (Team)", teamMostLossDesc));

            //least attendance - player
            List<Object[]> playerLeastAttendance = playerService.getPlayerWithLowestAttendance();
            String playerLeastAttendanceDesc = playerLeastAttendance.stream().
                    map(row -> {
                        Player p = playerService.findById(Long.parseLong(row[0].toString())).get();
                        BigInteger matchDayCnt = (BigInteger) row[1];
                        return matchDayCnt + " matchdays --> " + p.getNameAsLink();
                    }).collect(Collectors.joining("<br>"));
            shameList.add(new RecordStat("Least attendance (Player)", playerLeastAttendanceDesc));

            //least attendance - team
            List<Object[]> teamLeastAttendance = teamService.getTeamWithLowestAttendance();
            String teamLeastAttendanceDesc = teamLeastAttendance.stream().
                    map(row -> {
                        Team t = teamService.findById(Long.parseLong(row[0].toString())).get();
                        BigInteger matchDayCnt = (BigInteger) row[1];
                        return matchDayCnt + " matchdays --> " + t.getNameAsLink();
                    }).collect(Collectors.joining("<br>"));
            shameList.add(new RecordStat("Least attendance (Team)", teamLeastAttendanceDesc));

            //best losing streak - player
            List<Object[]> playerBestLosingStreak = playerService.getBestLosingStreak();
            String playerBestLosingStreakDesc = playerBestLosingStreak.stream().
                    map(row -> {
                        Player p = playerService.findById(Long.parseLong(row[0].toString())).get();
                        GameV2 from_game = gameV2Service.findById(Long.parseLong(row[2].toString())).get();
                        GameV2 to_game = gameV2Service.findById(Long.parseLong(row[3].toString())).get();
                        Integer matchCnt = ((BigInteger) row[5]).intValue();
                        return matchCnt + " games --> " + p.getNameAsLink() + ": "
                                + from_game.getGame_idAsLink() + " (" + from_game.getPlayedOn_IST() + ") to "
                                + to_game.getGame_idAsLink() + " (" + to_game.getPlayedOn_IST() + ")";
                    }).collect(Collectors.joining("<br>"));
            shameList.add(new RecordStat("Longest losing streak (Player)", playerBestLosingStreakDesc));

            //best losing streak - team
            shameList.add(new RecordStat("Longest losing streak (Team)", "todo"));

            model.addAttribute("shameRecords", shameList);
        }
        end = System.currentTimeMillis();
        diff = (end - start) / 1000F;
        System.out.println(diff);
        return "showRecords";
    }
*/
}
