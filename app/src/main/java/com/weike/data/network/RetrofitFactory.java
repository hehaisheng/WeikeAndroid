package com.weike.data.network;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.weike.data.BuildConfig;
import com.weike.data.config.Config;
import com.weike.data.util.LoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by LeoLu on 2018/5/23.
 */

public class RetrofitFactory {

    public static RetrofitFactory instance ;

    private RetrofitService service;


    public static RetrofitFactory getInstance(){
        if (instance == null) {
            instance = new RetrofitFactory();
        }
        return instance;
    }


    public RetrofitFactory(){

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)//设置超时时间
                .retryOnConnectionFailure(true);


        client.addInterceptor(new LoggingInterceptor());


        Retrofit mRetrofit=new Retrofit.Builder()
                .baseUrl(Config.APP_SERVER)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())//添加gson转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxjava转换器
                .build();
        service =mRetrofit.create(RetrofitService.class);
    }

    public RetrofitService getService(){
        return service;
    }
}
