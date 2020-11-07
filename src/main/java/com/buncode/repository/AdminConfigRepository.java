package com.buncode.repository;

import com.buncode.model.AdminConfig;
import com.buncode.model.Game;
import com.buncode.model.Team;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AdminConfigRepository extends CrudRepository<AdminConfig, Long> {

    @Query("SELECT a from AdminConfig a")
    AdminConfig getAdminConfig();

}