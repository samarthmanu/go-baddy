<!DOCTYPE html>
<html>
<script>
function changeTeam1(){
    var t1=document.scoreForm.t1;
    if(t1.value>0){
        document.scoreForm.p1.selectedIndex=0;
        document.scoreForm.p2.selectedIndex=0;
    }
}

function changeTeam2(){
    var t2=document.scoreForm.t2;
    if(t2.value>0){
        document.scoreForm.p3.selectedIndex=0;
        document.scoreForm.p4.selectedIndex=0;
    }
}

function validateform(){

       var p1=document.scoreForm.p1;
       var p2=document.scoreForm.p2;
       var p3=document.scoreForm.p3;
       var p4=document.scoreForm.p4;

       var t1=document.scoreForm.t1;
       var t2=document.scoreForm.t2;

       var t1_name = t1.options[t1.selectedIndex].text;
       var t2_name = t2.options[t2.selectedIndex].text;

       var score1=parseInt(document.scoreForm.score1.value);
       var score2=parseInt(document.scoreForm.score2.value);

       var p1_name = p1.options[p1.selectedIndex].text;
       var p2_name = p2.options[p2.selectedIndex].text;
       var p3_name = p3.options[p3.selectedIndex].text;
       var p4_name = p4.options[p4.selectedIndex].text;

        //alert(p1.value);
        //alert(p2.value);
        //alert(p3.value);
        //alert(p4.value);
        //alert(t1.value);
        //alert(t2.value);

       //team1 validation
       if (t1.value==0){
          //player validation
           if (p1.value==0){
             alert("Player1/Team1 not selected");
             return false;
           }

           if (p2.value==0){
             alert("Player2/Team1 not selected");
             return false;
           }

       }

       //team2 validation
       if (t2.value==0){
           //player validation
           if (p3.value==0){
             alert("Player3/Team2 not selected");
             return false;
           }

           if (p4.value==0){
             alert("Player4/Team2 not selected");
             return false;
           }

       }

       //duplicate team check
        if (t1.value>0 && t1.value==t2.value){
             alert(t1_name + " selected twice");
             return false;
        }

       //duplicate players check
       if (t1.value==0 && p1.value>0 && (p1.value==p2.value || p1.value==p3.value || p1.value==p4.value)){
         alert(p1_name + " selected twice or more");
         return false;
       }

       if (t1.value==0 && p2.value>0 && (p2.value==p3.value || p2.value==p4.value)){
         alert(p2_name + " selected twice or more");
         return false;
       }

       if (t2.value==0 && p3.value>0 && (p3.value==p4.value)){
         alert(p3_name + " selected twice or more");
         return false;
       }

       //score validation
       if (score1>score2){
           var winning_score = parseInt(score1);
           var losing_score = parseInt(score2);
           if(t1.value>0){
                 var winning_team = t1_name;
           }else{
                  var winning_team = p1_name + " & " + p2_name;
           }
           if(t2.value>0){
                  var losing_team = t2_name;
           }else{
                  var losing_team = p3_name + " & " + p4_name;
           }
       }else if (score2>score1){
           var winning_score = parseInt(score2);
           var losing_score = parseInt(score1);
           if(t2.value>0){
                 var winning_team = t2_name;
           }else{
                  var winning_team = p3_name + " & " + p4_name;
           }
           if(t1.value>0){
                  var losing_team = t1_name;
           }else{
                  var losing_team = p1_name + " & " + p2_name;
           }
       }else{
           alert ("Invalid Score");
           return false;
       }

       //alert(winning_score);
       //alert(winning_team);
       //alert(losing_score);
       //alert(losing_team);

       if (!(winning_score>=21 && winning_score-losing_score>=2)){
           alert ("Invalid Score");
           return false;
       }else if(winning_score>21 && winning_score-losing_score!=2){
           alert ("Invalid Score");
           return false;
       }

       //final confirmation
       //var r = confirm("proceed?");
       var r = confirm(winning_team + " --> " + winning_score + " points.\nBEAT\n" + losing_team + " --> " + losing_score + " points.\n\nProceed to submit score?");
         if (r == false) {
           return false;
         }
   }
