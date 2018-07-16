package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.weike.data.R;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseVM;
import com.weike.data.business.log.RemindSettingActivity;
import com.weike.data.business.setting.AttrManagerActivity;
import com.weike.data.config.DataConfig;
import com.weike.data.model.business.ToDo;
import com.weike.data.util.ActivitySkipUtil;
import com.weike.data.util.PickerUtil;
import com.weike.data.util.TimeUtil;

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

    public ToDo birthDayTodo = new ToDo();


    public ObservableField<Integer> birthdayRemindIcon = new ObservableField<>(R.mipmap.ic_remind_dis);

    /**
     * 是否是编辑状态
     */
    public ObservableField<Boolean> isModify = new ObservableField<>(false);



    public BaseFragment baseFragment;

    public ClientBaseMsgVM(BaseFragment baseFragment) {
        this.baseFragment = baseFragment;
    }

    public ClientBaseMsgVM(Activity activity) {
        this.activity = activity;
    }

    /**
     * 生日提醒回调
     * @param toDo
     */
    public void onBirthdayRemindResult(ToDo toDo){

        birthDayTodo = toDo;
        if (toDo.isAdvance == DataConfig.RemindType.TYPE_REMIND) {
            birthdayRemindIcon.set(R.mipmap.ic_remind);
        } else {
            birthdayRemindIcon.set(R.mipmap.ic_remind_dis);
        }
    }


    /**
     * 生日时间点击
     */
    public void birthdayTimeClick(){


        if(TextUtils.isEmpty(birthday.get())) {

            PickerUtil.onYearMonthDayTimePicker(null, new DateTimePicker.OnYearMonthDayTimePickListener() {
                @Override
                public void onDateTimePicked(String s, String s1, String s2, String s3, String s4) {
                    birthday.set(s + "-" + s1 + "-" + s2 + " " + s3 + ":" + s4);
                }
            }, baseFragment.getActivity());
        } else {
            ArrayList<Integer> tmp = TimeUtil.formatTimeClick(birthday.get());

            PickerUtil.onYearMonthDayTimePicker(tmp.get(0),tmp.get(1),tmp.get(2),tmp.get(3),tmp.get(4), new DateTimePicker.OnYearMonthDayTimePickListener() {
                @Override
                public void onDateTimePicked(String s, String s1, String s2, String s3, String s4) {
                    birthday.set(s + "-" + s1 + "-" + s2 + " " + s3 + ":" + s4);
                }
            }, baseFragment.getActivity());
        }
    }

    /*
     *性别选择
     */
    public void sexClick(){
        SinglePicker<String> picker = new SinglePicker<String>(baseFragment.getActivity(),new String[]{"男","女"});

        PickerUtil.onConstellationPicker(null, picker, new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int i, String s) {
                sex.set(s);
            }
        });
    }

    /**
     * 婚姻选择
     */
    public void marryClick(){
        SinglePicker<String> picker = new SinglePicker<String>(baseFragment.getActivity(),new String[]{"已婚","未婚","离异"});

        PickerUtil.onConstellationPicker(null, picker, new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int i, String s) {
                marry.set(s);
            }
        });
    }

    /**
     * 生日提醒跳转
     */
    public void goToBirthDayRemind(){
        RemindSettingActivity.startActivity(baseFragment,birthDayTodo);
    }

    public void goToManagerAttr(){
        Intent intent = new Intent(baseFragment.getContext(),AttrManagerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        baseFragment.startActivityForResult(intent,AttrManagerActivity.REQUEST_CODE);


    }
}
