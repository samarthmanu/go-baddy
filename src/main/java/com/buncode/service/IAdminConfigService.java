package com.buncode.service;

import com.buncode.model.AdminConfig;
import com.buncode.model.Game;
import com.buncode.model.GameV2;
import com.buncode.model.Player;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

public interface IAdminConfigService {

    @Cacheable(cacheNames = "admin_config_cache")
    AdminConfig getAdminConfig();

    @CacheEvict(cacheNames = "admin_config_cache")
    AdminConfig save(AdminConfig adminConfig);

}