package com.weike.data.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.weike.data.WKBaseApplication;
import com.weike.data.config.SpConfig;

/**
 * Created by LeoLu on 2018/5/26.
 *
 * 保存sp用的
 */
public class SpUtil {

    private static SharedPreferences preferences;


    private static SpUtil instance;

    public static SpUtil getInstance(){
        if (instance == null) {
            instance = new SpUtil();
            preferences = PreferenceManager.getDefaultSharedPreferences(WKBaseApplication.getInstance());
        }
        return instance;
    }

   /*
    *是否第一次打开 引导页系列
    */
    public  boolean getIsFirstOpen() {
        return preferences.getBoolean(SpConfig.TAG_FIRST_OPEN ,true);
    }

    public void saveIsFirstOpen() {
        preferences.edit().putBoolean(SpConfig.TAG_FIRST_OPEN,false).commit();
    }
}
