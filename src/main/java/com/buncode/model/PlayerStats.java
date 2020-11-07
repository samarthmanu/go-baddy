package com.buncode.model;

import com.buncode.util.CommonUtil;

import java.util.Map;

public class PlayerStats{

    private Player player;

    private int played;
    private int won;
    private int lost;
    private Map<String, FantasyStat> fMap;
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

    public Map<String, FantasyStat> getfMap() {
        return fMap;
    }

    public void setfMap(Map<String, FantasyStat> fMap) {
        this.fMap = fMap;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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

    public int getLost() {
        return lost;
    }

    public void setLost(int lost) {
        this.lost = lost;
    }

    public double getWinRatio() {
        return CommonUtil.calcWinPercentage(played, won);
    }

    public PlayerStats(Player player, int played, int won, int lost) {
        this.player = player;
        this.played = played;
        this.won = won;
        this.lost = lost;
    }

    public PlayerStats(Player player, int played, int won, int lost, Map<String, FantasyStat> fMap) {
        this.player = player;
        this.played = played;
        this.won = won;
        this.lost = lost;
        this.fMap = fMap;
    }

    public PlayerStats(Player player) {
        this.player = player;
    }

    public PlayerStats() {
    }

}
