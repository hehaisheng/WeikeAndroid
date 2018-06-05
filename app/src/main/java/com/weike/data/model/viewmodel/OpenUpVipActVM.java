package com.weike.data.model.viewmodel;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.FileObserver;

import com.weike.data.base.BaseVM;
import com.weike.data.util.LogUtil;
import com.weike.data.util.ToastUtil;

/**
 * Created by LeoLu on 2018/6/5.
 */

public class OpenUpVipActVM extends BaseVM {


    /**
     * 钱
     */
    public ObservableField<String> allMoney = new ObservableField<>("199");

    /**
     * 用户年
     */
    public ObservableField<String> allYear = new ObservableField<>("1");

    /**
     * 用户名
     */
    public ObservableField<String> userName = new ObservableField<>("你好");
    /**
     * vip的时间
     */
    public ObservableField<String> vipTime = new ObservableField<>();

    public ObservableBoolean wechatPay = new ObservableBoolean(true);
    public ObservableBoolean isAliPay = new ObservableBoolean(false);

    public void add(){
        updateData(true);
    }

    private void updateData(boolean isAdd){
        int year = Integer.parseInt(allYear.get());
        int money = Integer.parseInt(allMoney.get());
        if (isAdd) {
           year = year +1;
            money = money + 199;
        } else {
            if (year == 1) return;
            year = year - 1;
            money = money - 199;
        }
        allYear.set(year + "");
        allMoney.set(money + "");

    }

    public void aliPay(){
        isAliPay.set(true);
        wechatPay.set(false);
    }

    public void wechatPay(){
        isAliPay.set(false);
        wechatPay.set(true);
    }

    public void pay(){
        if (isAliPay.get()) {
            //TODO go to alipay
        } else {
            //TODO go to wechatPay
        }
    }

    public void reduce(){
        updateData(false);
    }
}
