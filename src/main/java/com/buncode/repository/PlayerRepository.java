package com.buncode.repository;

import com.buncode.model.Player;
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

    @Query(value = "SELECT IFNULL(SUM(team_changes),0) as team_changes\n" +
            "FROM (SELECT match_day,\n" +
            "             COUNT(DISTINCT played_with) AS team_changes\n" +
            "      FROM (SELECT game_id,\n" +
            "                   CAST(played_on AS DATE) AS match_day,\n" +
            "                   CASE\n" +
            "                     WHEN p1 = :player THEN p2\n" +
            "                     WHEN p2 = :player THEN p1\n" +
            "                     WHEN p3 = :player THEN p4\n" +
            "                     WHEN p4 = :player THEN p3\n" +
            "                   END AS played_with\n" +
            "            FROM games_v2\n" +
            "            WHERE invalidate IS FALSE " +
            "            AND :player IN (p1,p2,p3,p4)) team_changes\n" +
            "      GROUP BY match_day) t", nativeQuery = true)
    Integer getTeamChangeCountByPlayer(@Param("player") Player player);

   /*

    @Query(value = "SELECT *\n" +
            "FROM (SELECT *,\n" +
            "             DENSE_RANK() OVER (ORDER BY games_played DESC) AS p_rank\n" +
            "      FROM (SELECT player_id,\n" +
            "                   (SELECT COUNT(game_id)\n" +
            "                    FROM games_v2\n" +
            "                    WHERE invalidate = FALSE\n" +
            "                    AND   p.player_id IN (p1,p2,p3,p4)) AS games_played\n" +
            "            FROM players p\n" +
            "            WHERE p.invalidate = FALSE) played) ranked\n" +
            "WHERE p_rank = 1", nativeQuery = true)
    List<Object[]> getPlayerWithHighestPlayedCount();

    @Query(value = "SELECT *\n" +
            "FROM (SELECT *,\n" +
            "             DENSE_RANK() OVER (ORDER BY games_played ASC) AS p_rank\n" +
            "      FROM (SELECT player_id,\n" +
            "                   (SELECT COUNT(game_id)\n" +
            "                    FROM games_v2\n" +
            "                    WHERE invalidate = FALSE\n" +
            "                    AND   p.player_id IN (p1,p2,p3,p4)) AS games_played\n" +
            "            FROM players p\n" +
            "            WHERE p.invalidate = FALSE) played where games_played>0) ranked\n" +
            "WHERE p_rank = 1", nativeQuery = true)
    List<Object[]> getPlayerWithLowestPlayedCount();

    @Query(value = "SELECT *\n" +
            "FROM (SELECT *,\n" +
            "             DENSE_RANK() OVER (ORDER BY games_won DESC) AS p_rank\n" +
            "      FROM (SELECT player_id,\n" +
            "                   (SELECT COUNT(game_id)\n" +
            "                    FROM games_v2\n" +
            "                    WHERE invalidate = FALSE\n" +
            "                    AND ((p.player_id in (p1,p2) AND score1 > score2) or (p.player_id in (p3,p4) AND score2 > score1))) AS games_won\n" +
            "            FROM players p) played where games_won>0) ranked\n" +
            "WHERE p_rank = 1", nativeQuery = true)
    List<Object[]> getPlayerWithHighestWinCount();

    @Query(value = "SELECT *\n" +
            "FROM (SELECT *,\n" +
            "             DENSE_RANK() OVER (ORDER BY games_lost DESC) AS p_rank\n" +
            "      FROM (SELECT player_id,\n" +
            "                   (SELECT COUNT(game_id)\n" +
            "                    FROM games_v2\n" +
            "                    WHERE invalidate = FALSE\n" +
            "                    AND ((p.player_id in (p1,p2) AND score1 < score2) or (p.player_id in (p3,p4) AND score2 < score1))) AS games_lost\n" +
            "            FROM players p) played where games_lost>0) ranked\n" +
            "WHERE p_rank = 1", nativeQuery = true)
    List<Object[]> getPlayerWithHighestLossCount();

    @Query(value = "SELECT *\n" +
            "FROM (SELECT *,\n" +
            "             DENSE_RANK() OVER (ORDER BY matchdays DESC) AS p_rank\n" +
            "      FROM (SELECT player_id,\n" +
            "                   (SELECT COUNT(distinct cast(played_on as date))\n" +
            "                    FROM games_v2\n" +
            "                    WHERE invalidate = FALSE\n" +
            "                    AND   p.player_id IN (p1,p2,p3,p4)) AS matchdays\n" +
            "            FROM players p\n" +
            "            WHERE p.invalidate = FALSE) attended where matchdays>0) ranked\n" +
            "WHERE p_rank = 1", nativeQuery = true)
    List<Object[]> getPlayerWithHighestAttendance();

    @Query(value = "SELECT *\n" +
            "FROM (SELECT *,\n" +
            "             DENSE_RANK() OVER (ORDER BY matchdays ASC) AS p_rank\n" +
            "      FROM (SELECT player_id,\n" +
            "                   (SELECT COUNT(distinct cast(played_on as date))\n" +
            "                    FROM games_v2\n" +
            "                    WHERE invalidate = FALSE\n" +
            "                    AND   p.player_id IN (p1,p2,p3,p4)) AS matchdays\n" +
            "            FROM players p\n" +
            "            WHERE p.invalidate = FALSE) attended where matchdays>0) ranked\n" +
            "WHERE p_rank = 1", nativeQuery = true)
    List<Object[]> getPlayerWithLowestAttendance();

    @Query(value = "call getPlayerGameStreaks('WON')", nativeQuery = true)
    List<Object[]> getBestWinningStreak();

    @Query(value = "call getPlayerGameStreaks('LOST')", nativeQuery = true)
    List<Object[]> getBestLosingStreak();

    @Query(nativeQuery = true)
    PlayerStats getPlayerStats(@Param("player_id") Long player_id);*/

}