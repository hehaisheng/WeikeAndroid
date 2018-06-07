package com.weike.data.business.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.util.ActivitySkipUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeoLu on 2018/6/1.
 * 服务设置页面
 */
public class ServiceSettingActivity extends BaseActivity{


    @OnClick(R.id.ll_attribute_manager)
    public void attrManager(View view){
        ActivitySkipUtil.skipAnotherAct(this,AttrManagerActivity.class);
    }

    @OnClick(R.id.ll_handle_working_display)
    public void handleWorking(View view){

    }

    @OnClick(R.id.ll_client_tag_setting)
    public void clientTagSetting(View view){
        ActivitySkipUtil.skipAnotherAct(this,ClientTagSettingActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_setting);
        ButterKnife.bind(this);
        setLeftText("服务设置");
        setCenterText("");
        setRightText("");
    }
}
