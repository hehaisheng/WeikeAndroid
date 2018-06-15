package com.weike.data;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Created by LeoLu on 2018/5/21.
 */

public class WKBaseApplication extends Application {

    private static WKBaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static WKBaseApplication getInstance(){
        return instance;
    }
}
