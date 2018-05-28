package com.weike.data.business.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.config.Config;
import com.weike.data.databinding.ActivityLoginBinding;
import com.weike.data.network.RetrofitService;
import com.weike.data.util.LogUtil;

import java.io.IOException;
import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
        vm = new LoginActVM();
        binding.setLoginVM(vm);
    }


    public void onClick(View view) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://114.116.15.19/").build();
        RetrofitService service = retrofit.create(RetrofitService.class);


        RequestBody.create(MediaType.parse(""),"");

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

