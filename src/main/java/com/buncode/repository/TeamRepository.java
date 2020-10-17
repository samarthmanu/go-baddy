package com.buncode.repository;

import com.buncode.model.Player;
import com.buncode.model.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {

    @Query("SELECT t FROM Team t WHERE :player in (p1, p2)")
    List<Team> getTeamsByPlayer(@Param("player") Player player);

    @Query("SELECT t FROM Team t WHERE (:player1=p1 AND :player2=p2) OR (:player1=p2 AND :player2=p1)")
    Team getTeamByPlayerCombo(@Param("player1") Player player1, @Param("player2") Player player2);

    @Query("SELECT t from Team t WHERE UPPER(TRIM(name))=UPPER(TRIM(:t_name))")
    Team getTeamByName(@Param("t_name") String t_name);
}