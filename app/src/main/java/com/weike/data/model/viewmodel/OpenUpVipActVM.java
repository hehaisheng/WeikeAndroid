package com.weike.data.model.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.base.BaseVM;
import com.weike.data.config.Config;
import com.weike.data.model.req.GetPayDataReq;
import com.weike.data.model.resp.GetPayDataResp;
import com.weike.data.model.resp.WeChatPayDataResp;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.payment.alipay.OrderInfoUtil2_0;
import com.weike.data.payment.alipay.PayResult;
import com.weike.data.payment.wechat.WXRegister;
import com.weike.data.payment.wechat.WxConfig;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

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

    public ObservableBoolean wechatPay = new ObservableBoolean(false);

    public ObservableBoolean isAliPay = new ObservableBoolean(true);

    public ObservableField<Boolean> oneCheck = new ObservableField<>(false);

    public ObservableField<Boolean> twoCheck = new ObservableField<>(false);

    public ObservableField<Boolean> threeCheck = new ObservableField<>(true);

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

        if (isAliPay.get()) {
            alipay();
        } else {
            wechat();
            //TODO go to wechatPay
        }
    }

    private void wechat(){
        int money = 0;
        if (oneCheck.get()) {
            money = 188;
        } else if (twoCheck.get()){
            money = 328;
        } else if (threeCheck.get()) {
            money = 418;
        }

        GetPayDataReq req = new GetPayDataReq();
        req.buyNum = Integer.parseInt(allYear.get());
        req.money = money + "";
        req.orderNo = System.currentTimeMillis() + "";
        req.platform = "wxpay";
        req.sign = SignUtil.signData(req);


        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_PAY_DATA)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<WeChatPayDataResp>>(){

                })).subscribe(new BaseObserver<BaseResp<WeChatPayDataResp>>() {
            @Override
            protected void onSuccess(BaseResp<WeChatPayDataResp> getPayDataRespBaseResp) throws Exception {
                WeChatPayDataResp.WxPay orderInfo = getPayDataRespBaseResp.getDatas().wxpay;

                new Thread(){
                    public void run(){
                        PayReq payReq = new PayReq();
                        payReq.packageValue = "Sign=WXPay";
                        payReq.appId = orderInfo.appid;
                        payReq.partnerId		= orderInfo.partnerid;
                        payReq.prepayId		= orderInfo.prepayid;
                        payReq.nonceStr		= orderInfo.noncestr;
                        payReq.timeStamp		= orderInfo.timestamp;
                        payReq.sign			= orderInfo.sign;
                        payReq.extData			= "app data"; // optional
                        WXRegister.getApi().sendReq(payReq);
                    }
                }.start();
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    public void oneCheckClick(){
        oneCheck.set(true);
        twoCheck.set(false);
        threeCheck.set(false);
    }

    public void twoCheckClick(){
        twoCheck.set(true);
        oneCheck.set(false);
        threeCheck.set(false);
    }

    public void threeCheckClick(){
        twoCheck.set(false);
        oneCheck.set(false);
        threeCheck.set(true);
    }

    private void alipay(){

        int money = 0;
        if (oneCheck.get()) {
            money = 188;
        } else if (twoCheck.get()){
            money = 328;
        } else if (threeCheck.get()) {
            money = 418;
        }

        GetPayDataReq req = new GetPayDataReq();
        req.buyNum = Integer.parseInt(allYear.get());
        req.money = money + "";
        req.orderNo = System.currentTimeMillis() + "";
        req.platform = "alipay";
        req.sign = SignUtil.signData(req);


        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_PAY_DATA)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetPayDataResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetPayDataResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetPayDataResp> getPayDataRespBaseResp) throws Exception {
                String orderInfo = getPayDataRespBaseResp.getDatas().alipay;

                new Thread(){
                    public void run(){
                        PayTask alipay = new PayTask(activity);
                        Map<String, String> result = alipay.payV2(orderInfo, true);
                        Message message = Message.obtain();
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                }.start();
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }



   @SuppressLint("HandlerLeak")
   Handler handler = new Handler(){
        public void handleMessage(Message msg){

            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                ToastUtil.showToast("支付成功");
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                ToastUtil.showToast("支付失败");
            }
        }
    };

    public void reduce(){
        updateData(false);
    }
}
