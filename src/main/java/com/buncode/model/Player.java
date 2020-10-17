package com.buncode.model;

import com.buncode.util.CommonUtil;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long player_id;

    private String name;
    private String playing_style;
    private String signature_moves;
    private String alias;
    private String contact;
    private boolean invalidate;

    @CreationTimestamp
    @Column(name="updated_on", nullable = false, updatable = true, insertable = false)
    private Timestamp updated_on;

    @CreationTimestamp
    @Column(name="updated_on", nullable = false, updatable = false, insertable = false)
    private Timestamp created_on;


    public Player(String name, String playing_style, String signature_moves, String alias, String contact, boolean invalidate, Timestamp updated_on, Timestamp created_on) {
        this.name = name;
        this.playing_style = playing_style;
        this.signature_moves = signature_moves;
        this.alias = alias;
        this.contact = contact;
        this.invalidate = invalidate;
        this.updated_on = updated_on;
        this.created_on = created_on;
    }

    public Player() {
    }

    public boolean isInvalidate() {
        return invalidate;
    }

    public void setInvalidate(boolean invalidate) {
        this.invalidate = invalidate;
    }

    public Long getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(Long player_id) {
        this.player_id = player_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Player{");
        sb.append("player_id=").append(player_id);
        sb.append(", name=").append(name);
        sb.append('}');
        return sb.toString();
    }
}