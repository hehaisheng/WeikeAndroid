package com.weike.data.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;

import com.weike.data.R;
import com.weike.data.WKBaseApplication;

import java.util.ArrayList;
import java.util.List;

import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.listeners.OnMoreItemPickListener;
import cn.addapp.pickers.picker.DatePicker;
import cn.addapp.pickers.picker.DateTimePicker;
import cn.addapp.pickers.picker.LinkagePicker;
import cn.addapp.pickers.picker.SinglePicker;

public class PickerUtil {
    public static void  onYearMonthDayTimePicker(View view, DateTimePicker.OnYearMonthDayTimePickListener listener, Activity activity) {
        DateTimePicker picker = new DateTimePicker(activity, DateTimePicker.HOUR_24);
        picker.setActionButtonTop(false);
        picker.setDateRangeStart(1900, 1, 1);
        picker.setDateRangeEnd(2100, 12, 12);
        picker.setSelectedItem(2018,6,16,16,43);
        picker.setTimeRangeStart(9, 0);
        picker.setTimeRangeEnd(20, 30);
        picker.setCanLinkage(false);
        picker.setLabel("年","月","日","时","分");

        picker.setTitleText("请选择");
//        picker.setStepMinute(5);
        picker.setWeightEnable(true);
        picker.setWheelModeEnable(true);
        LineConfig config = new LineConfig();
        config.setColor(Color.BLUE);//线颜色
        config.setAlpha(120);//线透明度
        config.setVisible(true);//线不显示 默认显示
        picker.setLineConfig(config);
        picker.setOnDateTimePickListener(listener);
        picker.show();
    }

    public static void  onYearMonthDayTimePicker(int year,int month , int day ,int hour , int min,DateTimePicker.OnYearMonthDayTimePickListener listener, Activity activity) {
        if (hour == 0) {
            hour =10;
        }
        if (min == 0) {
            hour = 10;
        }

        DateTimePicker picker = new DateTimePicker(activity, DateTimePicker.HOUR_24);
        picker.setActionButtonTop(false);
        picker.setDateRangeStart(1900, 1, 1);
        picker.setDateRangeEnd(2100, 12, 12);
        picker.setSelectedItem(year,month,day,hour,min);
        picker.setTimeRangeStart(9, 0);
        picker.setTimeRangeEnd(20, 30);
        picker.setCanLinkage(false);
        picker.setLabel("年","月","日","时","分");

        picker.setTitleText("请选择");
//        picker.setStepMinute(5);
        picker.setWeightEnable(true);
        picker.setWheelModeEnable(true);
        LineConfig config = new LineConfig();
        config.setColor(Color.BLUE);//线颜色
        config.setAlpha(120);//线透明度
        config.setVisible(true);//线不显示 默认显示
        picker.setLineConfig(config);
        picker.setOnDateTimePickListener(listener);
        picker.show();
    }

    public static void onYearMonthDayPicker(int year,int month , int day ,Activity activity,DatePicker.OnYearMonthDayPickListener listener) {
        final DatePicker picker = new DatePicker(activity);
        picker.setCanLoop(true);
        picker.setWheelModeEnable(true);
        picker.setTopPadding(15);
        picker.setDateRangeStart(1900, 1, 1);
        picker.setDateRangeEnd(2100, 12, 12);
        picker.setSelectedItem(year, month, day);
        picker.setWeightEnable(true);
        picker.setLineColor(Color.BLACK);
        picker.setOnDatePickListener(listener);

        LineConfig config = new LineConfig();
        config.setColor(Color.BLUE);//线颜色
        config.setAlpha(120);//线透明度
        config.setVisible(true);//线不显示 默认显示
        picker.setLineConfig(config);
        picker.show();
    }


    public static void onOptionPicker(Activity activity,OnItemPickListener listener) {
        ArrayList<String> list = new ArrayList<>();
        list.add("0.5");
        list.add("1");
        list.add("1.5");
        list.add("2");
        list.add("2.5");
        list.add("3");

//        String[] ss = (String[]) list.toArray();
        SinglePicker<String> picker = new SinglePicker<>(activity, list);
        picker.setCanLoop(false);//不禁用循环
        picker.setLineVisible(true);
        picker.setTitleText("设置你的提醒时间");
        picker.setTextSize(18);
        picker.setSelectedIndex(8);
        picker.setWheelModeEnable(false);
        //启用权重 setWeightWidth 才起作用
        picker.setSelectedTextColor(WKBaseApplication.getInstance().getResources().getColor(R.color.color_41BCF6));
        picker.setUnSelectedTextColor(WKBaseApplication.getInstance().getResources().getColor(R.color.color_bebebe));
        picker.setLabel("小时");
        picker.setWeightEnable(true);
        picker.setWeightWidth(1);

        picker.setOnItemPickListener(listener);
        picker.show();
    }

