package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.FileObserver;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.weike.data.base.BaseVM;
import com.weike.data.payment.alipay.OrderInfoUtil2_0;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.ToastUtil;

import java.util.Map;

import static com.weike.data.payment.alipay.AliPayConfig.APPID;
import static com.weike.data.payment.alipay.AliPayConfig.PID;
import static com.weike.data.payment.alipay.AliPayConfig.RSA2_PRIVATE;
import static com.weike.data.payment.alipay.AliPayConfig.TARGET_ID;

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

    public OpenUpVipActVM(Activity activity) {
        this.activity = activity;
    }

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

        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2,allMoney.get());
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey =  RSA2_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        new Thread(){
            public void run(){
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                LogUtil.d("OpenUVipAct", JsonUtil.GsonString(result));
            }
        }.start();


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
