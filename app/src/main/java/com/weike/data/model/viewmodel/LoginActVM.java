package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.base.BaseVM;
import com.weike.data.business.home.HomeActivity;
import com.weike.data.config.Config;
import com.weike.data.model.req.GetVerificationCodeReq;
import com.weike.data.model.req.LoginByCodeReq;
import com.weike.data.model.req.LoginByPwdReq;
import com.weike.data.model.resp.GetVerificationCodeResp;
import com.weike.data.model.resp.LoginByCodeResp;
import com.weike.data.model.resp.LoginByPwdResp;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.AccountValidatorUtil;
import com.weike.data.util.ActivitySkipUtil;
import com.weike.data.util.CountDownUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by LeoLu on 2018/5/22.
 * 登录模块用的VM
 */
public class LoginActVM extends BaseVM {

    //120c83f76074f8684e2

    /**
     * 是否用密码登录
     */
    public ObservableField<Boolean> isPwdLogin = new ObservableField<>(true);
    /**
     * 手机号码
     */
    public ObservableField<String> phoneNum = new ObservableField<>("");

    /**
     * 是否显示密码
     */
    public ObservableField<Boolean> isShowPwd = new ObservableField<>(false);
    /**
     * 登录密码类型
     */
    public ObservableField<String> typeText = new ObservableField<>("使用验证码登录");

    /*
     *密码
     */
    public ObservableField<String> pwd = new ObservableField<>();

    /**
     *
     */
    public ObservableField<String> getVCode = new ObservableField<>("获取验证码");
    /**
     *
     */
    public ObservableField<Boolean> isGetVCode = new ObservableField<>(true);

    /**
     * 手机验证码
     */

    public ObservableField<Integer> showPwdIc = new ObservableField<>(R.mipmap.icon_see);
    /**
     * 验证码EditText
     */
    public ObservableField<String> smsCode = new ObservableField<>();

    public Activity activity;


    public LoginActVM(Activity activity){
        super(activity);
        this.activity = activity;
    }

    public void showPwd(){
        if (isShowPwd.get()) {
            isShowPwd.set(false);
            showPwdIc.set(R.mipmap.icon_see);
        } else {
            showPwdIc.set(R.mipmap.icon_hide);
            isShowPwd.set(true);
        }
    }

    /**
     * 获取验证码
     */
    public void getVerificationCode() {
        //TODO  get 验证码 叼你老母

        if (TextUtils.isEmpty(phoneNum.get())) {
            ToastUtil.showToast("手机号码不能为空");
            return;
        }

        if (!AccountValidatorUtil.isMobile(phoneNum.get())) {
            ToastUtil.showToast("手机号码格式错误");
            return;
        }

        GetVerificationCodeReq req = new GetVerificationCodeReq();
        req.phoneNumber = phoneNum.get();
        req.sign = SignUtil.signData(req);


        RetrofitFactory.getInstance().getService().postAnything(req,Config.GET_SMS_CODE)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetVerificationCodeResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetVerificationCodeResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetVerificationCodeResp> getVerificationCodeRespBaseResp) throws Exception {
                    if (Integer.parseInt(getVerificationCodeRespBaseResp.getResult()) == 1) {
                        ToastUtil.showToast("验证码获成功,请查看短信");
                        countGetVCode();
                        return;
                    } else {
                        ToastUtil.showToast("获取验证码失败");
                    }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    /**
     * 验证码倒计时
     */
    private void countGetVCode(){
        isGetVCode.set(false);
        CountDownUtil util = new CountDownUtil();
        util.setListener(new CountDownUtil.OnTimeTickListener() {
            @Override
            public void onTick(long min) {
                String notice = (min / 1000) +"秒后重新获取";
                getVCode.set(notice);
            }

            @Override
            public void onFinish() {
                isGetVCode.set(true);
                getVCode.set("获取验证码");

            }
        });
        util.start();
    }

    /**
     * 切换登录类型
     */
    public void switchLoginType() {
        isPwdLogin.set(isPwdLogin.get() ? false : true);
        typeText.set(isPwdLogin.get() ?
                WKBaseApplication.getInstance().getString(R.string.login_use_code) : WKBaseApplication.getInstance().getString(R.string.login_use_pwd));
    }

    public void login() {
        // TODO  登录
        if (isPwdLogin.get()) {
            loginForPwd();
            LogUtil.d("acthome","pwd login");
        } else {
            LogUtil.d("acthome","code login");
            loginForCode();
        }
    }


    /**
     * 验证码登录
     */
    private void loginForCode() {
        LoginByCodeReq req = new LoginByCodeReq();
        req.phoneNumber = phoneNum.get();
        req.code = smsCode.get();
        req.igNo = JPushInterface.getRegistrationID(WKBaseApplication.getInstance());;
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.LOGIN_FOR_CODE)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<LoginByCodeResp>>() {

                })).subscribe(new BaseObserver<BaseResp<LoginByCodeResp>>() {
            @Override
            protected void onSuccess(BaseResp<LoginByCodeResp> loginByPwdResp) throws Exception {

                if(Integer.parseInt(loginByPwdResp.getResult()) == 1) {
                    String token = loginByPwdResp.getDatas().getToken();
                    SpUtil.getInstance().saveCurrentToken(token); //登录保存令牌
                    ActivitySkipUtil.skipAnotherAct(activity, HomeActivity.class, true);
                } else {
                    ToastUtil.showToast(loginByPwdResp.getMessage());
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    @BindingAdapter({"updateInputType"})
    public static void updateInputType(EditText editText , boolean isShowPwd){
        if (isShowPwd) {// 显示密码
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editText.setSelection(editText.getText().toString().length());
        } else {// 隐藏密码

            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editText.setSelection(editText.getText().toString().length());

        }
    }

    /**
     * 密码登录
     */
    private void loginForPwd() {
        LoginByPwdReq req = new LoginByPwdReq();
        req.phoneNumber = phoneNum.get();
        LogUtil.d("LoginActVm","" +JPushInterface.getRegistrationID(WKBaseApplication.getInstance()));
        req.igNo = JPushInterface.getRegistrationID(WKBaseApplication.getInstance());;
        req.password = pwd.get();
        req.sign = SignUtil.signData(req);



        RetrofitFactory.getInstance().getService().postAnything(req, Config.LOGIN_FOR_ACCOUNT)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<LoginByPwdResp>>() {

                })).subscribe(new BaseObserver<BaseResp<LoginByPwdResp>>() {
            @Override
            protected void onSuccess(BaseResp<LoginByPwdResp> loginByPwdResp) throws Exception {
                if (Integer.parseInt(loginByPwdResp.getResult()) == 1) {
                    String token = loginByPwdResp.getDatas().token;
                    SpUtil.getInstance().saveCurrentToken(token); //登录保存令牌
                    ActivitySkipUtil.skipAnotherAct(activity, HomeActivity.class, true);
                } else {
                    ToastUtil.showToast(loginByPwdResp.getMessage());
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

}
