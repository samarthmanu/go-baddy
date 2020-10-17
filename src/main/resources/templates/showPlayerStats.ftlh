<!DOCTYPE html>
<html>
    <head>
        <title>Player Stats</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
    <body>
    <button onClick="window.location.reload();">Refresh</button>
    <input type="button" value="Back" onclick="location.href = document.referrer; return false;"/>
    <input type="button" onclick="location.href='/'" value="Back to Main"/>
    <h2>Select Player</h2>
    <select onchange="location = this.value;">
        <#list players as p>
            <option value="playerStats?id=${p.player_id}" <#if p.player_id==player.player_id>selected</#if>>${p.name}</option>
        </#list>
    </select>
    <br><br>
    <input type="button" onclick="location.href='/updatePlayer?id=${player.player_id}'" value="Update Player"/>
    <input type="button" onclick="location.href='/newPlayer'" value="New Player"/>
          <h2>Player Info</h2>
            <table border="5">
                <th colspan="2">
                 ${player.name} <#if player.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if></td>
                </th>
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
        <h2>Player Stats</h2>
        <table border="5">
            <tr>
                <th>Played</th>
                <td>${pStat.played}</td>
            </tr>
            <tr>
                <th>Won</th>
                <td>${pStat.won}</td>
            </tr>
            <tr>
                <th>Win %</th>
                <td>${pStat.ratio}</td>
            </tr>
        </table>
                <h2>Associated Teams</h2>
                  <table border="5">
                                    <tr>
                                        <th>Name</th>
                                        <th>Players</th>
                                        <th>Played</th>
                                        <th>Won</th>
                                        <th>Win %</th>
                                    </tr>
                                    <#list teams as tStat>
                                        <tr>
                                            <td><a href="teamStats?id=${tStat.team.team_id}">Team ${tStat.team.name}</a></td>
                                            <td>
                                            <a href="playerStats?id=${tStat.team.p1.player_id}">${tStat.team.p1.name}</a> <#if tStat.team.p1.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>
                                             &
                                             <a href="playerStats?id=${tStat.team.p2.player_id}">${tStat.team.p2.name}</a> <#if tStat.team.p2.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>
                                             </td>
                                            <td>${tStat.played}</td>
                                            <td>${tStat.won}</td>
                                            <td>${tStat.ratio}</td>
                                        </tr>
                                    </#list>
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
                                                       <a href="teamStats?id=${game.t1.team_id}">Team ${game.t1.name}</a>
                                                   <#else>
                                                       <a href="playerStats?id=${game.p1.player_id}">${game.p1.name}</a> <#if game.p1.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>
                                                        &
                                                        <a href="playerStats?id=${game.p2.player_id}">${game.p2.name}</a> <#if game.p2.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>
                                                   </#if>
                                               </td>
                                               <td>${game.score1}</td>
                                               <td>
                                                   <#if game.t2?has_content>
                                                       <a href="teamStats?id=${game.t2.team_id}">Team ${game.t2.name}</a>
                                                   <#else>
                                                       <a href="playerStats?id=${game.p3.player_id}">${game.p3.name}</a> <#if game.p3.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>
                                                        &
                                                        <a href="playerStats?id=${game.p4.player_id}">${game.p4.name}</a> <#if game.p4.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>
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