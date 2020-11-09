<!DOCTYPE html>
<html>
<script>
 function validateform(){

    var p1=document.teamForm.p1;
    var p2=document.teamForm.p2;

    var p1_name = p1.options[p1.selectedIndex].text;
    var p2_name = p2.options[p2.selectedIndex].text;

    //player validation
    if (p1.value==0){
      alert("Player1 not selected");
      return false;
    }

    if (p2.value==0){
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
        <title>New Team</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
    <style>
               th {
                    background-color: #DCDCDC
                }
    </style>
    <body>
    <input type="button" value="Back" onclick="location.href = document.referrer; return false;"/>
    <input type="button" onclick="location.href='/'" value="Back to Main"/>
     <h2>Team Info</h2>
             <form name="teamForm" onsubmit="return validateform()" action="/newTeam" method="post">
               <table border="5">
               <tr>
                         <td>
                         <br>
                         <label for="p1">Player1:</label><br>
                         <select id="p1" name="p1">
                             <option value="0">Select Player</option>
                             <#list players as p>
                                <option value="${p.player_id}">${p.name}</option>
                             </#list>
                         </select><br><br>

                         <label for="p2">Player2:</label><br>
                         <select id="p2" name="p2">
                            <option value="0">Select Player</option>
                             <#list players as p>
                                <option value="${p.player_id}">${p.name}</option>
                             </#list>
                         </select><br><br>

                         <label for="name">Team Name:</label><br>
                         <input type="text" id="name" name="name"><br><br>

                         <label for="name">Playing Style:</label><br>
                         <input type="text" id="playing_style" name="playing_style"><br><br>

                         <label for="name">Signature moves:</label><br>
                         <input type="text" id="signature_moves" name="signature_moves""><br><br>

                         <label for="name">Other Names:</label><br>
                         <input type="text" id="alias" name="alias"><br><br>
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