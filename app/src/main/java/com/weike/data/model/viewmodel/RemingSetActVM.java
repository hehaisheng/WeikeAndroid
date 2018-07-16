package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.weike.data.base.BaseVM;
import com.weike.data.config.DataConfig;
import com.weike.data.util.LogUtil;
import com.weike.data.util.PickerUtil;
import com.weike.data.util.TimeUtil;

import java.util.ArrayList;

import cn.addapp.pickers.listeners.OnMoreItemPickListener;
import cn.addapp.pickers.picker.DateTimePicker;

public class RemingSetActVM extends BaseVM {

    /**
     * 是否提前提醒
     */
    public int isAdvance;  // 1是 2 否

    public int advanceDateType; //提前提醒时间类型

    public int advanceInterval; // 提醒时间间隔

    /*
     * 是否重复提醒
     */
    public int isRepeat = 1; // 是 2 否

    public int repeatInterval;//重复提醒时间间隔

    public int repeatDateType; //重复提醒时间类型

    /**
     * 时间
     */
    public ObservableField<String> time = new ObservableField<>();
    /**
     * 内容
     */
    public ObservableField<String> content = new ObservableField<>();


    /**
     * 是否重复文字
     */
    public ObservableField<String>  repeatText = new ObservableField<>();

    /**
     * 提醒时间
     */
    public ObservableField<String> remindTime = new ObservableField<>();


    /**
     * 设置提醒
     */
    public ObservableField<Boolean> isRemind = new ObservableField<>(true);



    /**
     * 设置不提醒
     */
    public ObservableField<Boolean> isUnRemind = new ObservableField<>(false);


    public ObservableField<Boolean> heightCheck = new ObservableField<>(true);

    public ObservableField<Boolean> midCheck = new ObservableField<>(false);

    public ObservableField<Boolean> lowCheck = new ObservableField<>(false);

   public RemingSetActVM(Activity activity){
        this.activity = activity;
    }

    public void repeatClick(){
        PickerUtil.onLinkagePicker("不重复",activity, new OnMoreItemPickListener<String>() {
            @Override
            public void onItemPicked(String s, String t1, String t2) {

                if(s.contains("不重复")) {
                    repeatText.set("不重复");
                    isRepeat = DataConfig.RemindType.TYPE_UNREMIND;
                } else {
                    isRepeat = DataConfig.RemindType.TYPE_REMIND;
                    repeatText.set(s + " " + t1);
                    repeatInterval = Integer.parseInt(s);

                    LogUtil.d("ActhomeRemind","-->" + t1.contains("天"));
                    if (t1.contains("天")) {
                        LogUtil.d("ActhomeRemind","-->1");
                        repeatDateType = DataConfig.RemindDateType.TYPE_OF_DAY;
                    } else if (t1.contains("周")) {
                        LogUtil.d("ActhomeRemind","-->2");
                        repeatDateType = DataConfig.RemindDateType.TYPE_OF_WEEK;
                    } else if (t1.contains("月")) {
                        LogUtil.d("ActhomeRemind","-->3");
                        repeatDateType = DataConfig.RemindDateType.TYPE_OF_MONTH ;
                    } else if (t1.contains("年")) {
                        LogUtil.d("ActhomeRemind","-->4");
                        repeatDateType = DataConfig.RemindDateType.TYPE_OF_YEAR;
                    } else if (t1.contains("分") || t1.contains("分钟")){
                        repeatDateType = DataConfig.RemindDateType.TYPE_OF_MIN;
                    } else if (t1.contains("时") || t1.contains("小时")){
                        repeatDateType = DataConfig.RemindDateType.TYPE_OF_HOUR;
                    }
                }


            }
        }, new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
    }

    /**
     * 时间Dialog
     */
    public void timeClick(){
        if(TextUtils.isEmpty(time.get())) {


            ArrayList<Integer> tmp = TimeUtil.formatTimeClick(TimeUtil.getTimeFormat());

            PickerUtil.onYearMonthDayTimePicker(tmp.get(0),tmp.get(1),tmp.get(2),tmp.get(3),tmp.get(4), new DateTimePicker.OnYearMonthDayTimePickListener() {
                @Override
                public void onDateTimePicked(String s, String s1, String s2, String s3, String s4) {
                    time.set(s + "-" + s1 + "-" + s2 + " " + s3 + ":" + s4);
                }
            }, activity);
        } else {
            ArrayList<Integer> tmp = TimeUtil.formatTimeClick(time.get());

            PickerUtil.onYearMonthDayTimePicker(tmp.get(0),tmp.get(1),tmp.get(2),tmp.get(3),tmp.get(4), new DateTimePicker.OnYearMonthDayTimePickListener() {
                @Override
                public void onDateTimePicked(String s, String s1, String s2, String s3, String s4) {
                    time.set(s + "-" + s1 + "-" + s2 + " " + s3 + ":" + s4);
                }
            }, activity);
        }
    }

    /**
     *
     */
    public void remindClick() {

        PickerUtil.onLinkagePicker("不提前",activity, new OnMoreItemPickListener<String>() {
            @Override
            public void onItemPicked(String s, String t1, String t2) {

                if(s.contains("不提前")) {
                    remindTime.set("不提前");
                    isAdvance = DataConfig.RemindType.TYPE_UNREMIND;
                } else {
                    isAdvance = DataConfig.RemindType.TYPE_REMIND;
                    remindTime.set(s + " " + t1);
                    advanceInterval = Integer.parseInt(s);
                    if (t1.contains("天")) {
                        advanceDateType = DataConfig.RemindDateType.TYPE_OF_DAY;
                    } else if (t1.contains("周")) {
                        advanceDateType = DataConfig.RemindDateType.TYPE_OF_WEEK;
                    } else if (t1.contains("月")) {
                        advanceDateType = DataConfig.RemindDateType.TYPE_OF_MONTH ;
                    } else if (t1.contains("年")) {
                        advanceDateType = DataConfig.RemindDateType.TYPE_OF_YEAR;
                    } else if (t1.contains("分") || t1.contains("分钟")){
                        advanceDateType = DataConfig.RemindDateType.TYPE_OF_MIN;
                    } else if (t1.contains("时") || t1.contains("小时")){
                        advanceDateType = DataConfig.RemindDateType.TYPE_OF_HOUR;
                    }
                }


            }
        }, new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
    }

    /**
     *
     */
    public void unRemindCheckBoxClick(){
        isUnRemind.set(true);
        isRemind.set(false);

    }

    public void remindCheckBoxClick(){
        isUnRemind.set(false);
        isRemind.set(true);
    }
}
