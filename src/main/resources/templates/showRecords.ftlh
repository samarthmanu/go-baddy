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
        <title>Records</title>
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
        <input type="button" onclick="location.href='/'" value="Back to Main"/>
                 <h2>Filter By</h2>
                                                <form name="filterForm" action="/records" method="get">
                                                        <select id="season_id" name="season_id" onchange="validate()">
                                                            <#list seasons as s>
                                                                <option value="${s.season_id}" <#if s.season_id==season_id>selected</#if>>${s.name}</option>
                                                            </#list>
                                                                <option value="-1" <#if -1==season_id>selected</#if>>All time</option>
                                                                <!--<option value="0"  <#if 0==season_id>selected</#if>>Custom</option>-->
                                                        </select>
                                                        <br><br>
                                                        <div id="datePicker" <#if 0==season_id>style="display:block;"<#else>style="display:none;"</#if>>
                                                        <label for="fromDate">From: </label> <input type="date" id="fromDate" name="fromDate" <#if fromDate?has_content>value="${fromDate}"</#if>>
                                                            <label for="toDate">To: </label> <input type="date" id="toDate" name="toDate" <#if toDate?has_content>value="${toDate}"</#if>>
                                                               <button onclick="return validateCustomDates()">Go</button>
                                                        </div>
                                               </form>
        <h2>Records</h2>
                <#noautoesc>
                <table>
                <tr>
                <td style="vertical-align:top">
                <table border="5">
                    <tr>
                        <th colspan=2>Hall of Fame</th>
                    </tr>
                    <#list fameRecords as record>
                        <tr>
                            <th>${record.desc}</th>
                            <td>${record.holder}</td>
                        </tr>
                    </#list>
                </table>
                </td>
                <td style="vertical-align:top">
                <table border="5">
                    <tr>
                        <th colspan=2>Hall of Shame</th>
                    </tr>
                    <#list shameRecords as record>
                        <tr>
                            <th>${record.desc}</th>
                            <td >${record.holder}</td>
                        </tr>
                    </#list>
               </table>
               </td>
               </#noautoesc>
    </body>
</html>