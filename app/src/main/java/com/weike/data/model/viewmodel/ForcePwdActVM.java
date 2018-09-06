package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.weike.data.R;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.base.BaseVM;
import com.weike.data.config.Config;
import com.weike.data.model.req.ForceModifyPwdReq;
import com.weike.data.model.req.ResetPwdReq;
import com.weike.data.model.resp.ResetPwdResp;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

/**
 * ${huneng} on 2018/7/16 10:57
 */

public class ForcePwdActVM extends BaseVM {

    public ObservableField<Integer> pwdImageId = new ObservableField<>(R.mipmap.icon_see);

    public ObservableField<Integer> pwd2ImageId = new ObservableField<>(R.mipmap.icon_see);

    public ObservableField<String> vCode = new ObservableField<>();

    public ObservableField<String> pwd = new ObservableField<>();

    public ObservableField<String> pwd2 = new ObservableField<>();

    public ObservableField<Boolean> isGetVCode = new ObservableField<>(true);

    public ObservableField<String> vCodeText = new ObservableField<>("获取验证码");

    /**
     * 眼睛标记
     */
    public ObservableField<Boolean> reset1 = new ObservableField<>(false);
    /**
     * 眼睛图案标记
     */
    public ObservableField<Boolean> reset2 = new ObservableField<>(false);

    public ForcePwdActVM(Activity activity) {
        this.activity =activity;
    }


    public void submit(){


        if (TextUtils.isEmpty(pwd.get())
                ||TextUtils.isEmpty(pwd2.get())){
            ToastUtil.showToast("密码不能为空");
            return;
        }

        if (!pwd.get().equals(pwd2.get())) {
            ToastUtil.showToast("两次输入的密码不一致");
            return;
        }


        String passwordString=pwd.get();
        String passwordString2=pwd2.get();

        if(pwd.get().length()<6||pwd2.get().length()<6){
            ToastUtil.showToast("密码最短长度需为6");
        }else{

            String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
            boolean result=passwordString.matches(regex);
            boolean result2=passwordString2.matches(regex);
//            result=true;
//            result2=true;
            if(result&&result2){

                ForceModifyPwdReq req = new ForceModifyPwdReq();
                req.token = SpUtil.getInstance().getCurrentToken();
                req.userPassword = pwd.get();
                req.sign = SignUtil.signData(req);


                RetrofitFactory.getInstance().getService().postAnything(req, Config.ADD_PWD_FORCE)
                        .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp>(){

                        })).subscribe(new BaseObserver<BaseResp>() {
                    @Override
                    protected void onSuccess(BaseResp resetPwdRespBaseResp) throws Exception {
                        if (Integer.parseInt(resetPwdRespBaseResp.getResult()) == 1){
                            ToastUtil.showToast("保存成功");
                            activity.finish();
                        } else {
                            ToastUtil.showToast("保存失败:" + resetPwdRespBaseResp.getMessage());
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

                    }
                });


            }else{
                ToastUtil.showToast("密码需由字母和数字组成");
            }
        }


    }

    public void pwd1Click(){
        if (reset1.get()) {
            reset1.set(false);
            pwdImageId.set(R.mipmap.icon_see);
        } else {
            pwdImageId.set(R.mipmap.icon_hide);
            reset1.set(true);
        }
    }

    public void pwd2Click(){
        if (reset2.get()) {
            reset2.set(false);
            pwd2ImageId.set(R.mipmap.icon_see);
        } else {
            pwd2ImageId.set(R.mipmap.icon_hide);
            reset2.set(true);
        }
    }
}
