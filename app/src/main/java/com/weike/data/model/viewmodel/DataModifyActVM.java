package com.weike.data.model.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigTitle;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.TitleParams;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.base.BaseVM;
import com.weike.data.business.setting.ModifyPhoneNumAct;
import com.weike.data.config.Config;
import com.weike.data.model.business.User;
import com.weike.data.model.req.GetUserInfoReq;
import com.weike.data.model.resp.GetUserInfoResp;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.ActivitySkipUtil;
import com.weike.data.util.LQRPhotoSelectUtils;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.TransformerUtils;

import kr.co.namee.permissiongen.PermissionGen;

public class DataModifyActVM extends BaseVM {

    LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    public ObservableField<String> photoUrl = new ObservableField<>();

    public ObservableField<String> phoneNum = new ObservableField<>();

    public ObservableField<String> userName = new ObservableField<>();

    public ObservableField<String> job = new ObservableField<>();

    public ObservableField<String> location = new ObservableField<>();

    public ObservableField<String> email = new ObservableField<>();

    public ObservableField<String> detailAddress = new ObservableField<>();

    public DataModifyActVM(Activity activity) {
        this.activity = activity;

        User user = SpUtil.getInstance().getUser();
        photoUrl.set(user.iconUrl);
        phoneNum.set(user.phoneNumber);
        userName.set(user.userName);
        job.set(user.job);
        location.set(user.address);
        email.set(user.email);
        init();
    }

    public void modifyPhone(){
        ActivitySkipUtil.skipAnotherAct(activity, ModifyPhoneNumAct.class);
    }
    public void setmLqrPhotoSelectUtils(LQRPhotoSelectUtils mLqrPhotoSelectUtils) {
        this.mLqrPhotoSelectUtils = mLqrPhotoSelectUtils;
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
                    userName.set(getUserInfoRespBaseResp.getDatas().userName);
                    phoneNum.set(getUserInfoRespBaseResp.getDatas().phoneNumber );
                    photoUrl.set(getUserInfoRespBaseResp.getDatas().photoUrl);
                    job.set(getUserInfoRespBaseResp.getDatas().occupation);
                    email.set(getUserInfoRespBaseResp.getDatas().email);
                    detailAddress.set(getUserInfoRespBaseResp.getDatas().detailAddress);

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



    public void photoClick(){


            final String[] items = {"拍照", "从相册选择"};
            new CircleDialog.Builder()
                    .configDialog(new ConfigDialog() {
                        @Override
                        public void onConfig(DialogParams params) {
                            params.backgroundColorPress = Color.CYAN;
                            //增加弹出动画
                        }
                    })
                    .setTitle("提示")
                    .configTitle(new ConfigTitle() {
                        @Override
                        public void onConfig(TitleParams params) {
                        }
                    })
                    .setItems(items, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int
                                position, long id) {
                            if (position == 0) {
                                //拍照
                                PermissionGen.with(activity)
                                        .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.CAMERA
                                        ).request();
                            } else {
                                //相册
                                PermissionGen.needPermission(activity,
                                        LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                );
                            }
                        }
                    })
                    .setNegative("取消", null)
                    .show(((FragmentActivity) activity).getSupportFragmentManager());

    }

}
