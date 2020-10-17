package com.buncode.controller;

import com.buncode.model.*;
import com.buncode.service.IGameV2Service;
import com.buncode.service.IPlayerService;
import com.buncode.service.ITeamService;
import com.buncode.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class MiscController {

    @Autowired
    private IPlayerService playerService;

    @Autowired
    private IGameV2Service gameV2Service;

    @Autowired
    private ITeamService teamService;

    @GetMapping("/leaderboard")
    public String getLeaderBoard(Model model) {

        //player leaderboard
        {
            List<Player> players = playerService.findAllValid();
            List playerStats = new ArrayList<PlayerStat>();
            for (Player p : players) {

                int played = gameV2Service.getGamesPlayedByPlayer(p).size();
                int won = gameV2Service.getGamesWonByPlayer(p).size();
                double ratio = CommonUtil.calcWinRatio(played, won);

                PlayerStat pStat = new PlayerStat(p, played, won, ratio);
                playerStats.add(pStat);
            }

            //Sort by win ratio descending
            Collections.sort(playerStats); //sort by win ratio
            model.addAttribute("players", playerStats);
        }
        //team leaderboard
        {
            List<Team> teams = teamService.findAll();
            List teamStats = new ArrayList<TeamStat>();
            for (Team t : teams) {

                int played = gameV2Service.getGamesPlayedByTeam(t).size();
                int won = gameV2Service.getGamesWonByTeam(t).size();
                double ratio = CommonUtil.calcWinRatio(played, won);

                TeamStat tStat = new TeamStat(t, played, won, ratio);
                teamStats.add(tStat);
            }

            //Sort by win ratio descending
            Collections.sort(teamStats); //sort by win ratio
            model.addAttribute("teams", teamStats);
        }

        //hall of fame
        {
            List<RecordStat> fameList = new ArrayList<>();
            fameList.add(new RecordStat("Most games played (Player)", "todo"));
            fameList.add(new RecordStat("Most games won (Player)", "todo"));
            fameList.add(new RecordStat("Most attendance (Player)", "todo"));
            fameList.add(new RecordStat("Biggest Win (Winners)", "todo"));
            fameList.add(new RecordStat("Longest winning streak (Player)", "todo"));
            fameList.add(new RecordStat("Longest winning streak (Team)", "todo"));
            fameList.add(new RecordStat("Most games played (Team)", "todo"));
            fameList.add(new RecordStat("Most games won (Team)", "todo"));
            model.addAttribute("fameRecords", fameList);
        }

        //hall of shame
        {
            List<RecordStat> shameList = new ArrayList<>();
            shameList.add(new RecordStat("Least games played (Player)", "todo"));
            shameList.add(new RecordStat("Least games won (Player)", "todo"));
            shameList.add(new RecordStat("Least attendance (Player)", "todo"));
            shameList.add(new RecordStat("Biggest Defeat (Losers)", "todo"));
            shameList.add(new RecordStat("Longest losing streak (Player)", "todo"));
            shameList.add(new RecordStat("Longest losing streak (Team)", "todo"));
            shameList.add(new RecordStat("Least games played (Team)", "todo"));
            shameList.add(new RecordStat("Least games won (Team)", "todo"));
            model.addAttribute("shameRecords", shameList);
        }

        return "showLeaderboard";
    }

}