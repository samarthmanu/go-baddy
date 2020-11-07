package com.buncode.util;

import com.buncode.model.PlayerStats;
import com.buncode.model.TeamStats;

import java.util.Comparator;

public class CustomSort {

    public static class SortPlayerStatsByWinRatio implements Comparator<PlayerStats>
    {
        public int compare(PlayerStats b, PlayerStats a)
        {
            if(a.getWinRatio()==b.getWinRatio()){
                return Integer.compare(a.getPlayed(), b.getPlayed());
            }else{
                return Double.compare(a.getWinRatio(), b.getWinRatio());
            }
        }
    }

    public static class SortPlayerStatsByPoints implements Comparator<PlayerStats>
    {
        public int compare(PlayerStats b, PlayerStats a)
        {
            if(a.getfMap().get("total_pts").getTotal()
                    ==b.getfMap().get("total_pts").getTotal()){
                if(a.getfMap().get("played_pts").getTotal()
                        ==b.getfMap().get("played_pts").getTotal()){
                    return Integer.compare(a.getfMap().get("won_pts").getTotal(),
                            b.getfMap().get("won_pts").getTotal());
                }else{
                    return Integer.compare(a.getfMap().get("played_pts").getTotal(),
                            b.getfMap().get("played_pts").getTotal());
                }
            }else{
                return Integer.compare(a.getfMap().get("total_pts").getTotal(),
                        b.getfMap().get("total_pts").getTotal());
            }
        }
    }

    public static class SortTeamStatsByWinRatio implements Comparator<TeamStats>
    {
        public int compare(TeamStats b, TeamStats a)
        {
            if(a.getWinRatio()==b.getWinRatio()){
                return Integer.compare(a.getPlayed(), b.getPlayed());
            }else{
                return Double.compare(a.getWinRatio(), b.getWinRatio());
            }
        }
    }

    public static class SortTeamStatsByPoints implements Comparator<TeamStats>
    {
        public int compare(TeamStats b, TeamStats a)
        {
            if(a.getfMap().get("total_pts").getTotal()
                    ==b.getfMap().get("total_pts").getTotal()){
                if(a.getfMap().get("played_pts").getTotal()
                        ==b.getfMap().get("played_pts").getTotal()){
                    return Integer.compare(a.getfMap().get("won_pts").getTotal(),
                            b.getfMap().get("won_pts").getTotal());
                }else{
                    return Integer.compare(a.getfMap().get("played_pts").getTotal(),
                            b.getfMap().get("played_pts").getTotal());
                }
            }else{
                return Integer.compare(a.getfMap().get("total_pts").getTotal(),
                        b.getfMap().get("total_pts").getTotal());
            }
        }
    }
}