</script>
<head>
        <title>New Match</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
    <body>
    <button onClick="window.location.reload();">Refresh</button>
    <input type="button" value="Back" onclick="location.href = document.referrer; return false;"/>
    <input type="button" onclick="location.href='/'" value="Back to Main"/>
     <h2>New Match</h2>
             <form name="scoreForm" onsubmit="return validateform()" action="/newMatch" method="post">
               <table border="5">
               <tr>
                         <td align="center">
                         <br>
                         <label for="p1">Player1:</label><br>
                         <select id="p1" name="p1">
                         <option value="0">Select Player</option>
                         <option value="17">N/A</option>
                             <#list players as p>
                                <option value="${p.player_id}">${p.name}</option>
                             </#list>
                         </select><br><br>

                         <label for="p2">Player2:</label><br>
                         <select id="p2" name="p2">
                         <option value="0">Select Player</option>
                         <option value="17">N/A</option>
                             <#list players as p>
                                <option value="${p.player_id}">${p.name}</option>
                             </#list>
                         </select><br><br>
                        </td>

                        <td> (or) </td>
                        <td align="center">
                        <label for="t1">Team1:</label><br>
                        <select id="t1" name="t1" onchange="changeTeam1()">
                        <option value="0">Select Team</option>
                             <#list teams as t>
                                <option value="${t.team_id}">${t.name}</option>
                             </#list>
                         </select><br><br>
                        </td>
               </tr>
               <tr>
                        <td colspan="3" align="center">
                        <br>
                        <label for="score1">Score1:</label>
                             <select id="score1" name="score1">
                                <option value="0">0</option>
                                <option value="1">1</option>
                                <option value="2">2</option>
                                <option value="3">3</option>
                                <option value="4">4</option>
                                <option value="5">5</option>
                                <option value="6">6</option>
                                <option value="7">7</option>
                                <option value="8">8</option>
                                <option value="9">9</option>
                                <option value="10">10</option>
                                <option value="11">11</option>
                                <option value="12">12</option>
                                <option value="13">13</option>
                                <option value="14">14</option>
                                <option value="15">15</option>
                                <option value="16">16</option>
                                <option value="17">17</option>
                                <option value="18">18</option>
                                <option value="19">19</option>
                                <option value="20">20</option>
                                <option value="21" selected>21</option>
                                <option value="22">22</option>
                                <option value="23">23</option>
                                <option value="24">24</option>
                                <option value="25">25</option>
                                <option value="26">26</option>
                                <option value="27">27</option>
                                <option value="28">28</option>
                                <option value="29">29</option>
                                <option value="30">30</option>
                             </select>
                             <br><br>
                        </td>
               </tr>
                <tr>
                         <td align="center">
                         <br>
                         <label for="p3">Player3:</label><br>
                         <select id="p3" name="p3">
                            <option value="0">Select Player</option>
                            <option value="17">N/A</option>
                             <#list players as p>
                                <option value="${p.player_id}">${p.name}</option>
                             </#list>
                         </select><br><br>

                         <label for="p4">Player4:</label><br>
                         <select id="p4" name="p4">
                         <option value="0">Select Player</option>
                         <option value="17">N/A</option>
                             <#list players as p>
                                <option value="${p.player_id}">${p.name}</option>
                             </#list>
                         </select><br><br>
                        </td>

                        <td> (or) </td>
                        <td align="center">
                        <label for="t2">Team2:</label><br>
                        <select id="t2" name="t2" onchange="changeTeam2()">
                        <option value="0">Select Team</option>
                             <#list teams as t>
                                <option value="${t.team_id}">${t.name}</option>
                             </#list>
                         </select><br><br>
                        </td>
               </tr>
               <tr>
                        <td colspan="3" align="center">
                        <br>
                        <label for="score2">Score2:</label>
                             <select id="score2" name="score2">
                                <option value="0">0</option>
                                <option value="1">1</option>
                                <option value="2">2</option>
                                <option value="3">3</option>
                                <option value="4">4</option>
                                <option value="5">5</option>
                                <option value="6">6</option>
                                <option value="7">7</option>
                                <option value="8">8</option>
                                <option value="9">9</option>
                                <option value="10">10</option>
                                <option value="11">11</option>
                                <option value="12">12</option>
                                <option value="13">13</option>
                                <option value="14">14</option>
                                <option value="15">15</option>
                                <option value="16">16</option>
                                <option value="17">17</option>
                                <option value="18">18</option>
                                <option value="19">19</option>
                                <option value="20">20</option>
                                <option value="21" selected>21</option>
                                <option value="22">22</option>
                                <option value="23">23</option>
                                <option value="24">24</option>
                                <option value="25">25</option>
                                <option value="26">26</option>
                                <option value="27">27</option>
                                <option value="28">28</option>
                                <option value="29">29</option>
                                <option value="30">30</option>
                             </select>
                             <br><br>
                        </td>
               </tr>
               <tr>
                        <td colspan="3" align="center">
                        <br>
                        <input type="submit" value="Save">
                        <input type="reset" value="Reset">
                        <br><br>
                        </td>

               </form>
             </table>
    </body>
</html>