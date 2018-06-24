package com.weike.data.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    public static String getTimeFormat(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");

        return format.format(System.currentTimeMillis());
    };
}
