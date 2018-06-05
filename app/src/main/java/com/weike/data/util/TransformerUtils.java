package com.weike.data.util;

import android.content.Intent;
import android.databinding.BaseObservable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weike.data.WKBaseApplication;
import com.weike.data.base.BaseResp;
import com.weike.data.model.resp.LoginByPwdResp;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.flowable.FlowableOnErrorReturn;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by LeoLu on 2018/5/23.
 */

public class TransformerUtils {
    public static <T> ObservableTransformer<ResponseBody,T> jsonCompass(final TypeToken<T> typeToken){
        try {
            return responseBodyObservable -> responseBodyObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(responseBody -> {
                        try {
                            String responseStr = responseBody.string();


                            BaseResp baseResp = JsonUtil.GsonToBean(responseStr,BaseResp.class);
                            if (baseResp.getResult() == 100040){
                                WKBaseApplication.getInstance().getApplicationContext()
                                        .sendBroadcast(new Intent());
                            }



                            T t = JsonUtil.GsonToBean(responseStr , typeToken.getType());
                            LogUtil.d("Transformer",responseStr);
                            return Observable.just(t);
                        }catch (Exception e) {
                            return Observable.error(new RuntimeException("错误"));
                        }

                    });


        }catch (Exception e) {
            e.printStackTrace();
            return responseBodyObservable -> responseBodyObservable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).flatMap(responseBody -> {
                        return Observable.error(new RuntimeException("错误"));
                    });
        }


    }

}
