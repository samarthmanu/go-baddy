package com.buncode.model;

import javax.persistence.*;

@Entity
@Table(name = "points_config")
public class PointsConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rule_id;
    private String rule;
    private int multiplier;

    public Long getRule_id() {
        return rule_id;
    }

    public void setRule_id(Long rule_id) {
        this.rule_id = rule_id;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public PointsConfig(Long rule_id, String rule, int multiplier) {
        this.rule_id = rule_id;
        this.rule = rule;
        this.multiplier = multiplier;
    }

    public PointsConfig() {
    }
}
