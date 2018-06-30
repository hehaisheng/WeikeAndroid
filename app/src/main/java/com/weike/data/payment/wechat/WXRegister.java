package com.weike.data.payment.wechat;

import android.content.Context;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static com.weike.data.payment.wechat.WxConfig.APP_ID;

/**
 * Created by LeoLu on 2018/6/27.
 */

public final class WXRegister {


    private static IWXAPI api;

    public static IWXAPI getApi(){
        return api;
    }


    public static void rg(Context context){
        if (api == null) {
            api = WXAPIFactory.createWXAPI(context,APP_ID,true);
            api.registerApp(APP_ID);
        }
    }
}
