package com.weike.data.model.viewmodel;

import android.content.Intent;
import android.databinding.ObservableField;

import com.weike.data.base.BaseVM;

import java.util.ArrayList;

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
     * 是否能点击
     */
    public ObservableField<Boolean> canClickable = new ObservableField<>(false);


    /**
     * 是否是编辑状态
     */
    public ObservableField<Boolean> isModify = new ObservableField<>();


    public void goToBirthDayRemind(){
        Intent intent = new Intent();
    }

    public void goToManagerAttr(){

    }
}
