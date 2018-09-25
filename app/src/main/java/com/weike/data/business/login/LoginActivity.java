package com.weike.data.business.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.base.BaseActivity;
import com.weike.data.databinding.ActivityLoginBinding;
import com.weike.data.model.viewmodel.LoginActVM;
import com.weike.data.util.NetManager;
import com.weike.data.view.DialogCommonLayout;

/**
 * Created by LeoLu on 2018/5/21.
 */

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding ;
    LoginActVM vm;

    public DialogCommonLayout dialogCommonLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        vm = new LoginActVM(this);
        binding.setLoginVM(vm);
        WKBaseApplication.getInstance().activity=this;


        if(NetManager.newInstance().isNetworkConnected(this)){

        }else{

            show();
        }



    }


    public void show(){
        dialogCommonLayout=findViewById(R.id.common_layout);
        dialogCommonLayout.setContentAndListener("没有网络,请连接网络", new DialogCommonLayout.DialogListener() {
            @Override
            public void handle(String model) {
                if(model.equals("handle")){

                }else if(model.equals("finish")){

                }
            }
        });
    }
    public void onClick(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WKBaseApplication.getInstance().activity=null;

    }
}

