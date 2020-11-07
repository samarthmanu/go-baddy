package com.buncode.model;

import com.buncode.util.CommonUtil;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Component
@Entity
@Cacheable
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long game_id;

    @ManyToOne
    @JoinColumn(name = "p1")
    private Player p1;

    @ManyToOne
    @JoinColumn(name = "p2")
    private Player p2;

    @ManyToOne
    @JoinColumn(name = "p3")
    private Player p3;

    @ManyToOne
    @JoinColumn(name = "p4")
    private Player p4;

    private int score1;
    private int score2;
    private boolean invalidate;

    @CreationTimestamp
    @Column(name="updated_on", nullable = false, updatable = true, insertable = false)
    private Timestamp updated_on;

    @CreationTimestamp
    @Column(name="created_on", nullable = false, updatable = false, insertable = false)
    private Timestamp created_on;

    public Game(Player p1, Player p2, Player p3, Player p4, int score1, int score2, boolean invalidate, Timestamp updated_on, Timestamp created_on) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.score1 = score1;
        this.score2 = score2;
        this.invalidate = invalidate;
        this.updated_on = updated_on;
        this.created_on = created_on;
    }

    public Game() {
    }

    public String getPlayedOn_IST() {
        return CommonUtil.getTimeInIST(created_on);
    }

    public boolean isInvalidate() {
        return invalidate;
    }

    public void setInvalidate(boolean invalidate) {
        this.invalidate = invalidate;
    }

    public Long getGame_id() {
        return game_id;
    }

    public void setGame_id(Long game_id) {
        this.game_id = game_id;
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

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public Timestamp getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(Timestamp updated_on) {
        this.updated_on = updated_on;
    }

    public Timestamp getCreated_on() {
        return created_on;
    }

    public void setCreated_on(Timestamp created_on) {
        this.created_on = created_on;
    }

    public String getResult() {
        if(score1 > score2){
            return "WINNERS: " + p1.getName() + " & " + p2.getName();
        }else{
            return "WINNERS: " + p3.getName() + " & " + p4.getName();
        }
    }

    public List<Player> getWinners() {
        Player winners[];
        if (score1 > score2) {
                return Arrays.asList(new Player[]{p1,p2});
            } else {
                return Arrays.asList(new Player[]{p3,p4});
            }
    }

    public String getPlayerResult(Player player){
        if(getWinners().contains(player)) {
            return "WON";
        }else {
            return "LOST";
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Game{");
        sb.append("game_id=").append(game_id);
        sb.append(", p1=").append(p1);
        sb.append(", p2=").append(p2);
        sb.append(", score1=").append(score1);
        sb.append(", p3=").append(p3);
        sb.append(", p4=").append(p4);
        sb.append(", score2=").append(score2);
        sb.append(", WINNER").append(getResult());
        sb.append('}');
        return sb.toString();
    }
}