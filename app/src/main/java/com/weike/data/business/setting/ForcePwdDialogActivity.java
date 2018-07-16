package com.weike.data.business.setting;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.weike.data.R;
import com.weike.data.util.ActivitySkipUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ${huneng} on 2018/7/16 10:10
 */

public class ForcePwdDialogActivity extends Activity {

    @OnClick(R.id.btn_dialog_apply_create_company_confirm_rightbutton)
    public void onClick(View view){
        ActivitySkipUtil.skipAnotherAct(this, ForcePwdActivity.class);
        finish();
    }

    @BindView(R.id.tv_dialog_apply_create_company_confirm_content)
    public TextView content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_apply_create_company_comfirm_orginal_2);
        ButterKnife.bind(this);
        content.setText("您还没有设置密码,请设置");

    }

    @Override
    public void onBackPressed() {

    }

}
