package com.weike.data.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;

import com.weike.data.R;
import com.weike.data.WKBaseApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.listeners.OnMoreItemPickListener;
import cn.addapp.pickers.listeners.OnSingleWheelListener;
import cn.addapp.pickers.picker.DateTimePicker;
import cn.addapp.pickers.picker.LinkagePicker;
import cn.addapp.pickers.picker.SinglePicker;
import cn.addapp.pickers.util.DateUtils;

public class PickerUtil {
    public static void  onYearMonthDayTimePicker(View view, DateTimePicker.OnYearMonthDayTimePickListener listener, Activity activity) {
        DateTimePicker picker = new DateTimePicker(activity, DateTimePicker.HOUR_24);
        picker.setActionButtonTop(false);
        picker.setDateRangeStart(2017, 1, 1);
        picker.setDateRangeEnd(2025, 11, 11);
        picker.setSelectedItem(2018,6,16,16,43);
        picker.setTimeRangeStart(9, 0);
        picker.setTimeRangeEnd(20, 30);
        picker.setCanLinkage(false);
        picker.setTitleText("请选择");
//        picker.setStepMinute(5);
        picker.setWeightEnable(true);
        picker.setWheelModeEnable(true);
        LineConfig config = new LineConfig();
        config.setColor(Color.BLUE);//线颜色
        config.setAlpha(120);//线透明度
        config.setVisible(true);//线不显示 默认显示
        picker.setLineConfig(config);
        picker.setLabel(null,null,null,null,null);
        picker.setOnDateTimePickListener(listener);
        picker.show();
    }

    public static void onConstellationPicker(View view ,SinglePicker<String> picker,OnItemPickListener<String> listener) {


        picker.setCanLoop(false);//不禁用循环
        picker.setTopBackgroundColor(0xFFEEEEEE);
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
        picker.setBackgroundColor(0xFFE1E1E1);
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

    public static void onLinkagePicker(Activity activity,OnMoreItemPickListener<String> listener,DialogInterface.OnDismissListener dismissListener) {
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
                firstList.add(0,"不重复");
                return firstList;
            }

            @Override
            public List<String> provideSecondData(int firstIndex) {
                ArrayList<String> secondList = new ArrayList<>();

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
