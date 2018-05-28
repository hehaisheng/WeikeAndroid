package com.weike.data;

import android.app.Application;

/**
 * Created by LeoLu on 2018/5/21.
 */

public class WKBaseApplication extends Application {

    private static WKBaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

    public static WKBaseApplication getInstance(){
        return instance;
    }
}
