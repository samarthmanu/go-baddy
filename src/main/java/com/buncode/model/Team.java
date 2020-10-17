package com.buncode.model;

import com.buncode.util.CommonUtil;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long team_id;

    private String name;
    private String playing_style;
    private String signature_moves;
    private String alias;

    @CreationTimestamp
    @Column(name="updated_on", nullable = false, updatable = true, insertable = false)
    private Timestamp updated_on;

    @CreationTimestamp
    @Column(name="created_on", nullable = false, updatable = false, insertable = false)
    private Timestamp created_on;

    @ManyToOne
    @JoinColumn(name = "p1")
    private Player p1;

    @ManyToOne
    @JoinColumn(name = "p2")
    private Player p2;

    public String getPlaying_style() {
        return playing_style;
    }

    public void setPlaying_style(String playing_style) {
        this.playing_style = playing_style;
    }

    public String getSignature_moves() {
        return signature_moves;
    }

    public void setSignature_moves(String signature_moves) {
        this.signature_moves = signature_moves;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

    public String getCreatedOn_IST() {
        return CommonUtil.getTimeInIST(created_on);
    }

    public void setCreated_on(Timestamp created_on) {
        this.created_on = created_on;
    }

    public Long getTeam_id() {
        return team_id;
    }

    public void setTeam_id(Long team_id) {
        this.team_id = team_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Team() {
    }

    public Team(String name, String playing_style, String signature_moves, String alias, Timestamp updated_on, Timestamp created_on, Player p1, Player p2) {
        this.name = name;
        this.playing_style = playing_style;
        this.signature_moves = signature_moves;
        this.alias = alias;
        this.updated_on = updated_on;
        this.created_on = created_on;
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Team{");
        sb.append("team_id=").append(team_id);
        sb.append(", name=").append(name);
        sb.append(", p1=").append(p1);
        sb.append(", p2=").append(p2);
        sb.append('}');
        return sb.toString();
    }
}