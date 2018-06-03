package com.weike.data.business.myself;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.weike.data.R;
import com.weike.data.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by LeoLu on 2018/6/1.
 * 服务设置页面
 */
public class ServiceSettingActivity extends BaseActivity{


    public EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_setting);
        ButterKnife.bind(this);
    }
}
