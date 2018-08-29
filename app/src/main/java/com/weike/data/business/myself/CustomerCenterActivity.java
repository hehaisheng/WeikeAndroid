package com.weike.data.business.myself;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.business.web.WebActivity;
import com.weike.data.config.Config;
import com.weike.data.model.req.UserFeedBackReq;
import com.weike.data.model.resp.UserFeedBackResp;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by LeoLu on 2018/6/1.
 */

public class CustomerCenterActivity extends BaseActivity {


    @BindView(R.id.ed_activity_customer_center)
    public EditText inputText;

    @BindView(R .id.tv_qq)
    public TextView qq;

    @BindView(R.id.tv_phone)
    public TextView phoneNum;

    @OnClick(R.id.ll_goto_web)
    public void goToWeb(View view){
        WebActivity.startActivity(this,"www.duoqiandan.com");
    }

    @OnClick(R.id.btn_submit)
    public void submit(View view){
        if (TextUtils.isEmpty(inputText.getText().toString())){
            ToastUtil.showToast("内容不能为空");
            return ;
        }


        UserFeedBackReq req = new UserFeedBackReq();
        req.content = inputText.getText().toString();
        req.token = SpUtil.getInstance().getCurrentToken();

        RetrofitFactory.getInstance().getService().postAnything(req, Config.USER_FEEDBACK)
        .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<UserFeedBackResp>>(){

        })).subscribe(new BaseObserver<BaseResp<UserFeedBackResp>>() {
            @Override
            protected void onSuccess(BaseResp<UserFeedBackResp> userFeedBackRespBaseResp) throws Exception {
                ToastUtil.showToast("谢谢您的反馈");
                finish();

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    @OnClick(R.id.tv_phone)
    public void phoneClick(View view){
        Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum.getText().toString()));
        startActivity(dialIntent);
    }

    @OnLongClick(R.id.tv_qq)
    public boolean qqClick(View view){
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        manager.setText(qq.getText().toString());
        ToastUtil.showToast("复制成功");
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_center);
        ButterKnife.bind(this);
        setLeftText("客服中心");
        setRightText("");
        setCenterText("");
    }
}
