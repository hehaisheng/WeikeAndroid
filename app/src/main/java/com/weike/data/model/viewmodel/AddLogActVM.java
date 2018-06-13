package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;
import android.support.v4.app.FragmentActivity;

import com.weike.data.base.BaseVM;
import com.weike.data.business.log.RemindSettingActivity;
import com.weike.data.util.ActivitySkipUtil;

/**
 * Created by LeoLu on 2018/5/30.
 */

public class AddLogActVM extends BaseVM {

    public ObservableField<String> time = new ObservableField<>();

    public ObservableField<String> content = new ObservableField<>();

    public void timeClick(){
        //TODO shou dialog
    }

    public AddLogActVM(FragmentActivity activity) {
        super(activity);
    }

    public void goToRemindSetting(){
        ActivitySkipUtil.skipAnotherAct(activity, RemindSettingActivity.class);
    }
}
