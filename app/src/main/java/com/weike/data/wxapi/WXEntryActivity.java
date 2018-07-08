package com.weike.data.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.weike.data.base.BaseActivity;
import com.weike.data.payment.wechat.WxConfig;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.ToastUtil;

/**
 * Created by LeoLu on 2018/6/27.
 */

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {


    public static final String WX_SHAERD = "shared";

    /**分享到微信接口**/
    private IWXAPI mWxApi;
    /**分享结果信息**/
    private TextView txtShareResult;
    /**分享结果图片**/
    private ImageView imgShareResult;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWxApi = WXAPIFactory.createWXAPI(this, WxConfig.APP_ID, false);
        mWxApi.registerApp(WxConfig.APP_ID);
        mWxApi.handleIntent(getIntent(), this);

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWxApi.handleIntent(intent, this);
    }


    @Override
    public void onResp(BaseResp baseResp) {
        LogUtil.d("acthome",JsonUtil.GsonString(baseResp));
       if (baseResp.errCode == 0 && baseResp.transaction.equals(WX_SHAERD)){
           ToastUtil.showToast("分享成功");
            finish();
       } else if (baseResp.errCode != 0 && baseResp.transaction.equals(WX_SHAERD)){
           ToastUtil.showToast("分享失败");
           finish();
       }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
