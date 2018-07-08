package com.weike.data.util;

import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    public static String getTimeFormat(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");

        return format.format(System.currentTimeMillis());
    };

    public static ArrayList<Integer> formatTimeClick(String time) {
        int year = Integer.parseInt(time.substring(0,4));
        int month = Integer.parseInt(time.substring(5,7));
        int day = Integer.parseInt(time.substring(8,10));

        int hour = Integer.parseInt(time.substring(11,13));

        int min = Integer.parseInt(time.substring(14,16));

        ArrayList<Integer> tmp = new ArrayList<>();
        tmp.add(year);
        tmp.add(month);
        tmp.add(day);
        tmp.add(hour);
        tmp.add(min);


        return tmp;
    }
}
