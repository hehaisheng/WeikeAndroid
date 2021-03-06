package com.weike.data.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TimeUtil {

    public static String getTimeFormat(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String time = format.format(new Date());
        LogUtil.d(Constants.LOG_DATA,"format:" + time);

        return time;
    };

    public static String getTimeFormat(String f){
        SimpleDateFormat format = new SimpleDateFormat(f);

        return format.format(System.currentTimeMillis());
    };


    public static boolean timeCoperay(String cur , String old){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            long curTime = format.parse(cur).getTime();
            long oldTime = format.parse(old).getTime();


            LogUtil.d(Constants.LOG_DATA ,"timeCoperay"+curTime + ","+ oldTime);
            if (curTime >= oldTime ){
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<Integer> formatDateClick(String date){
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,7));
        int day = Integer.parseInt(date.substring(8,10));

        ArrayList<Integer> tmp = new ArrayList<>();
        tmp.add(year);
        tmp.add(month);
        tmp.add(day);

        return tmp;
    }

    public static ArrayList<Integer> formatTimeClick(String time) {


        ArrayList<Integer> tmp = new ArrayList<>();


        int year = Integer.parseInt(time.substring(0,4));
        int month = Integer.parseInt(time.substring(5,7));
        int day = Integer.parseInt(time.substring(8,10));

        int hour = Integer.parseInt(time.substring(11,13));

        int min = Integer.parseInt(time.substring(14,16));




        if (min == 0) min = 1;


        tmp.add(year);
        tmp.add(month);
        tmp.add(day);
        tmp.add(hour);
        tmp.add(min);


        return tmp;
    }
}
