package com.weike.data.business.client;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.business.myself.VipOpenUpActivity;
import com.weike.data.util.ActivitySkipUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ${huneng} on 2018/7/27 11:07
 */

public class ForceOpenVipDialogActivity extends BaseActivity {
    @OnClick(R.id.btn_dialog_apply_create_company_confirm_rightbutton)
    public void onClick(View view){
        ActivitySkipUtil.skipAnotherAct(this, VipOpenUpActivity.class);
        finish();
    }

    @BindView(R.id.tv_dialog_apply_create_company_confirm_content)
    public TextView content;

    @BindView(R.id.btn_dialog_apply_create_company_confirm_rightbutton)
    public TextView check;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_apply_create_company_comfirm_orginal_2);
        ButterKnife.bind(this);
        content.setText("您的会员已过期");
        check.setText("充值");

    }

    @Override
    public void onBackPressed() {

    }
}
