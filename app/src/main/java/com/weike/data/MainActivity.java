package com.weike.data;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.req.LoginByPwdReq;
import com.weike.data.model.resp.LoginByCodeResp;
import com.weike.data.model.resp.LoginByPwdResp;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.network.RetrofitService;
import com.weike.data.network.callback.HttpCallBack;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.TransformerUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //testData();

    }

    public void testUploadFile() {

    }


    private void testData() {

        LoginByPwdReq req = new LoginByPwdReq();
        req.appKey = "android";
        req.password = "123456";
        req.phoneNumber = "15766506263";
        req.sign = "62e1f24949dfad11db7d22fa59887c72";




        RetrofitFactory.getInstance().getService().postAnything(req,Config.LOGIN_FOR_ACCOUNT)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<LoginByPwdResp>>(){

                })).subscribe(new BaseObserver<BaseResp<LoginByPwdResp>>() {
            @Override
            protected void onSuccess(BaseResp<LoginByPwdResp> loginByPwdResp) throws Exception {
                LogUtil.d("acthome","getToken:" + loginByPwdResp.getDatas().token);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });


    }

}
