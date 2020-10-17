<!DOCTYPE html>
<html>
    <head>
        <title>Leaderboard</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
    <body>
        <button onClick="window.location.reload();">Refresh</button>
        <input type="button" value="Back" onclick="location.href = document.referrer; return false;"/>
        <input type="button" onclick="location.href='/'" value="Back to Main"/>
    <h2>Player Leaderboard</h2>
                  <table border="5">
                      <tr>
                          <th>Player</th>
                          <th>Played</th>
                          <th>Win %</th>
                      </tr>
                    <#list players as pStat>
                        <tr>
                            <td>${pStat.player.name}</td>
                            <td>${pStat.played}</td>
                            <td>${pStat.ratio}</td>
                        </tr>
                    </#list>
                  </table>
           <h2>Team Leaderboard</h2>
                  <table border="5">
                      <tr>
                          <th>Team</th>
                          <th>Played</th>
                          <th>Win %</th>
                      </tr>
                        <#list teams as tStat>
                        <tr>
                            <td>Team ${tStat.team.name} <#if (tStat.team.p1.invalidate==true||tStat.team.p2.invalidate==true)><img src="images/icon-flag.jpg" height="10" width="10" title="This team has one/more invalidated players"></#if></td>
                            <td>${tStat.played}</td>
                            <td>${tStat.ratio}</td>
                        </tr>
                    </#list>
                  </table>
      <!--todo
        <h2>Hall of Fame</h2>
                <table border="5">
                    <tr>
                        <th>Record</th>
                        <th>Holder</th>
                    </tr>
                    <#list fameRecords as record>
                        <tr>
                            <td>${record.desc}</td>
                            <td>${record.holder}</td>
                        </tr>
                    </#list>
               </table>
        <h2>Hall of Shame</h2>
                <table border="5">
                    <tr>
                        <th>Record</th>
                        <th>Holder</th>
                    </tr>
                    <#list shameRecords as record>
                        <tr>
                            <td>${record.desc}</td>
                            <td>${record.holder}</td>
                        </tr>
                    </#list>
               </table>
        -->
    </body>
</html>