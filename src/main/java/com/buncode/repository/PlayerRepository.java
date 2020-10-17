package com.buncode.repository;

import com.buncode.model.Player;
import com.buncode.model.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

    @Query("SELECT p from Player p WHERE UPPER(TRIM(name))=UPPER(TRIM(:p_name))")
    Player getPlayerByName(@Param("p_name") String p_name);

    @Query("SELECT p from Player p WHERE invalidate IS FALSE")
    List<Player> findAllValid();


}