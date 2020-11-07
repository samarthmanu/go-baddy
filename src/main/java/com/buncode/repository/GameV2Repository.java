package com.buncode.repository;

import com.buncode.model.GameV2;
import com.buncode.model.Season;
import org.hibernate.type.DateType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface GameV2Repository extends CrudRepository<GameV2, Long> {

    @Query("SELECT g FROM GameV2 g WHERE  invalidate IS FALSE")
    List<GameV2> findAllValid();

    @Query("SELECT g FROM GameV2 g, Season s WHERE s=:season AND g.invalidate IS FALSE AND g.played_on BETWEEN s.effective_from AND s.effective_to")
    List<GameV2> findAllValidBySeason(@Param("season") Season season);

    //@Query("SELECT g FROM GameV2 g WHERE g.invalidate IS FALSE AND (g.played_at>=:fromDate  AND g.played_at<=:toDate)")
    @Query("SELECT g FROM GameV2 g WHERE g.invalidate IS FALSE AND g.played_at BETWEEN :fromDate  AND :toDate")
    List<GameV2> findAllValidByDateRange(@Param("fromDate") Timestamp fromDate, @Param("toDate") Timestamp toDate);

    /*@Query("SELECT g FROM GameV2 g WHERE  invalidate IS FALSE AND :player in (p1, p2, p3, p4)")
    List<GameV2> getGamesPlayedByPlayer(@Param("player") Player player);

    @Query("SELECT COUNT(g) FROM GameV2 g WHERE  invalidate IS FALSE AND :player in (p1, p2, p3, p4)")
    Integer getGamesPlayedCountByPlayer(@Param("player") Player player);

    @Query("SELECT COUNT(g) " +
            "FROM GameV2 g " +
            "WHERE  invalidate IS FALSE AND ((:player IN (p1,p2) AND score1 > score2) or (:player IN (p3,p4) AND score2 > score1))")
    Integer getGamesWonCountByPlayer(@Param("player") Player player);

    @Query("SELECT g FROM GameV2 g WHERE invalidate IS FALSE AND :team in (t1, t2)")
    List<GameV2> getGamesPlayedByTeam(@Param("team") Team team);

    @Query("SELECT COUNT(g) FROM GameV2 g WHERE  invalidate IS FALSE AND :team in (t1, t2)")
    Integer getGamesPlayedCountByTeam(@Param("team") Team team);

    @Query("SELECT COUNT(g) " +
            "FROM GameV2 g " +
            "WHERE  invalidate IS FALSE AND ((:team = t1 AND score1 > score2) or (:team = t2 AND score2 > score1))")
    Integer getGamesWonCountByTeam(@Param("team") Team team);

    @Query(value = "SELECT COUNT(DISTINCT played_on) FROM GameV2 g WHERE  invalidate IS FALSE AND :player in (p1, p2, p3, p4)")
    Integer getMatchdayCountByPlayer(@Param("player") Player player);

    @Query(value = "SELECT COUNT(DISTINCT played_on) FROM GameV2 g WHERE  invalidate IS FALSE AND :team in (t1, t2)")
    Integer getMatchdayCountByTeam(@Param("team") Team team);

    @Query("SELECT COUNT(g) " +
            "FROM GameV2 g " +
            "WHERE  invalidate IS FALSE AND :player in (p1, p2, p3, p4) and score1+score2>40")
    Integer getDeuceGamesCountByPlayer(@Param("player") Player player);

    @Query("SELECT COUNT(g) " +
            "FROM GameV2 g " +
            "WHERE  invalidate IS FALSE AND :team in (t1, t2) AND score1+score2>40")
    Integer getDeuceGamesCountByTeam(@Param("team") Team team);

    @Query(value = "SELECT COUNT(DISTINCT CAST(played_on AS DATE)) as match_days FROM games_v2 WHERE invalidate IS FALSE AND :player in (p1, p2, p3, p4)", nativeQuery = true)
    Integer getMatchdayCountByPlayer(@Param("player") Player player);

    @Query(value = "SELECT COUNT(DISTINCT CAST(played_on AS DATE)) as match_days FROM games_v2 WHERE invalidate IS FALSE AND :team in (t1, t2)", nativeQuery = true)
    Integer getMatchdayCountByTeam(@Param("team") Team team);

    @Query("SELECT g " +
            "FROM GameV2 g " +
            "WHERE  invalidate IS FALSE AND ((:player IN (p1,p2) AND score1 > score2) or (:player IN (p3,p4) AND score2 > score1))")
    List<GameV2> getGamesWonByPlayer(@Param("player") Player player);

    /*@Query("SELECT g " +
            "FROM GameV2 g " +
            "WHERE  invalidate IS FALSE AND ((:player IN (p1,p2) AND score1 < score2) or (:player IN (p3,p4) AND score2 < score1))")
    List<GameV2> getGamesLostByPlayer(@Param("player") Player player);

    @Query("SELECT g " +
            "FROM GameV2 g " +
            "WHERE  invalidate IS FALSE AND ((:team = t1 AND score1 > score2) or (:team = t2 AND score2 > score1))")
    List<GameV2> getGamesWonByTeam(@Param("team") Team team);

    @Query("SELECT g " +
            "FROM GameV2 g " +
            "WHERE  invalidate IS FALSE AND ((:team = t1 AND score1 < score2) or (:team = t2 AND score2 < score1))")
    List<GameV2> getGamesLostByTeam(@Param("team") Team team);

    @Query("SELECT g " +
            "FROM GameV2 g " +
            "WHERE invalidate IS FALSE " +
            "AND   ABS(score1 - score2) = (SELECT MAX(ABS(score1 - score2)) " +
            "                              FROM GameV2\n" +
            "                              WHERE invalidate IS FALSE)")
    List<GameV2> getHighestScore();*/

}