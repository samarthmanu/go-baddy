package com.buncode.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class CommonUtil {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static double calcWinPercentage(float played, float won){
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

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static Date stringToSqlDate(String dateStr) throws ParseException {
        java.util.Date date = dateFormat.parse(dateStr);
        return new java.sql.Date(date.getTime());
    }

    public static java.util.Date stringToUtilDate(String dateStr) throws ParseException {
        return dateFormat.parse(dateStr);
    }

    public static Timestamp stringToTimeStamp(String dateStr) throws ParseException {
        java.util.Date date = dateFormat.parse(dateStr);
        return new java.sql.Timestamp(date.getTime());
    }

    /*@Test
    public void testCalc(){
        System.out.println(calcWinRatio(3,1));
    }*/
}
