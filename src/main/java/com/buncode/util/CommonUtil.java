package com.buncode.util;

import org.junit.Test;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class CommonUtil {

    public static double calcWinRatio(float played, float won){
        double res;
        if (played==0){
            res=0;
        }else{
            res=((won/played)*100);
            res= Math.round(res*100.0)/100.0;  //2 decimals
        }
        return res;
    }

/*    public static Timestamp getCurrentTimestamp_IST(){
        ZonedDateTime timeInIndia = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        return Timestamp.from(timeInIndia.toInstant());
    }*/

  public static String getTimeInIST(Timestamp timestamp){
      TimeZone ist = TimeZone.getTimeZone("Asia/Kolkata"); //Target timezone
      SimpleDateFormat FORMATTER = new SimpleDateFormat("MM/dd/yyyy hh:mma");
      FORMATTER.setTimeZone(ist);

      return (FORMATTER.format(timestamp));  //Date in target timezone
    }

    /*@Test
    public void testCalc(){
        System.out.println(calcWinRatio(3,1));
    }*/
}
