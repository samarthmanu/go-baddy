package com.buncode.model;

import com.buncode.util.CommonUtil;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Cacheable
@Table(name = "games_v2")
public class GameV2 {

    @Id
    private Long game_id;

    private Timestamp played_at;

    private Date played_on;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "p1")
    private Player p1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "p2")
    private Player p2;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "t1")
    private Team t1;

    private int score1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "p3")
    private Player p3;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "p4")
    private Player p4;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "t2")
    private Team t2;

    private int score2;

    private boolean invalidate;

    @Transient
    private List<Player> winners;

    @Transient
    private String losers;

    @Transient
    private String result;

    public boolean isInvalidate() {
        return invalidate;
    }

    public void setInvalidate(boolean invalidate) {
        this.invalidate = invalidate;
    }

    public Long getGame_id() {
        return game_id;
    }
    public String getGame_idAsLinktoMatchHist(Long season_id) {
        return MessageFormat.format("<a href=\"matchHist?season_id={0}\" title=\"Click to goto match history page\">#{1}</a>", season_id,game_id);
    }

    public void setGame_id(Long game_id) {
        this.game_id = game_id;
    }

    public Timestamp getPlayed_at() {
        return played_at;
    }

    public String getPlayedOn_IST() {
        return CommonUtil.getTimeInIST(played_at);
    }

    public void setPlayed_at(Timestamp played_at) {
        this.played_at = played_at;
    }

    public Player getP1() {
        return p1;
    }

    public void setP1(Player p1) {
        this.p1 = p1;
    }

    public Player getP2() {
        return p2;
    }

    public void setP2(Player p2) {
        this.p2 = p2;
    }

    public Team getT1() {
        return t1;
    }

    public Date getPlayed_on() {
        return played_on;
    }

    public void setPlayed_on(Date played_on) {
        this.played_on = played_on;
    }

    /*public String getTeam1(){
        if (t1!=null) {
            return t1.getName();
        } else{
            return p1.getName() + " & " + p2.getName();
        }
    }*/

    public void setT1(Team t1) {
        this.t1 = t1;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public Player getP3() {
        return p3;
    }

    public void setP3(Player p3) {
        this.p3 = p3;
    }

    public Player getP4() {
        return p4;
    }

    public void setP4(Player p4) {
        this.p4 = p4;
    }

    public Team getT2() {
        return t2;
    }

    public void setT2(Team t2) {
        this.t2 = t2;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public int getScoreDiff(){
        return Math.abs(score1-score2);
    }

    public int getScoreSum(){
        return score1+score2;
    }

  /*  public String getTeam2(){
        if (t2!=null) {
            return t2.getName();
        } else{
            return p3.getName() + " & " + p4.getName();
        }
    }*/

    public String getResult(){
        if (score1 > score2){
            return "WINNERS: " + (t1 != null? t1.getName() : p1.getName() + " & " + p2.getName());
        }else{
            return "WINNERS: " + (t2 != null? t2.getName() : p3.getName() + " & " + p4.getName());
        }
    }

    public String getWinningResultWithScore(){
        if (score1 > score2){
            return (t1 != null? t1.getNameAsLink() :
                        p1.getNameAsLink() + " & " + p2.getNameAsLink()) + " [" + score1 + " pts] won against "
                    + (t2 != null? t2.getNameAsLink() :
                        p3.getNameAsLink() + " & " + p4.getNameAsLink()) + " [" + score2 + " pts]";
        }else{
            return (t2 != null? t2.getNameAsLink() :
                    p3.getNameAsLink() + " & " + p4.getNameAsLink()) + " [" + score2 + " pts] won against "
                    + (t1 != null? t1.getNameAsLink() :
                    p1.getNameAsLink() + " & " + p2.getNameAsLink()) + " [" + score1 + " pts]";
        }
    }

    public String getLosingResultWithScore(){
        if (score1 < score2){
            return (t1 != null? t1.getNameAsLink() :
                    p1.getNameAsLink() + " & " + p2.getNameAsLink()) + " [" + score1 + " pts] lost to "
                    + (t2 != null? t2.getNameAsLink() :
                    p3.getNameAsLink() + " & " + p4.getNameAsLink()) + " [" + score2 + " pts]";
        }else{
            return (t2 != null? t2.getNameAsLink() :
                    p3.getNameAsLink() + " & " + p4.getNameAsLink()) + " [" + score2 + " pts] lost to "
                    + (t1 != null? t1.getNameAsLink() :
                    p1.getNameAsLink() + " & " + p2.getNameAsLink()) + " [" + score1 + " pts]";
        }
    }

    public List<Player> getPlayers() {
        //return Arrays.asList(new Long[]{p1.getPlayer_id(), p2.getPlayer_id(),p3.getPlayer_id(),p4.getPlayer_id()});
        return Arrays.asList(new Player[]{p1, p2, p3, p4});
    }

    /*public List<Long> getTeams() {
        List<Long> teamList = new ArrayList<Long>();
        if (t1!=null) teamList.add(t1.getTeam_id());
        if (t2!=null) teamList.add(t2.getTeam_id());
        return teamList;
    }*/

    public List<Team> getTeams() {
        List<Team> teams = new ArrayList<>();
        if(t1!=null) teams.add(t1);
        if(t2!=null) teams.add(t2);

        return teams;
    }


    public List<Player> getWinners() {
        Player[] winners = {};
        if (score1>score2) {
            winners = new Player[]{p1, p2};
        }else{
            winners = new Player[]{p3,p4};
        }
        return Arrays.asList(winners);
    }

    public Team getWinningTeam() {
        if (score1>score2) {
            return t1;
        }else{
            return t2;
        }
    }

    /*public Player[] getLosers(){
        Player[] losers = {};
        if (score1>score2) {
            losers = new Player[]{p3, p4};
        }else{
            losers = new Player[]{p1,p2};
        }
        return losers;
    }
     */


    public String getResult(Player p){
        if ((p.equals(p1) || p.equals(p2)) && (score1>score2))   return "WON";
        if ((p.equals(p1) || p.equals(p2)) && (score1<score2))   return "LOST";
        if ((p.equals(p3) || p.equals(p4)) && (score2>score1))   return "WON";
        if ((p.equals(p3) || p.equals(p4)) && (score2<score1))   return "LOST";
        return "N/A";
    }

    public String getResult(Team t){
        if (t1!=null && t1.equals(t) && score1>score2)  return "WON";
        if (t1!=null && t1.equals(t) && score1<score2)  return "LOST";
        if (t2!=null && t2.equals(t) && score2>score1)  return "WON";
        if (t2!=null && t2.equals(t) && score2<score1)   return "LOST";
        return "N/A";
    }

    public Player getPartner(Player p) {
        if(p1.equals(p)) return p2;
        if(p2.equals(p)) return p1;
        if(p3.equals(p)) return p4;
        if(p4.equals(p)) return p3;
        return null;
    }

    public GameV2() {
    }

    public GameV2(Long game_id, Player p1, Player p2, Team t1, int score1, Player p3, Player p4, Team t2, int score2) {
        this.game_id = game_id;
        this.p1 = p1;
        this.p2 = p2;
        this.t1 = t1;
        this.score1 = score1;
        this.p3 = p3;
        this.p4 = p4;
        this.t2 = t2;
        this.score2 = score2;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GameV2
                && o!=null
                && ((GameV2) o).game_id == this.game_id){
            return true;
        }else{
            return false;
        }
    }
}