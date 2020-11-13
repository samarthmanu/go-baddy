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
             <link rel="stylesheet" href="dist/sortable-tables.min.css">
            <script src="dist/sortable-tables.min.js" type="text/javascript"></script>
    </head>
    <style>
               th {
                    background-color: #DCDCDC
                }
    </style>
    <body>
    <#noautoesc>
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
                    <td>${player.name} <#if player.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if></td>
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
                                        <td>${pStats.player.getNameAsLink((season_id)!-1, (fromDate)!"NA", (toDate)!"NA")}</td>
                                        <td>${pStats.played}</td>
                                        <td>${pStats.won}</td>
                                        <td>${pStats.lost}</td>
                                        <td>${pStats.getWinRatio()}</td>
                                    </tr>
                                    <#list teamStats as tStats>
                                        <tr>
                                            <td>${tStats.team.getNameAsLink((season_id)!-1, (fromDate)!"NA", (toDate)!"NA")}</td>
                                            <td>${tStats.played}</td>
                                            <td>${tStats.won}</td>
                                            <td>${tStats.lost}</td>
                                            <td>${tStats.getWinRatio()}</td>
                                        </tr>
                                    </#list>
                                    </tbody>
                                </table>
        <h2>Player Points</h2>

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
                                    <#list pStats.fMap?keys as key>
                                        <#if key=='total_pts'><#continue></#if>
                                            <tr>
                                                <td>${pStats.fMap[key].desc}</td>
                                                <td>${pStats.fMap[key].multiplier}</td>
                                                <td>${pStats.fMap[key].count}</td>
                                                <td>${pStats.fMap[key].total}</td>
                                            </tr>
                                        </#list>
                                    </tbody>
                                        <tr>
                                            <th colspan=3 align="left">TOTAL</th>
                                            <th align="left">${pStats.fMap.total_pts.total}</th>
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
                            <td>${game.getGame_idAsLinktoUpdateMatch()}</td>
                            <td>${game.getPlayedOn_IST()}</td>
                            <td>${game.getTeam1_or_playersAsLink((season_id)!-1, (fromDate)!"NA", (toDate)!"NA")}</td>
                            <td>${game.score1}</td>
                            <td>${game.getTeam2_or_playersAsLink((season_id)!-1, (fromDate)!"NA", (toDate)!"NA")}</td>
                            <td>${game.score2}</td>
                            <td>${game.getResult(player)}</td>
                         </tr>
                      </#list>
                   </tbody>
                </table>
        </#noautoesc>
    </body>
</html>