package com.weike.data.base;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.content.Context;

import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by LeoLu on 2018/5/23.
 */

public abstract class BaseObserver<T>  implements Observer<T> {

    protected Context mContext;

    public BaseObserver(Context cxt) {
        this.mContext = cxt;
    }

    public BaseObserver() {

    }


    @Override
    public void onSubscribe(Disposable d) {
        onRequestStart();

    }

    @Override
    public void onNext(T tBaseEntity) {



        onRequestEnd();
        try {
            onSuccess(tBaseEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(Throwable e) {
        LogUtil.d("BaseObServer","Error---->" +JsonUtil.GsonString(e));
        onRequestEnd();
        try {
            if (e instanceof ConnectException
                    || e instanceof TimeoutException
                    || e instanceof NetworkErrorException
                    || e instanceof UnknownHostException) {
                onFailure(e, true);
            } else {
                onFailure(e, false);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onComplete() {
    }

    /**
     * 返回成功
     *
     * @param t
     * @throws Exception
     */
    protected abstract void onSuccess(T t) throws Exception;

    /**
     * 返回成功了,但是code错误
     *
     * @param t
     * @throws Exception
     */
    protected void onCodeError(T t) throws Exception {
    }

    /**
     * 返回失败
     *
     * @param e
     * @param isNetWorkError 是否是网络错误
     * @throws Exception
     */
    protected abstract void onFailure(Throwable e, boolean isNetWorkError) throws Exception;

    protected void onRequestStart() {
    }

    protected void onRequestEnd() {

    }

}
