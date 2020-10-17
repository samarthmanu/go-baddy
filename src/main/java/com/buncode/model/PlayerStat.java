package com.buncode.model;

public class PlayerStat implements Comparable<PlayerStat> {

    private Player player;
    private int played;
    private int won;
    private double ratio;

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

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public PlayerStat(Player player, int played, int won, double ratio) {
        this.player = player;
        this.played = played;
        this.won = won;
        this.ratio = ratio;
    }

    @Override
    public int compareTo(PlayerStat p) {
        return Double.compare(p.ratio, this.ratio);
    }
}
