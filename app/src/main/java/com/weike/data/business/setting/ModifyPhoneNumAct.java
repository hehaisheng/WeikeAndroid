package com.weike.data.business.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.req.ChangePhoneNumReq;
import com.weike.data.model.req.GetVCodeAfterLoginReq;
import com.weike.data.model.resp.ChangePhoneNumResp;
import com.weike.data.model.resp.GetVCodeAfterLoginResp;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.CountDownUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyPhoneNumAct extends BaseActivity {
    public String KEY_PHONE_NUM = "com.weike.data.KEY_PHONE_NUM";

    @BindView(R.id.tv_title_1)
    public TextView tv_title1;

    @BindView(R.id.tv_title_2)
    public TextView tv_phone;

    @BindView(R.id.ed_inputext)
    public EditText inputText;

    @BindView(R.id.tv_get_code)
    public TextView tv_getCode;

    @BindView(R.id.btn_click)
    public Button btn_click;

    private boolean isFirst = true;

    private String lastPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone);
        ButterKnife.bind(this);

        setCenterText("");
        setLeftText("修改号码");
        setRightText("");
        init();
    }

    private void init(){

        tv_phone.setText(SpUtil.getInstance().getUser().phoneNumber);
    }


    @OnClick(R.id.btn_click)
    public void submit(View view){



        if (isFirst) {

            if (TextUtils.isEmpty(inputText.getText().toString())) {
                ToastUtil.showToast("手机号不能为空");
                return;
            }

            lastPhone = inputText.getText().toString();
            tv_getCode.setVisibility(View.VISIBLE);
            getCode(inputText.getText().toString());

            tv_title1.setText("验证码已经发送:");
            tv_phone.setText(lastPhone);

            btn_click.setText("确认");
            inputText.setText("");
            inputText.setHint("请输入验证码");

            isFirst = false;
        } else {
            ChangePhoneNumReq req = new ChangePhoneNumReq();
            req.phoneNumber = lastPhone;
            req.code = inputText.getText().toString();
            req.sign = SignUtil.signData(req);


            RetrofitFactory.getInstance().getService().postAnything(req, Config.CHANGE_PHONE_NUM)
                    .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<ChangePhoneNumResp>>(){

                    })).subscribe(new BaseObserver<BaseResp<ChangePhoneNumResp>>() {
                @Override
                protected void onSuccess(BaseResp<ChangePhoneNumResp> getVerificationCodeRespBaseResp) throws Exception {
                    if (Integer.parseInt(getVerificationCodeRespBaseResp.getResult()) == 1) {
                        ToastUtil.showToast("修改成功");
                        finish();
                        return;
                    } else {
                        ToastUtil.showToast("修改失败");
                    }
                }

                @Override
                protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

                }
            });
        }
    }

    @OnClick(R.id.tv_get_code)
    public void clickGetClick(View view){
        getCode(lastPhone);
    }

    private void getCode(String phone){
        GetVCodeAfterLoginReq req = new GetVCodeAfterLoginReq();
        req.phoneNumber = phone;
        req.type = 3;
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);


        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_CODE_AFTER_LOGIN)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetVCodeAfterLoginResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetVCodeAfterLoginResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetVCodeAfterLoginResp> getVerificationCodeRespBaseResp) throws Exception {
                if (Integer.parseInt(getVerificationCodeRespBaseResp.getResult()) == 1) {
                    ToastUtil.showToast("验证码获成功,请查看短信");
                    countGetVCode();
                    return;
                } else {
                    ToastUtil.showToast("获取验证码失败");
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    private void countGetVCode(){
        tv_getCode.setClickable(false);
        tv_getCode.setTextColor(getResources().getColor(R.color.color_bebebe));
        CountDownUtil util = new CountDownUtil();
        util.setListener(new CountDownUtil.OnTimeTickListener() {
            @Override
            public void onTick(long min) {
                String notice = (min / 1000) +"秒后重新获取";
                tv_getCode.setText(notice);
            }

            @Override
            public void onFinish() {
                tv_getCode.setClickable(true);
                tv_getCode.setText("获取验证码");
                tv_getCode.setTextColor(getResources().getColor(R.color.color_41BCF6));
            }
        });
        util.start();
    }
}
