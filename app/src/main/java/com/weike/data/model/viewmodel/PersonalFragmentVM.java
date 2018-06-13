package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;
import android.support.v4.app.FragmentActivity;

import com.google.gson.reflect.TypeToken;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.base.BaseVM;
import com.weike.data.business.setting.AppSettingActivity;
import com.weike.data.business.myself.CustomerCenterActivity;
import com.weike.data.business.myself.DataModifyActivity;
import com.weike.data.business.myself.MyIntegralActivity;
import com.weike.data.business.myself.MyQRCodeActivity;
import com.weike.data.business.setting.ServiceSettingActivity;
import com.weike.data.business.myself.VipOpenUpActivity;
import com.weike.data.config.Config;
import com.weike.data.model.business.User;
import com.weike.data.model.req.GetUserInfoReq;
import com.weike.data.model.resp.GetUserInfoResp;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.ActivitySkipUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.TransformerUtils;

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

    public ObservableField<String> photoUrl = new ObservableField<>();

    public ObservableField<String> nickName = new ObservableField<>("未知用户");

    public void openUpVip(){
        ActivitySkipUtil.skipAnotherAct(activity, VipOpenUpActivity.class);
    }

    public PersonalFragmentVM(FragmentActivity activity) {
        super(activity);
        LogUtil.d("acthome","init");
        init();
    }

    private void init(){
        GetUserInfoReq req = new GetUserInfoReq();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.USER_INFO)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetUserInfoResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetUserInfoResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetUserInfoResp> getUserInfoRespBaseResp) throws Exception {
                if (getUserInfoRespBaseResp.getResult() == 0) {
                    nickName.set(getUserInfoRespBaseResp.getDatas().userName);
                    phoneNum.set("电话号码：" + getUserInfoRespBaseResp.getDatas().phoneNumber );
                    photoUrl.set(getUserInfoRespBaseResp.getDatas().photoUrl);

                    if(getUserInfoRespBaseResp.getDatas().memberLevel == 1) {
                        vipTime.set("开通");
                    } else {
                        vipTime.set(getUserInfoRespBaseResp.getDatas().timeoutDate + "   到期");
                    }

                    User user = SpUtil.getInstance().getUser();
                    user.phoneNumber = getUserInfoRespBaseResp.getDatas().phoneNumber;
                    user.iconUrl = getUserInfoRespBaseResp.getDatas().photoUrl;
                    user.userName = getUserInfoRespBaseResp.getDatas().userName;
                    user.email = getUserInfoRespBaseResp.getDatas().email;
                    user.job = getUserInfoRespBaseResp.getDatas().occupation;
                    user.address  = getUserInfoRespBaseResp.getDatas().detailAddress;

                    SpUtil.getInstance().saveNewsUser(user);

                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
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
        ActivitySkipUtil.skipAnotherAct(activity, MyQRCodeActivity.class);
    }

    /**
     * 我的积分
     */
    public void jumpToMyIntegral(){
        ActivitySkipUtil.skipAnotherAct(activity, MyIntegralActivity.class);
    }

    /**
     * 客服中心
     */
    public void jumpToCustomer(){
        ActivitySkipUtil.skipAnotherAct(activity, CustomerCenterActivity.class);
    }

    /**
     * 服务中心
     */
    public void jumpServiceSetting(){
        ActivitySkipUtil.skipAnotherAct(activity, ServiceSettingActivity.class);
    }

    /**
     * app 设置
     */
    public void jumpAppSetting(){
        ActivitySkipUtil.skipAnotherAct(activity, AppSettingActivity.class);
    }
}
