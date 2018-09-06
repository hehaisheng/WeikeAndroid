package com.weike.data.business.setting;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.databinding.ActivityAppSettingBinding;
import com.weike.data.model.business.User;
import com.weike.data.model.req.UpdatePushReq;
import com.weike.data.model.viewmodel.AppSettingActVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.DialogManager;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.TransformerUtils;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by LeoLu on 2018/6/1.
 * app 设置页面
 */
public class AppSettingActivity extends BaseActivity {

    ActivityAppSettingBinding binding;
    AppSettingActVM vm;

    LinearLayout  checkLayout;
    RelativeLayout relativeLayout;
    ProgressBar progressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_app_setting);
        vm = new AppSettingActVM(this);
        binding.setAppSettingVM(vm);
        checkLayout=findViewById(R.id.check_version);
        relativeLayout=(RelativeLayout) findViewById(R.id.relative_layout);

        checkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setVisibility(View.VISIBLE);
                //因为一进入就会做更新操作,所以这里不做操作
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        relativeLayout.setVisibility(View.GONE);
                        DialogManager.showDialog(AppSettingActivity.this,"现为最新版本");
                    }
                },800);
            }
        });

        setLeftText("应用设置");
        setCenterText("");
        setRightText("");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        User u = SpUtil.getInstance().getUser();
        u.isOpenPush = vm.isCheck.get();
        SpUtil.getInstance().saveNewsUser(u);

        initPush();
    }

    private void initPush(){
        User user = SpUtil.getInstance().getUser();
        boolean isOpenPush = user.isOpenPush;
        String igNo = "";
        if (isOpenPush) {
            igNo = JPushInterface.getRegistrationID(WKBaseApplication.getInstance());
        }
        UpdatePushReq req = new UpdatePushReq();
        req.jgNo = igNo;
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.UPDATE_PUSH)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp>() {

                })).subscribe(new BaseObserver<BaseResp>() {
            @Override
            protected void onSuccess(BaseResp loginByPwdResp) throws Exception {


            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }
}
