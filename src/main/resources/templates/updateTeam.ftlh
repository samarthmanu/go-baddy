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

    var r = confirm("Permanently deleting team\nProceed?");
      if (r == false) {
           return false;
      }
}

 function validateform(){

    var p1=document.submitForm.p1;
    var p2=document.submitForm.p2;

    var p1_name = p1.options[p1.selectedIndex].text;
    var p2_name = p2.options[p2.selectedIndex].text;

    //player validation
    if (p1.value==null || p1.value=="" || p1.value=="Select Player"){
      alert("Player1 not selected");
      return false;
    }

    if (p2.value==null || p2.value=="" || p2.value=="Select Player"){
      alert("Player2 not selected");
      return false;
    }

    //duplicate players check
    if (p1.value==p2.value){
      alert(p1_name + " selected twice or more");
      return false;
    }
}
</script>
    <head>
        <title>Update Team</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
   <!--
   <h2>Select Team</h2>
       <select onchange="location = this.value;">
           <#list teams as t>
               <option value="updateTeam?id=${t.team_id}" <#if t.team_id==team.team_id>selected</#if>>${t.name}</option>
           </#list>
       </select>
     <br><br>
     <h2>Team Info</h2>-->
     <br><br>
               <table border="5">
               <tr>
                    <th>Update Team</th>
               </tr>
               <tr>
                                <td align="center">
                                <br>
                                <form name="invalidateForm" onsubmit="return validateInvalidateForm()" action="/invalidateTeam" method="post">
                                           <label for="team_id">Id:</label><br>
                                           <input type="text" id="team_id" name="team_id" value="${team.team_id}" readonly="readonly" style="background-color: lightgrey;">
                                           <br><br>
                                           <div align="center"><input type="submit" value="Delete Team"></div>
                                           <br>
                                </form>
                                </td>
               </tr>
               <tr>
                         <td align="center">
                        <form name="teamForm" onsubmit="return validateform()" action="/updateTeam" method="post">
                         <input type="text" id="team_id" name="team_id" value="${team.team_id}" hidden="true">
                         <br>
                         <label for="p1">Player1:</label><br>
                         <select id="p1" name="p1" disabled>
                             <#list players as p>
                                <option value="${p.player_id}" <#if p.player_id==team.p1.player_id>selected</#if>>${p.name}</option>
                             </#list>
                         </select><br><br>

                         <label for="p2">Player2:</label><br>
                         <select id="p2" name="p2" disabled>
                             <#list players as p>
                                <option value="${p.player_id}" <#if p.player_id==team.p2.player_id>selected</#if>>${p.name}</option>
                             </#list>
                         </select><br><br>

                         <label for="name">Team Name:</label><br>
                         <input type="text" id="name" name="name" value="${team.name}"><br><br>

                         <label for="name">Playing Style:</label><br>
                         <input type="text" id="playing_style" name="playing_style" value="${team.playing_style}"><br><br>

                         <label for="name">Signature moves:</label><br>
                         <input type="text" id="signature_moves" name="signature_moves" value="${team.signature_moves}"><br><br>

                         <label for="name">Other Names:</label><br>
                         <input type="text" id="alias" name="alias" value="${team.alias}"><br><br>
                         </td>
               </tr>

               <tr>
                        <td align="center">
                        <br>
                        <input type="submit" value="Save">
                        <input type="reset" value="Reset">
                        <br><br>
                        </td>
               </tr>
               </form>
             </table>
    </body>
</html>