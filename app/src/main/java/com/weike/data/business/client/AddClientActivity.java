package com.weike.data.business.client;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.weike.data.R;
import com.weike.data.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by LeoLu on 2018/5/31.
 * 添加客户和修改客户信息都是这个Activity
 */
public class AddClientActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_tag);

    }
}
