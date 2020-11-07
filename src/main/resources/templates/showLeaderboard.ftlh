<!DOCTYPE html>
<html>
<script>
    onload=function(){
            if(!!window.performance && window.performance.navigation.type == 2)
            {
                window.location.reload();
            }
         }
      function validateCustomDates() {
        from = document.getElementById("fromDate").value;
        to = document.getElementById("toDate").value;

        //alert(from);
        //alert(to);

         if (from.trim() == "" || to.trim() == ""){
            alert("Please select both from and to dates");
            return false;
         }

         var fromDate = new Date(from);
         var toDate = new Date(to);
         var now = new Date();
         now.setHours(24,0,0,0)

         //alert(fromDate);
         //alert(toDate);
         //alert(now);

         if (isNaN(fromDate.getTime()) || isNaN(toDate.getTime())) {
            alert("Invalid dates");
            return false;
         }

         if (fromDate>now || toDate>now) {
          alert("Cant select future dates");
          return false;
         }

         if (fromDate>toDate) {
          alert("FROM date greater than TO date");
          return false;
         }

        //if all passed
         document.filterForm.submit();
      }

        function validate() {
                var season = document.getElementById('season_id');
                var datePicker = document.getElementById('datePicker');
                //alert(season.value);
                if (season.value==0) {
                    datePicker.style.display = 'block';
                    document.getElementById('fromDate').disabled = false;
                    document.getElementById('toDate').disabled = false;
                }
                else{
                    datePicker.style.display = 'none';
                    document.getElementById('fromDate').disabled = true;
                    document.getElementById('toDate').disabled = true;
                    document.filterForm.submit();
                }
        }
      </script>
    <head>
        <title>Leaderboard</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
    <body>
        <button onClick="window.location.reload();">Refresh</button>
        <input type="button" value="Back" onclick="location.href = document.referrer; return false;"/>
        <input type="button" onclick="location.href='/'" value="Back to Main"/>
         <h2>Filter By</h2>
                                        <form name="filterForm" action="/leaderboard" method="get">
                                                <select id="season_id" name="season_id" onchange="validate()">
                                                    <#list seasons as s>
                                                        <option value="${s.season_id}" <#if s.season_id==season_id>selected</#if>>${s.name}</option>
                                                    </#list>
                                                        <option value="-1" <#if -1==season_id>selected</#if>>All time</option>
                                                        <option value="0"  <#if 0==season_id>selected</#if>>Custom</option>
                                                </select>
                                                <br><br>
                                                <div id="datePicker" <#if 0==season_id>style="display:block;"<#else>style="display:none;"</#if>>
                                                <label for="fromDate">From: </label> <input type="date" id="fromDate" name="fromDate" <#if fromDate?has_content>value="${fromDate}"</#if>>
                                                    <label for="toDate">To: </label> <input type="date" id="toDate" name="toDate" <#if toDate?has_content>value="${toDate}"</#if>>
                                                       <button onclick="return validateCustomDates()">Go</button>
                                                </div>
                                       </form>
                      <table>
                      <tr>
                      <td>
                           <h2>Points table</h2>
                          <table border="5">
                              <tr>
                                  <th>Player</th>
                                  <th>Attendance pts</th>
                                  <th>Played pts</th>
                                  <th>Win pts</th>
                                  <th>Deuce pts</th>
                                  <th>Team change pts</th>
                                  <th>Total pts</th>
                              </tr>
                            <#list players_pts as pStats>
                                <tr>
                                    <td><a href="playerStats?player_id=${pStats.player.player_id}">${pStats.player.name}<a/></td>
                                    <td>${pStats.fMap.matchday_pts.total}</td>
                                    <td>${pStats.fMap.played_pts.total}</td>
                                    <td>${pStats.fMap.won_pts.total}</td>
                                    <td>${pStats.fMap.deuce_pts.total}</td>
                                    <td>${pStats.fMap.teamchange_pts.total}</td>
                                    <td>${pStats.fMap.total_pts.total}</td>
                                </tr>
                            </#list>
                                  <th>Team</th>
                                  <th>Attendance pts</th>
                                  <th>Played pts</th>
                                  <th>Win pts</th>
                                  <th>Deuce pts</th>
                                  <th>Team change pts</th>
                                  <th>Total pts</th>
                              </tr>
                            <#list teams_pts as tStats>
                                <tr>
                                    <td> <a href="teamStats?team_id=${tStats.team.team_id}" title="${tStats.team.p1.name} & ${tStats.team.p2.name}"}>Team ${tStats.team.name}</a>
                                    <font size="-1">
                                    (<a href="playerStats?player_id=${tStats.team.p1.player_id}">${tStats.team.p1.name}</a><#if tStats.team.p1.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>
                                     &
                                     <a href="playerStats?player_id=${tStats.team.p2.player_id}">${tStats.team.p2.name}</a><#if tStats.team.p2.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>)
                                     </font>
                                    </td>
                                    <td>${tStats.fMap.matchday_pts.total}</td>
                                    <td>${tStats.fMap.played_pts.total}</td>
                                    <td>${tStats.fMap.won_pts.total}</td>
                                    <td>${tStats.fMap.deuce_pts.total}</td>
                                    <td>N/A</td>
                                    <td>${tStats.fMap.total_pts.total}</td>
                                </tr>
                            </#list>
                          </table>
                       </td>
                       <td style="vertical-align:top">
                            <h2>Points system</h2>
                                 <table border="5">
                                    <tr>
                                        <th>Rule</th>
                                        <th>Points</th>
                                    </tr>
                                    <#list pointsConfig as pConfig>
                                        <tr>
                                            <td>${pConfig.rule}</td>
                                            <td>${pConfig.multiplier}</td>
                                        </tr>
                                    </#list>
                                </table>
                       </td>
                       </tr>
                       </table>
     <h2>Stats table</h2>
                  <table border="5">
                      <tr>
                          <th>Player</th>
                          <th>Played</th>
                          <th>Won</th>
                          <th>Lost</th>
                          <th>Win %</th>
                      </tr>
                    <#list playerStats as pStats>
                        <tr>
                            <td><a href="playerStats?player_id=${pStats.player.player_id}">${pStats.player.name}<a/></td>
                            <td>${pStats.played}</td>
                            <td>${pStats.won}</td>
                            <td>${pStats.lost}</td>
                            <td>${pStats.getWinRatio()}</td>
                        </tr>
                    </#list>
                      <tr>
                          <th>Team</th>
                          <th>Played</th>
                          <th>Won</th>
                          <th>Lost</th>
                          <th>Win %</th>
                      </tr>
                        <#list teamStats as tStats>
                        <tr>
                            <td> <a href="teamStats?team_id=${tStats.team.team_id}" title="${tStats.team.p1.name} & ${tStats.team.p2.name}"}>Team ${tStats.team.name}</a>
                                 <font size="-1">
                                 (<a href="playerStats?player_id=${tStats.team.p1.player_id}">${tStats.team.p1.name}</a><#if tStats.team.p1.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>
                                 &
                                 <a href="playerStats?player_id=${tStats.team.p2.player_id}">${tStats.team.p2.name}</a><#if tStats.team.p2.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>)
                                 </font>
                            </td>
                            <td>${tStats.played}</td>
                            <td>${tStats.won}</td>
                            <td>${tStats.lost}</td>
                            <td>${tStats.getWinRatio()}</td>
                        </tr>
                    </#list>
                  </table>

    </body>
</html>