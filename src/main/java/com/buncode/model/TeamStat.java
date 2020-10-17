package com.buncode.model;

public class TeamStat implements Comparable<TeamStat> {

    private Team team;
    private int played;
    private int won;
    private double ratio;

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

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public TeamStat(Team team, int played, int won, double ratio) {
        this.team = team;
        this.played = played;
        this.won = won;
        this.ratio = ratio;
    }

    @Override
    public int compareTo(TeamStat p) {
        return Double.compare(p.ratio, this.ratio);
    }
}
