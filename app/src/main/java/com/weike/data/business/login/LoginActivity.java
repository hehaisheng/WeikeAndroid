package com.weike.data.business.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.databinding.ActivityLoginBinding;
import com.weike.data.model.viewmodel.LoginActVM;

/**
 * Created by LeoLu on 2018/5/21.
 */

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding ;
    LoginActVM vm;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        vm = new LoginActVM(this);

        binding.setLoginVM(vm);
    }

    public void onClick(View view) {

    }

}

