package com.weike.data.util;

import android.util.Log;

/**
 * Created by LeoLu on 2018/5/22.
 */

public class LogUtil {
    public static final boolean DEBUG = true;

    public static void d(String t , String m) {
        if (DEBUG) {
            Log.d(t ,m);
        }
    }
}
