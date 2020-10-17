<!DOCTYPE html>
<html>
    <head>
        <title>Team Stats</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
    <body>
        <button onClick="window.location.reload();">Refresh</button>
        <input type="button" value="Back" onclick="location.href = document.referrer; return false;"/>
        <input type="button" onclick="location.href='/'" value="Back to Main"/>
    <h2>Select Team</h2>
    <select onchange="location = this.value;">
        <#list teams as t>
            <option value="teamStats?id=${t.team_id}" <#if t.team_id==team.team_id>selected</#if>>Team ${t.name}</option>
        </#list>
    </select>
    <br><br>
    <input type="button" onclick="location.href='/updateTeam?id=${team.team_id}'" value="Update Team"/>
    <input type="button" onclick="location.href='/newTeam'" value="New Team"/>
          <h2>Team Info:</h2>
            <table border="5">
                <tr>
                    <th>Name</th>
                    <td>Team ${team.name}</td>
                </tr>
               <tr>
                    <th>Players</th>
                    <td>
                    <a href="playerStats?id=${team.p1.player_id}">${team.p1.name} <#if team.p1.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if></a>
                     &
                     <a href="playerStats?id=${team.p2.player_id}">${team.p2.name}</a> <#if team.p2.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>
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
           <h2>Player Stats</h2>
                  <table border="5">
                      <th colspan="2">
                          <a href="playerStats?id=${p1Stat.player.player_id}">${p1Stat.player.name}</a> <#if p1Stat.player.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>
                      </th>
                       <tr>
                          <th>Played</th>
                           <td>${p1Stat.played}</td>
                      </tr>
                      <tr>
                          <th>Won</th>
                          <td>${p1Stat.won}</td>
                      </tr>
                      <tr>
                          <th>Win %</th>
                          <td>${p1Stat.ratio}</td>
                      </tr>
                  </table>
           <br><br>
                  <table border="5">
                      <th colspan="2">
                          <a href="playerStats?id=${p2Stat.player.player_id}">${p2Stat.player.name}</a> <#if p2Stat.player.invalidate==true><img src="images/icon-flag.jpg" height="10" width="10" title="Player Invalidated"></#if>
                      </tr>
                       <tr>
                          <th>Played</th>
                           <td>${p2Stat.played}</td>
                      </tr>
                      <tr>
                          <th>Won</th>
                          <td>${p2Stat.won}</td>
                      </tr>
                      <tr>
                          <th>Win %</th>
                          <td>${p2Stat.ratio}</td>
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
                                               <td>${game.getResult(team)}</td>
                                               </td>
                                           </tr>
                      </#list>
                </table>
    </body>
</html>