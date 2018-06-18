package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;

import com.weike.data.base.BaseVM;
import com.weike.data.util.PickerUtil;

import java.util.ArrayList;
import java.util.List;

import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.DateTimePicker;
import cn.addapp.pickers.picker.SinglePicker;

/**
 * Created by LeoLu on 2018/6/7.
 */

public class ClientBaseMsgVM extends BaseVM {
    /**
     *
     */
    public ArrayList<String> clientIds = new ArrayList<>();

    /**
     *
     */
    public ObservableField<String> email = new ObservableField<>("");

    /**
     * 公司名字
     */
    public ObservableField<String> companyName = new ObservableField<>();

    /**
     * 职业
     */
    public ObservableField<String> job = new ObservableField<>("");

    /**
     * 公司地址
     */
    public ObservableField<String> companyLocation = new ObservableField<>("");

    /**
     * 家地址
     */
    public ObservableField<String> houseLocation = new ObservableField<>("");

    /**
     * 生日
     */
    public ObservableField<String> birthday = new ObservableField<>("");

    /**
     *
     */
    public ObservableField<String> sex = new ObservableField<>("");

    /**
     * 是否结婚
     */
    public ObservableField<String> marry = new ObservableField<>("");

    /**
     * 儿子数量
     */
    public ObservableField<String> son = new ObservableField<>("");

    /**
     * 女孩数量
     */
    public ObservableField<String> gril = new ObservableField<>("");

    /**
     * 身份证号码
     */
    public ObservableField<String> idCard = new ObservableField<>();

    /**
     * 用户身高
     */
    public ObservableField<String> clientHeight = new ObservableField<>();

    /**
     * 用户体重
     */
    public ObservableField<String> clientWidght = new ObservableField<>();

    /**
     * 是否能点击
     */
    public ObservableField<Boolean> canClickable = new ObservableField<>(false);




    /**
     * 是否是编辑状态
     */
    public ObservableField<Boolean> isModify = new ObservableField<>();


    public ClientBaseMsgVM(Activity activity) {
        this.activity = activity;
    }

    public void birthDayClick(){
        PickerUtil.onYearMonthDayTimePicker(null, new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String s, String s1, String s2, String s3, String s4) {
                birthday.set(s + "-" + s1 + "-" + s2 + " " + s3 + ":" + s4);
            }
        },activity);
    }

    public void sexClick(){
        SinglePicker<String> picker = new SinglePicker<String>(activity,new String[]{"男","女","保密"});

        PickerUtil.onConstellationPicker(null, picker, new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int i, String s) {
                sex.set(s);
            }
        });
    }

    public void marryClick(){
        SinglePicker<String> picker = new SinglePicker<String>(activity,new String[]{"已婚","未婚","保密"});

        PickerUtil.onConstellationPicker(null, picker, new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int i, String s) {
                marry.set(s);
            }
        });
    }

    public void goToBirthDayRemind(){
        Intent intent = new Intent();
    }

    public void goToManagerAttr(){

    }
}
