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
        <title>Team Stats</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="dist/sortable-tables.min.css">
        <script src="dist/sortable-tables.min.js" type="text/javascript"></script>
    </head>
    <style>
               th {
                    background-color: #DCDCDC
                }
    </style>
    <body>
        <button onClick="window.location.reload();">Refresh</button>
        <input type="button" value="Back" onclick="location.href = document.referrer; return false;"/>
        <input type="button" onclick="location.href='/'" value="Back to Main"/>
        <br><br>
            <input type="button" onclick="location.href='/updateTeam?id=${team.team_id}'" value="Update Team"/>
            <input type="button" onclick="location.href='/newTeam'" value="New Team"/>
    <h2>Select Team</h2>
                                <form name="filterForm" action="/teamStats" method="get">
                                    <select id="team_id" name="team_id" onchange="this.form.submit()">
                                        <#list teams as t>
                                            <option value="${t.team_id}" <#if t.team_id==team.team_id>selected</#if>>Team ${t.name}</option>
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
          <h2>Team Info</h2>
            <table border="5">
                <tr>
                    <th>Name</th>
                    <td>Team ${team.name}</td>
                </tr>
               <tr>
                    <th>Players</th>
                    <td>
                    <a href="playerStats?player_id=${team.p1.player_id}">${team.p1.name} <#if team.p1.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if></a>
                     &
                     <a href="playerStats?player_id=${team.p2.player_id}">${team.p2.name}</a> <#if team.p2.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>
                     </td>
                </tr>
                <tr>
                    <th>Joined</th>
                    <td>${team.getCreatedOn_IST()}</td>
                </tr>
                <tr>
                    <th>Playing Style</th>
                    <td>${team.playing_style}</td>
                </tr>
                <tr>
                    <th>Signature moves</th>
                    <td>${team.signature_moves}</td>
                </tr>
                <tr>
                    <th>Other names</th>
                    <td>${team.alias}</td>
                </tr>
            </table>
            <h2>Form Guide</h2>
                        <table border="5">
                            <tr>
                                <th>Last 5 games (-->)</th>
                                <td>${tStats.recentForm}</td>
                            </tr>
                            <tr>
                                <th>Current Streak</th>
                                <td>${tStats.currentStreak}</td>
                            </tr>
                        </table>
            <h2>Team & Player Stats</h2>
                                 <table border="5" class="sortable-table">
                                 <thead>
                                    <tr>
                                        <th>Name</th>
                                        <th class="numeric-sort">Played</th>
                                        <th class="numeric-sort">Won</th>
                                        <th class="numeric-sort">Lost</th>
                                        <th class="numeric-sort">Win %</th>
                                    </tr>
                                 </thead>
                                 <tbody>
                                    </tr>
                                         <td><a href="teamStats?team_id=${tStats.team.team_id}" title="${tStats.team.p1.name} & ${tStats.team.p2.name}">Team ${tStats.team.name}</a>
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
                                    <#list playerStats as pStats>
                                        <tr>
                                        <td><a href="playerStats?player_id=${pStats.player.player_id}">${pStats.player.name}</a> <#if pStats.player.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if></td>
                                        <td>${pStats.played}</td>
                                        <td>${pStats.won}</td>
                                        <td>${pStats.lost}</td>
                                        <td>${pStats.getWinRatio()}</td>
                                        </tr>
                                    </#list>
                                 </tbody>
                                </table>
                            <h2>Team Points</h2>
                                 <table border="5" class="sortable-table">
                                    <thead>
                                    <tr>
                                        <th>Rule</th>
                                        <th class="numeric-sort">Points</th>
                                        <th class="numeric-sort">Count</th>
                                        <th class="numeric-sort">Accumulated Pts</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <#list tStats.fMap?keys as key>
                                    <#if key=='total_pts'><#continue></#if>
                                        <tr>
                                            <td>${tStats.fMap[key].desc}</td>
                                            <td>${tStats.fMap[key].multiplier}</td>
                                            <td>${tStats.fMap[key].count}</td>
                                            <td>${tStats.fMap[key].total}</td>
                                        </tr>
                                    </#list>
                                    </tbody>
                                        <tr>
                                            <th colspan=3 align="left">TOTAL</th>
                                            <th align="left">${tStats.fMap.total_pts.total}</th>
                                        </tr>
                                </table>
            <h2>Match History</h2>
                <table border="5" class="sortable-table">
                    <thead>
                    <tr>
                        <th class="numeric-sort">Id</th>
                        <th>Played on</th>
                        <th>Team1</th>
                        <th class="numeric-sort">Score1</th>
                        <th>Team2</th>
                        <th class="numeric-sort">Score2</th>
                        <th>Result</th>
                    </tr>
                    </thead>
                    <tbody>
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
                                               <td>${game.getResult(team)}</td>
                                               </td>
                                           </tr>
                      </#list>
                      </tbody>
                </table>
    </body>
</html>