package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.weike.data.base.BaseVM;
import com.weike.data.business.log.RemindSettingActivity;
import com.weike.data.model.business.ToDo;
import com.weike.data.util.Constants;
import com.weike.data.util.LogUtil;
import com.weike.data.util.PickerUtil;
import com.weike.data.util.TimeUtil;
import com.weike.data.util.ToastUtil;

import java.util.ArrayList;

import cn.addapp.pickers.picker.DateTimePicker;

/**
 * Created by LeoLu on 2018/5/30.
 */

public class AddLogActVM extends BaseVM {

    public ObservableField<String> time = new ObservableField<>();

    public ObservableField<String> content = new ObservableField<>();



    public ObservableField<Boolean> toChange = new ObservableField<>(false);



    public ToDo toDo = null;

    public AddLogActVM(Activity activity){
        this.activity =activity;

    }

    public void setToDo(ToDo toDo) {
        this.toDo = toDo;
    }


    public void timeClick(){


        if(TextUtils.isEmpty(time.get())) {

            ArrayList<Integer> tmp = TimeUtil.formatTimeClick(TimeUtil.getTimeFormat());

            PickerUtil.onYearMonthDayTimePicker(tmp.get(0),tmp.get(1),tmp.get(2),tmp.get(3),tmp.get(4), new DateTimePicker.OnYearMonthDayTimePickListener() {
                @Override
                public void onDateTimePicked(String s, String s1, String s2, String s3, String s4) {

                    String pickTime = s + "-" + s1 + "-" + s2 + " " + s3 + ":" + s4 ;
                    LogUtil.d(Constants.LOG_DATA,"timeClick"+pickTime);
                    boolean result = TimeUtil.timeCoperay(TimeUtil.getTimeFormat(),pickTime);
                    if(result == false) { //选择时间 大于当前时间 要处理提醒
                        ToastUtil.showToast("日志时间不能晚于当前时间");
                        return;
                    } else {
                        time.set(pickTime);
                    }


                }
            }, activity);
        } else {
            ArrayList<Integer> tmp = TimeUtil.formatTimeClick(time.get());

            PickerUtil.onYearMonthDayTimePicker(tmp.get(0),tmp.get(1),tmp.get(2),tmp.get(3),tmp.get(4), new DateTimePicker.OnYearMonthDayTimePickListener() {
                @Override
                public void onDateTimePicked(String s, String s1, String s2, String s3, String s4) {



                    String pickTime = s + "-" + s1 + "-" + s2 + " " + s3 + ":" + s4 ;
                    LogUtil.d(Constants.LOG_DATA,"timeClick1"+pickTime);
                    boolean result = TimeUtil.timeCoperay(TimeUtil.getTimeFormat(),pickTime);
                    if(result == false) { //选择时间 大于当前时间 要处理提醒
                        ToastUtil.showToast("日志时间不能晚于当前时间");
                        return;
                    } else {
                        time.set(pickTime);
                    }


                }
            }, activity);
        }
    }

    public  AddLogActVM(FragmentActivity activity) {
        this.activity = activity;
    }

    /**
     * 添加关联
     */



    public void goToRemindSetting(){


        RemindSettingActivity.startActivity(activity,toDo);

    }
}
