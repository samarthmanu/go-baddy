package com.buncode.model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "seasons")
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long season_id;
    private String name;
    private Date effective_from;
    private Date effective_to;

    public Long getSeason_id() {
        return season_id;
    }

    public void setSeason_id(Long season_id) {
        this.season_id = season_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEffective_from() {
        return effective_from;
    }

    public void setEffective_from(Date effective_from) {
        this.effective_from = effective_from;
    }

    public Date getEffective_to() {
        return effective_to;
    }

    public void setEffective_to(Date effective_to) {
        this.effective_to = effective_to;
    }

    public Season() {
    }

}
