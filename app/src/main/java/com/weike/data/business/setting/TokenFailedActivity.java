package com.weike.data.business.setting;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.weike.data.R;
import com.weike.data.business.login.LoginActivity;
import com.weike.data.util.ActivitySkipUtil;

import butterknife.ButterKnife;

public class TokenFailedActivity extends Activity {
//    @OnClick(R.id.btn_dialog_apply_create_company_confirm_rightbutton)
//    public void onClick(View view){
//        sendBroadcast(new Intent(BaseActivity.ACTION_OUT_OF_LOGIN));
//        ActivitySkipUtil.skipAnotherAct(this, LoginActivity.class);
//        finish();
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_apply_create_company_comfirm_orginal_2);
        ButterKnife.bind(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivitySkipUtil.skipAnotherAct(TokenFailedActivity.this, LoginActivity.class);
            }
        },300);

    }

    @Override
    public void onBackPressed() {

    }
}
