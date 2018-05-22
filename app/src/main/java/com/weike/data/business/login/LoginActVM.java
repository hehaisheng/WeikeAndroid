package com.weike.data.business.login;

import android.app.FragmentManager;
import android.databinding.ObservableField;
import android.view.View;

import com.weike.data.base.BaseVM;

/**
 * Created by LeoLu on 2018/5/22.
 * 登录模块用的VM
 */
public class LoginActVM  extends BaseVM{

    /**
     * 是否用密码登录
     */
    public boolean isPwdLogin = false;
    /**
     * 手机号码
     */
    public ObservableField<String> phoneNum = new ObservableField<>("");

    /**
     * 是否显示密码
     */
    public ObservableField<Boolean> isShowPwd = new ObservableField<>();


    public void getVerificationCode(){
        //TODO  get 验证码 叼你老母
    }

    public void submit(){
        // TODO  登录

    }


}
