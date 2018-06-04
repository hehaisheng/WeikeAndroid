package com.weike.data.business.myself;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseReq;
import com.weike.data.base.BaseResp;
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

/**
 * Created by LeoLu on 2018/6/1.
 */

public class CustomerCenterActivity extends BaseActivity {


    @BindView(R.id.ed_activity_customer_center)
    public EditText inputText;


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

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_center);
        ButterKnife.bind(this);
    }
}
