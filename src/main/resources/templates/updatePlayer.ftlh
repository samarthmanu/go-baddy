<!DOCTYPE html>
<html>
<script type="text/javascript">
    onload=function(){
        if(!!window.performance && window.performance.navigation.type == 2)
        {
            window.location.reload();
        }
    }
function validateInvalidateForm(){
    var current_status=document.invalidateForm.invalidate.checked;
    current_status=!current_status; //validity=opposite of invalidate flag
    var new_status=!current_status;

    //alert(current_status);
    //alert(new_status);

    var r = confirm("Changing player validity from [" + current_status + "] to [" + new_status + "]\n\nProceed?");
      if (r == false) {
           return false;
      }
}
</script>
    <head>
        <title>Update Player</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
    <body>
    <button onClick="window.location.reload();">Refresh</button>
    <input type="button" value="Back" onclick="location.href = document.referrer; return false;"/>
    <input type="button" onclick="location.href='/'" value="Back to Main"/>
    <!--
    <h2>Select Player</h2>
        <select onchange="location = this.value;">
            <#list players as p>
                <option value="updatePlayer?id=${p.player_id}" <#if p.player_id==player.player_id>selected</#if>>${p.name}</option>
            </#list>
        </select>
     <br><br>
     -->
     <h2>Player Info</h2>
     <#if player.invalidate==true><p style="color:red">This player is currently marked as INVALID</p></#if>
               <table border="5">
               <tr>
                        <td colspan="3" align="center">
                        <br>
                        <form name="invalidateForm" onsubmit="return validateInvalidateForm()" action="/invalidatePlayer" method="post">
                            <label for="player_id">Id:</label><br>
                            <input type="text" id="player_id" name="player_id" value="${player.player_id}" readonly="readonly" style="background-color: lightgrey;"><br><br>
                            <input type="checkbox" id="invalidate" name="invalidate" hidden="true" <#if player.invalidate==true>checked</#if>>
                            <input type="submit" value="<#if player.invalidate==true>Mark player as VALID<#else>Mark player as INVALID</#if>">
                         </form><br><br>
                       </td>
                </tr>

               <tr>
                    <td>
                    <form name="playerForm" onsubmit="return validateform()" action="/updatePlayer" method="post">
                         <fieldset style="display:inline-block;border: none;" <#if player.invalidate==true>disabled="disabled"</#if>>
                         <input type="text" id="player_id" name="player_id" value="${player.player_id}" hidden="true">
                         <br>
                         <label for="name">Name:</label><br>
                         <input type="text" id="name" name="name" value="${player.name}"><br><br>

                         <label for="name">Playing Style:</label><br>
                         <input type="text" id="playing_style" name="playing_style" value="${player.playing_style}"><br><br>

                         <label for="name">Signature moves:</label><br>
                         <input type="text" id="signature_moves" name="signature_moves" value="${player.signature_moves}"><br><br>

                         <label for="name">Other Names:</label><br>
                         <input type="text" id="alias" name="alias" value="${player.alias}"><br><br>

                         <label for="name">Contact:</label><br>
                         <input type="text" id="contact" name="contact" value="${player.contact}"><br><br>

                          <div align="center">
                          <br>
                          <input type="submit" value="Save">  <input type="reset" value="Reset">
                          <br>
                          </div>
                    </fieldset>
                    </form>
                    </td>
               </tr>
             </table>
    </body>
</html>