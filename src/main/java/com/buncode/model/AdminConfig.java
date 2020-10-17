package com.buncode.model;

import com.buncode.util.CommonUtil;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "admin_config")
public class AdminConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long config_id;

    private boolean match_lock;

    public Long getConfig_id() {
        return config_id;
    }

    public void setConfig_id(Long config_id) {
        this.config_id = config_id;
    }

    public boolean isMatch_lock() {
        return match_lock;
    }

    public void setMatch_lock(boolean match_lock) {
        this.match_lock = match_lock;
    }

    public AdminConfig(Long config_id, boolean match_lock) {
        this.config_id = config_id;
        this.match_lock = match_lock;
    }

    public AdminConfig() {
    }
}
