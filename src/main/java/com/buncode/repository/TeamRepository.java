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

    /*@Query(value = "SELECT *\n" +
            "FROM (SELECT *,\n" +
            "             DENSE_RANK() OVER (ORDER BY games_played DESC) AS p_rank\n" +
            "      FROM (SELECT team_id,\n" +
            "                   (SELECT COUNT(game_id)\n" +
            "                    FROM games_v2\n" +
            "                    WHERE invalidate = FALSE\n" +
            "                    AND   t.team_id IN (t1,t2)) AS games_played\n" +
            "            FROM teams t) played where games_played>0) ranked\n" +
            "WHERE p_rank = 1", nativeQuery = true)
    List<Object[]> getTeamWithHighestPlayedCount();

    @Query(value = "SELECT *\n" +
            "FROM (SELECT *,\n" +
            "             DENSE_RANK() OVER (ORDER BY games_played ASC) AS p_rank\n" +
            "      FROM (SELECT team_id,\n" +
            "                   (SELECT COUNT(game_id)\n" +
            "                    FROM games_v2\n" +
            "                    WHERE invalidate = FALSE\n" +
            "                    AND   t.team_id IN (t1,t2)) AS games_played\n" +
            "            FROM teams t) played where games_played>0) ranked\n" +
            "WHERE p_rank = 1", nativeQuery = true)
    List<Object[]> getTeamWithLowestPlayedCount();

    @Query(value = "SELECT *\n" +
            "FROM (SELECT *,\n" +
            "             DENSE_RANK() OVER (ORDER BY games_won DESC) AS p_rank\n" +
            "      FROM (SELECT team_id,\n" +
            "                   (SELECT COUNT(game_id)\n" +
            "                    FROM games_v2\n" +
            "                    WHERE invalidate = FALSE\n" +
            "                    AND ((t.team_id = t1 AND score1 > score2) or (t.team_id = t2 AND score2 > score1))) AS games_won\n" +
            "            FROM teams t) played where games_won>0) ranked\n" +
            "WHERE p_rank = 1", nativeQuery = true)
    List<Object[]> getTeamWithHighestWinCount();

    @Query(value = "SELECT *\n" +
            "FROM (SELECT *,\n" +
            "             DENSE_RANK() OVER (ORDER BY games_lost DESC) AS p_rank\n" +
            "      FROM (SELECT team_id,\n" +
            "                   (SELECT COUNT(game_id)\n" +
            "                    FROM games_v2\n" +
            "                    WHERE invalidate = FALSE\n" +
            "                    AND ((t.team_id = t1 AND score1 < score2) or (t.team_id = t2 AND score2 < score1))) AS games_lost\n" +
            "            FROM teams t) played where games_lost>0) ranked\n" +
            "WHERE p_rank = 1", nativeQuery = true)
    List<Object[]> getTeamWithHighestLossCount();

    @Query(value = "SELECT *\n" +
            "FROM (SELECT *,\n" +
            "             DENSE_RANK() OVER (ORDER BY matchdays DESC) AS p_rank\n" +
            "      FROM (SELECT team_id,\n" +
            "                   (SELECT COUNT(distinct cast(played_on as date))\n" +
            "                    FROM games_v2\n" +
            "                    WHERE t.team_id IN (t1,t2)) AS matchdays\n" +
            "            FROM teams t) attended where matchdays>0) ranked\n" +
            "WHERE p_rank = 1", nativeQuery = true)
    List<Object[]> getTeamWithHighestAttendance();

    @Query(value = "SELECT *\n" +
            "FROM (SELECT *,\n" +
            "             DENSE_RANK() OVER (ORDER BY matchdays ASC) AS p_rank\n" +
            "      FROM (SELECT team_id,\n" +
            "                   (SELECT COUNT(distinct cast(played_on as date))\n" +
            "                    FROM games_v2\n" +
            "                    WHERE t.team_id IN (t1,t2)) AS matchdays\n" +
            "            FROM teams t) attended where matchdays>0) ranked\n" +
            "WHERE p_rank = 1", nativeQuery = true)
    List<Object[]> getTeamWithLowestAttendance();

    @Query(nativeQuery = true)
    List<TeamStats> getTeamStatsByPlayer(@Param("player_id") Long player_id);

    @Query(nativeQuery = true)
    TeamStats getTeamStatsByTeam(@Param("team_id") Long team_id);*/

}