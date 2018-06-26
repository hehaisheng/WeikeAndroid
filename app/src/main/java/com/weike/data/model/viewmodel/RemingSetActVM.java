package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.widget.TimePicker;

import com.weike.data.base.BaseVM;
import com.weike.data.config.DataConfig;
import com.weike.data.util.PickerUtil;

import java.util.ArrayList;

import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.listeners.OnMoreItemPickListener;
import cn.addapp.pickers.picker.DateTimePicker;
import cn.addapp.pickers.picker.SinglePicker;
import io.reactivex.internal.operators.observable.ObservableFilter;

public class RemingSetActVM extends BaseVM {

    public int beforeRemindDay;

    public int repeatIntervalHour;
    /**
     * 时间
     */
    public ObservableField<String> time = new ObservableField<>();
    /**
     * 内容
     */
    public ObservableField<String> content = new ObservableField<>();

    /**
     * 是否重复
     */
    public ObservableField<Boolean> IsRepeat = new ObservableField<>(false);

    /**
     * 是否重复文字
     */
    public ObservableField<String>  repeatText = new ObservableField<>();

    /**
     * 提醒时间
     */
    public ObservableField<String> remindTime = new ObservableField<>();


    /**
     * 是否提醒
     */
    public ObservableField<Boolean> isRemind = new ObservableField<>(true);

    /**
     * 日期类型
     */
    public int dateType;

    /**
     * 设置不提醒
     */
    public ObservableField<Boolean> isUnRemind = new ObservableField<>();


    public ObservableField<Boolean> heightCheck = new ObservableField<>(true);

    public ObservableField<Boolean> midCheck = new ObservableField<>(false);

    public ObservableField<Boolean> lowCheck = new ObservableField<>(false);

   public RemingSetActVM(Activity activity){
        this.activity = activity;
    }

    public void repeatClick(){
        PickerUtil.onLinkagePicker(activity, new OnMoreItemPickListener<String>() {
            @Override
            public void onItemPicked(String s, String t1, String t2) {

                if(s.contains("不重复")) {
                    repeatText.set("不重复");
                } else {
                    repeatText.set(s + " " + t1);
                    repeatIntervalHour = Integer.parseInt(s);
                    if (t1.contains("天")) {
                        dateType = DataConfig.RemindDateType.TYPE_OF_DAY;
                    } else if (t1.contains("周")) {
                        dateType = DataConfig.RemindDateType.TYPE_OF_WEEK;
                    } else if (t1.contains("月")) {
                        dateType = DataConfig.RemindDateType.TYPE_OF_MONTH ;
                    } else if (t1.contains("年")) {
                        dateType = DataConfig.RemindDateType.TYPE_OF_YEAR;
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
        PickerUtil.onYearMonthDayTimePicker(null, new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String s, String s1, String s2, String s3, String s4) {
                time.set(s +"-" + s1 +"-" + s2 + " " +s3 +":"+ s4);
            }
        },activity);
    }

    /**
     *
     */
    public void remindClick() {

        ArrayList<String> list = new ArrayList<>();
        for(int i = 1;i<31; i++){
            String s = "";
            if(i<10){
                s = "0"+i;
            }else{
                s = i+"";
            }
            list.add(s);
        }

        SinglePicker<String> picker = new SinglePicker<String>(activity,list);
        picker.setLabel("天");
        PickerUtil.showSignTitlePicker(picker, new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int i, String s) {
                if(Integer.parseInt(s) < 10) {
                    remindTime.set(s.replace("0","") + "天");
                } else {
                    remindTime.set(s + "天");
                }
                beforeRemindDay = Integer.parseInt(s);
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
