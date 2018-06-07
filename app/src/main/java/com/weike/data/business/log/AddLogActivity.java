package com.weike.data.business.log;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.weike.data.R;
import com.weike.data.base.BaseActivity;

/**
 * Created by LeoLu on 2018/5/30.
 * 新建日志页面
 */
public class AddLogActivity extends BaseActivity {

    public static String keyOfLog = "AddLogActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);
        setCenterText("");
        setRightText("");
    }
}
