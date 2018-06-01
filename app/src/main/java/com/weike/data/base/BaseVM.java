package com.weike.data.base;

import android.app.Activity;
import android.databinding.BaseObservable;

/**
 * Created by LeoLu on 2018/5/22.
 */

public class BaseVM  extends BaseObservable {
    public Activity activity;


    public BaseVM(Activity activity){
        this.activity = activity;
    }
}
