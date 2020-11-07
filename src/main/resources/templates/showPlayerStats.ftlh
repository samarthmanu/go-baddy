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
        <title>Player Stats</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
    <button onClick="window.location.reload();">Refresh</button>
    <input type="button" value="Back" onclick="location.href = document.referrer; return false;"/>
    <input type="button" onclick="location.href='/'" value="Back to Main"/>
    <br><br>
    <input type="button" onclick="location.href='/updatePlayer?id=${player.player_id}'" value="Update Player"/>
    <input type="button" onclick="location.href='/newPlayer'" value="New Player"/>
                    <h2>Select Player</h2>
                    <form name="filterForm" action="/playerStats" method="get">
                            <select id="player_id" name="player_id" onchange="this.form.submit()">
                                    <#list players as p>
                                        <option value="${p.player_id}" <#if p.player_id==player.player_id>selected</#if>>${p.name}</option>
                                    </#list>
                            </select>
                          <h2>Filter By</h2>
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
          <h2>Player Info</h2>
            <table border="5">
                <tr>
                    <th>Name</th>
                    <td>${player.name} <#if player.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if></td></td>
                </tr>
                <tr>
                    <th>Joined</th>
                    <td>${player.getCreatedOn_IST()}</td>
                </tr>
                <tr>
                    <th>Playing Style</th>
                    <td>${player.playing_style}</td>
                </tr>
                <tr>
                    <th>Signature moves</th>
                    <td>${player.signature_moves}</td>
                </tr>
                <tr>
                    <th>Other names</th>
                    <td>${player.alias}</td>
                </tr>
                <tr>
                    <th>Contact</th>
                    <td>${player.contact}</td>
                </tr>
            </table>
          <h2>Form Guide</h2>
            <table border="5">
                <tr>
                    <th>Last 5 games (-->)</th>
                    <td>${pStats.recentForm}</td>
                </tr>
                <tr>
                    <th>Current Streak</th>
                    <td>${pStats.currentStreak}</td>
                </tr>
            </table>
        <h2>Player & Team Stats</h2>

                                 <table border="5">
                                    <tr>
                                        <th>Name</th>
                                        <th>Played</th>
                                        <th>Won</th>
                                        <th>Lost</th>
                                        <th>Win %</th>
                                    </tr>
                                    </tr>
                                        <td><a href="playerStats?player_id=${pStats.player.player_id}">${pStats.player.name}</a> <#if pStats.player.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if></td>
                                        <td>${pStats.played}</td>
                                        <td>${pStats.won}</td>
                                        <td>${pStats.lost}</td>
                                        <td>${pStats.getWinRatio()}</td>
                                    </tr>
                                    <#list teamStats as tStats>
                                        <tr>
                                            <td>
                                             <a href="teamStats?team_id=${tStats.team.team_id}" title="${tStats.team.p1.name} & ${tStats.team.p2.name}">Team ${tStats.team.name}</a>
                                             <font size="-1">
                                             (<a href="playerStats?player_id=${tStats.team.p1.player_id}">${tStats.team.p1.name}</a><#if tStats.team.p1.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>
                                              &
                                              <a href="playerStats?player_id=${tStats.team.p2.player_id}">${tStats.team.p2.name}</a><#if tStats.team.p2.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>)
                                              </font>
                                            <td>${tStats.played}</td>
                                            <td>${tStats.won}</td>
                                            <td>${tStats.lost}</td>
                                            <td>${tStats.getWinRatio()}</td>
                                        </tr>
                                    </#list>
                                </table>
        <h2>Player Points</h2>

                                 <table border="5">
                                    <tr>
                                        <th>Rule</th>
                                        <th>Points</th>
                                        <th>Count</th>
                                        <th>Accumulated Pts</th>
                                    </tr>
                                    <#list pStats.fMap?keys as key>
                                    <#if key=='total_pts'><#continue></#if>
                                        <tr>
                                            <td>${pStats.fMap[key].desc}</td>
                                            <td>${pStats.fMap[key].multiplier}</td>
                                            <td>${pStats.fMap[key].count}</td>
                                            <td>${pStats.fMap[key].total}</td>
                                        </tr>
                                    </#list>
                                        <tr>
                                            <th colspan=3 align="left">TOTAL</th>
                                            <th align="left">${pStats.fMap.total_pts.total}</th>
                                        </tr>
                                </table>
        <h2>Match History</h2>
                <table border="5">
                    <tr>
                        <th>Id</th>
                        <th>Played on</th>
                        <th>Team1</th>
                        <th>Score1</th>
                        <th>Team2</th>
                        <th>Score2</th>
                        <th>Result</th>
                    </tr>
                   <#list games as game>
                                           <tr>
                                               <td><a href="updateMatch?id=${game.game_id}" title="Click to update match details">${game.game_id}</a>
                                               </td>
                                               <td>${game.getPlayedOn_IST()}</td>
                                               <td>
                                                   <#if game.t1?has_content>
                                                       <a href="teamStats?team_id=${game.t1.team_id}" title="${game.t1.p1.name} & ${game.t1.p2.name}">Team ${game.t1.name}</a>
                                                         <font size="-1">
                                                         (<a href="playerStats?player_id=${game.t1.p1.player_id}">${game.t1.p1.name}</a><#if game.t1.p1.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>
                                                          &
                                                          <a href="playerStats?player_id=${game.t1.p2.player_id}">${game.t1.p2.name}</a><#if game.t1.p2.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>)
                                                          </font>
                                                   <#else>
                                                       <a href="playerStats?player_id=${game.p1.player_id}">${game.p1.name}</a> <#if game.p1.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>
                                                        &
                                                        <a href="playerStats?player_id=${game.p2.player_id}">${game.p2.name}</a> <#if game.p2.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>
                                                   </#if>
                                               </td>
                                               <td>${game.score1}</td>
                                               <td>
                                                   <#if game.t2?has_content>
                                                       <a href="teamStats?team_id=${game.t2.team_id}" title="${game.t2.p1.name} & ${game.t2.p2.name}">Team ${game.t2.name}</a>
                                                       <font size="-1">
                                                       (<a href="playerStats?player_id=${game.t2.p1.player_id}">${game.t2.p1.name}</a><#if game.t2.p1.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>
                                                        &
                                                        <a href="playerStats?player_id=${game.t2.p2.player_id}">${game.t2.p2.name}</a><#if game.t2.p2.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>)
                                                        </font>
                                                   <#else>
                                                       <a href="playerStats?player_id=${game.p3.player_id}">${game.p3.name}</a> <#if game.p3.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>
                                                        &
                                                        <a href="playerStats?player_id=${game.p4.player_id}">${game.p4.name}</a> <#if game.p4.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>
                                                   </#if>
                                               </td>
                                               <td>${game.score2}</td>
                                               <td>${game.getResult(player)}</td>
                                               </td>
                                           </tr>
                      </#list>
                </table>
    </body>
</html>