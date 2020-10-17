package com.buncode.repository;

import com.buncode.model.GameV2;
import com.buncode.model.Player;
import com.buncode.model.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameV2Repository extends CrudRepository<GameV2, Long> {

    @Query("SELECT g FROM GameV2 g WHERE  invalidate IS FALSE AND :player in (p1, p2, p3, p4)")
    List<GameV2> getGamesPlayedByPlayer(@Param("player") Player player);

    @Query("SELECT g " +
            "FROM GameV2 g " +
            "WHERE  invalidate IS FALSE AND ((:player IN (p1,p2) AND score1 > score2) or (:player IN (p3,p4) AND score2 > score1))")
    List<GameV2> getGamesWonByPlayer(@Param("player") Player player);

    @Query("SELECT g " +
            "FROM GameV2 g " +
            "WHERE  invalidate IS FALSE AND ((:player IN (p1,p2) AND score1 < score2) or (:player IN (p3,p4) AND score2 < score1))")
    List<GameV2> getGamesLostByPlayer(@Param("player") Player player);

    @Query("SELECT g FROM GameV2 g WHERE  invalidate IS FALSE AND :team in (t1, t2)")
    List<GameV2> getGamesPlayedByTeam(@Param("team") Team team);

    @Query("SELECT g " +
            "FROM GameV2 g " +
            "WHERE  invalidate IS FALSE AND ((:team = t1 AND score1 > score2) or (:team = t2 AND score2 > score1))")
    List<GameV2> getGamesWonByTeam(@Param("team") Team team);

    @Query("SELECT g " +
            "FROM GameV2 g " +
            "WHERE  invalidate IS FALSE AND ((:team = t1 AND score1 < score2) or (:team = t2 AND score2 < score1))")
    List<GameV2> getGamesLostByTeam(@Param("team") Team team);

}