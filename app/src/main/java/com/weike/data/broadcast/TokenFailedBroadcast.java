package com.weike.data.broadcast;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.weike.data.util.ToastUtil;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by LeoLu on 2018/6/1.
 * token失效的广播发送
 */
public class TokenFailedBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

    }

    public void clearLocalToken(){

    }
}
