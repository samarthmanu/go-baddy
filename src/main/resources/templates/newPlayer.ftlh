<!DOCTYPE html>
<html>
    <head>
        <title>New Player</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
    <body>
    <input type="button" value="Back" onclick="location.href = document.referrer; return false;"/>
    <input type="button" onclick="location.href='/'" value="Back to Main"/>
     <h2>Player Info</h2>
             <form name="playerForm" action="/newPlayer" method="post">
               <table border="5">
               <tr>
                         <td>
                         <br>
                         <label for="name">Name:</label><br>
                         <input type="text" id="name" name="name"><br><br>

                         <label for="name">Playing Style:</label><br>
                         <input type="text" id="playing_style" name="playing_style"><br><br>

                         <label for="name">Signature moves:</label><br>
                         <input type="text" id="signature_moves" name="signature_moves"><br><br>

                         <label for="name">Other Names:</label><br>
                         <input type="text" id="alias" name="alias"><br><br>

                         <label for="name">Contact:</label><br>
                         <input type="text" id="contact" name="contact"><br><br>
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