package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.weike.data.base.BaseVM;

/**
 * Created by LeoLu on 2018/6/4.
 */

public class IntegralItemVM  extends BaseVM{
    public String time;

    public String integralNum;

    public String name;

    public IntegralItemVM(){

    }

    public IntegralItemVM(Activity activity) {
        super(activity);
    }
}
