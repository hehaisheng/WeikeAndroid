package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;
import android.support.v4.app.FragmentActivity;

import com.google.gson.reflect.TypeToken;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.base.BaseVM;
import com.weike.data.business.client.SendSmsActivity;
import com.weike.data.business.myself.CustomerCenterActivity;
import com.weike.data.business.myself.MyIntegralActivity;
import com.weike.data.business.myself.MyQRCodeActivity;
import com.weike.data.business.myself.MySelfFragment;
import com.weike.data.business.myself.VipOpenUpActivity;
import com.weike.data.business.setting.AppSettingActivity;
import com.weike.data.business.setting.ServiceSettingActivity;
import com.weike.data.business.web.WebActivity;
import com.weike.data.config.Config;
import com.weike.data.model.business.User;
import com.weike.data.model.req.AgreeRequest;
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

    public ObservableField<String> nickName = new ObservableField<>("请输入姓名");

    public ObservableField<String> integral = new ObservableField<>("0.00");

    public void openUpVip(){
        ActivitySkipUtil.skipAnotherAct(activity, VipOpenUpActivity.class);
    }


    public boolean   isSelected=false;


    private BaseFragment fragment;

    public PersonalFragmentVM(BaseFragment fragment, Activity activity) {
        this.activity = activity;
        this.fragment = fragment;
    }

    public PersonalFragmentVM(FragmentActivity activity) {
        super(activity);
        LogUtil.d("acthome","init");
        init();
    }

    public void iconClick(){

    }

    public void init(){
        GetUserInfoReq req = new GetUserInfoReq();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.USER_INFO)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetUserInfoResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetUserInfoResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetUserInfoResp> getUserInfoRespBaseResp) throws Exception {
                if (Integer.parseInt(getUserInfoRespBaseResp.getResult()) == 0) {
                    nickName.set(getUserInfoRespBaseResp.getDatas().userName);
                    phoneNum.set("绑定手机：" + getUserInfoRespBaseResp.getDatas().phoneNumber );
                    photoUrl.set(getUserInfoRespBaseResp.getDatas().photoUrl);
                    integral.set(getUserInfoRespBaseResp.getDatas().currentIntegral);
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
        ActivitySkipUtil.skipAnotherAct(activity, SendSmsActivity.class);
//        Intent intent = new Intent(fragment.getContext(), DataModifyActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        fragment.startActivityForResult(intent,200);


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

    /**
     * 跳转到协议
     */
    public void jumpToLocal(){
        if(isSelected){
            //调用取消的链接
            AgreeRequest req = new AgreeRequest();
            req.token = SpUtil.getInstance().getCurrentToken();
            req.sign = SignUtil.signData(req);
            req.isOpen=0;

            RetrofitFactory.getInstance().getService().postAnything(req, Config.CHANGE_AGREE)
                    .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp>(){

                    })).subscribe(new BaseObserver<BaseResp>() {
                @Override
                protected void onSuccess(BaseResp getUserInfoRespBaseResp) throws Exception {

                    if(getUserInfoRespBaseResp.getResult().equals("1")){
                        MySelfFragment mySelfFragment=(MySelfFragment) fragment;
                        mySelfFragment.init();
//                        isSelected=false;
//                        WKBaseApplication.getInstance().hasSelectLocal=false;
                    }
                }

                @Override
                protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

                }
            });
        }else{
            WebActivity.startActivity( activity,"app.duoqiandan.com/local/localStorgeArguments.html","local");
        }

    }

}