    public static void onOptionPicker(List<String> data ,Activity activity,OnItemPickListener listener) {


//        String[] ss = (String[]) list.toArray();
        SinglePicker<String> picker = new SinglePicker<>(activity, data);
        picker.setCanLoop(false);//不禁用循环
        picker.setLineVisible(true);
        picker.setTitleText("设置你的提醒时间");
        picker.setTextSize(18);
        picker.setSelectedIndex(8);
        picker.setWheelModeEnable(false);
        //启用权重 setWeightWidth 才起作用
        picker.setSelectedTextColor(WKBaseApplication.getInstance().getResources().getColor(R.color.color_41BCF6));
        picker.setUnSelectedTextColor(WKBaseApplication.getInstance().getResources().getColor(R.color.color_bebebe));
        picker.setLabel("小时");
        picker.setWeightEnable(true);
        picker.setWeightWidth(1);

        picker.setOnItemPickListener(listener);
        picker.show();
    }

    public static void onOptionPicker(List<String> data , Activity activity, OnItemPickListener listener, DialogInterface.OnDismissListener listener2) {


//        String[] ss = (String[]) list.toArray();
        SinglePicker<String> picker = new SinglePicker<>(activity, data);
        picker.setCanLoop(false);//不禁用循环
        picker.setLineVisible(true);
        picker.setTitleText("设置你的提醒时间");
        picker.setTextSize(18);
        picker.setSelectedIndex(8);
        picker.setWheelModeEnable(false);
        //启用权重 setWeightWidth 才起作用
        picker.setSelectedTextColor(WKBaseApplication.getInstance().getResources().getColor(R.color.color_41BCF6));
        picker.setUnSelectedTextColor(WKBaseApplication.getInstance().getResources().getColor(R.color.color_bebebe));
        picker.setLabel("小时");
        picker.setWeightEnable(true);
        picker.setWeightWidth(1);
        picker.setOnDismissListener(listener2);
        picker.setOnItemPickListener(listener);
        picker.show();
    }

    public static void onConstellationPicker(View view ,SinglePicker<String> picker,OnItemPickListener<String> listener) {


        picker.setCanLoop(false);//不禁用循环
        picker.setTopBackgroundColor(Color.WHITE);
        picker.setTopHeight(50);
        picker.setTopLineColor(0xFF33B5E5);
        picker.setTopLineHeight(1);
        picker.setTitleText("请选择");
        picker.setTitleTextColor(0xFF999999);
        picker.setTitleTextSize(15);
        picker.setCancelTextColor(0xFF33B5E5);
        picker.setCancelTextSize(13);

        picker.setSubmitTextColor(0xFF33B5E5);
        picker.setSubmitTextSize(13);
        picker.setLineVisible(false);
        picker.setSelectedTextColor(WKBaseApplication.getInstance().getResources().getColor(R.color.color_41BCF6));
        picker.setUnSelectedTextColor(WKBaseApplication.getInstance().getResources().getColor(R.color.color_bebebe));
        picker.setWheelModeEnable(false);
        LineConfig config = new LineConfig();
        config.setColor(0xFFEEEEEE);//线颜色
//        config.setRatio(1);//线比率
        picker.setLineConfig(config);
        picker.setItemWidth(300);
        picker.setBackgroundColor(Color.WHITE);
        picker.setSelectedIndex(7);
        picker.setOnItemPickListener(listener);

        picker.show();
    }

    public static void showSignTitlePicker(SinglePicker<String> picker , OnItemPickListener<String> listener) {

        picker.setCanLoop(false);//不禁用循环
        picker.setLineVisible(true);
        picker.setTextSize(18);
        picker.setSelectedIndex(8);
        picker.setWheelModeEnable(false);
        //启用权重 setWeightWidth 才起作用
        picker.setLabel("天");
        picker.setWeightEnable(true);
        picker.setWeightWidth(1);
        picker.setSelectedTextColor(WKBaseApplication.getInstance().getResources().getColor(R.color.color_41BCF6));
        picker.setUnSelectedTextColor(WKBaseApplication.getInstance().getResources().getColor(R.color.color_bebebe));

        picker.setOnItemPickListener(listener);
        picker.show();
    }

    public static void onLinkagePicker(String notTitle , Activity activity,OnMoreItemPickListener<String> listener,DialogInterface.OnDismissListener dismissListener) {
        LinkagePicker.DataProvider provider = new LinkagePicker.DataProvider() {

            @Override
            public boolean isOnlyTwo() {
                return true;
            }

            @Override
            public List<String> provideFirstData() {
                ArrayList<String> firstList = new ArrayList<>();
                for(int i = 1 ; i < 11 ; i++) {
                    firstList.add("" + i);
                }
                firstList.add(0,notTitle);
                return firstList;
            }

            @Override
            public List<String> provideSecondData(int firstIndex) {
                ArrayList<String> secondList = new ArrayList<>();
               secondList.add("分钟");
               secondList.add("小时");
               secondList.add("天");
               secondList.add("周");
               secondList.add("月");
               secondList.add("年");


                return secondList;
            }

            @Override
            public List<String> provideThirdData(int firstIndex, int secondIndex) {
                return null;
            }

        };
        LinkagePicker picker = new LinkagePicker(activity, provider);
        picker.setCanLoop(false);
        picker.setSelectedIndex(0, 8);
        //picker.setSelectedItem("12", "9");
        picker.setLabel("","");
        picker.setOnDismissListener(dismissListener);
        picker.setOnMoreItemPickListener(listener);
        picker.show();
    }
}
