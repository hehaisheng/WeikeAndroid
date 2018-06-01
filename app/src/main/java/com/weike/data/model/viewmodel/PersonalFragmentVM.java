package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;

import com.weike.data.base.BaseVM;
import com.weike.data.business.myself.DataModifyActivity;
import com.weike.data.util.ActivitySkipUtil;

/**
 * Created by LeoLu on 2018/6/1.
 */

public class PersonalFragmentVM extends BaseVM {
    /**
     * 是否是会员
     */
    public ObservableField<Boolean> isVipVisibility = new ObservableField<>(false);

    /**
     * 会员名字
     */
    public ObservableField<String> vipTime = new ObservableField<>();

    public ObservableField<String> phoneNum = new ObservableField<>();

    public ObservableField<String> nickName = new ObservableField<>();

    public PersonalFragmentVM(Activity activity) {
        super(activity);
    }

    /**
     * 修改个人资料
     */
    public void modifyPersonalData(){
        ActivitySkipUtil.skipAnotherAct(activity, DataModifyActivity.class);
    }

    /*
     *我的二维码
     */
    public void jumpToQrCode(){

    }

    /**
     * 我的积分
     */
    public void jumpToMyIntegral(){

    }

    /**
     * 客服中心
     */
    public void jumpToCustomer(){

    }

    /**
     * 服务中心
     */
    public void jumpServiceSetting(){

    }

    /**
     * app 设置
     */
    public void jumpAppSetting(){

    }
}
