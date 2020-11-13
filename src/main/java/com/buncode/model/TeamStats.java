package com.buncode.model;

import com.buncode.util.CommonUtil;

import java.util.Map;

public class TeamStats {


    private Team team;

    private int played;
    private int won;
    private int lost;
    private String recentForm;
    private String currentStreak;

    public String getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(String currentStreak) {
        this.currentStreak = currentStreak;
    }

    public String getRecentForm() {
        return recentForm;
    }

    public void setRecentForm(String recentForm) {
        this.recentForm = recentForm;
    }

    private Map<String, FantasyStat> fMap;

    public Map<String, FantasyStat> getfMap() {
        return fMap;
    }

    public void setfMap(Map<String, FantasyStat> fMap) {
        this.fMap = fMap;
    }

    public double getWinRatio() {
        return CommonUtil.calcWinPercentage(played, won);
    }

    public double calcRatio(int x, int y) {
        return CommonUtil.calcRatio(x, y);
    }

    public int getLost() {
        return lost;
    }

    public void setLost(int lost) {
        this.lost = lost;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int won) {
        this.won = won;
    }


    public TeamStats(Team team, int played, int won, int lost) {
        this.team = team;
        this.played = played;
        this.won = won;
        this.lost = lost;
    }

    public TeamStats(Team team, int played, int won, int lost, Map<String, FantasyStat> fMap) {
        this.team = team;
        this.played = played;
        this.won = won;
        this.lost = lost;
        this.fMap = fMap;
    }

    /*public TeamStats(Team team) {
        this.team = team;
    }
*
    public TeamStats() {
    }*/
}