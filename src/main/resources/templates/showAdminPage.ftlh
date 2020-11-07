<!DOCTYPE html>
<html>
<script type="text/javascript">
    onload=function(){
        if(!!window.performance && window.performance.navigation.type == 2)
        {
            window.location.reload();
        }
    }
</script>
    <head>
        <title>Admin Settings</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
    <body>
    <button onClick="window.location.reload();">Refresh</button>
    <input type="button" value="Back" onclick="location.href = document.referrer; return false;"/>
    <input type="button" onclick="location.href='/'" value="Back to Main"/>
     <h2>Admin settings</h2>
               <table border="5">
              <tr>
                  <form name="adminForm1" action="/toggleMatchLock" method="post">
                        <td>
                        <br>
                            <input type="checkbox" id="match_lock" name="match_lock" hidden="true" <#if adminConfig.match_lock==true>checked</#if>>
                            <label for="submit1">
                            Match Update/Creation Lock (Currently <#if adminConfig.match_lock==true><font color="green">ON</font><#else><font color="red">OFF</font></#if>)</label>
                            <br><br>
                       </td>
                       <td>
                           <br>
                               <input type="submit" id="submit1" value="<#if adminConfig.match_lock==true>TURN OFF<#else>TURN ON</#if>">
                           <br><br>
                       </td>
              </form>
              </tr
             </table>
    </body>
</html>