package com.weike.data.base;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.support.v4.app.FragmentActivity;

/**
 * Created by LeoLu on 2018/5/22.
 */

public class BaseVM  extends BaseObservable {
    public Activity activity;

    public BaseVM(){

    }

    public BaseVM(Activity activity){
        this.activity = activity;
    }
}
