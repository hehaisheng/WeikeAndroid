package com.weike.data.business.client;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.weike.data.R;
import com.weike.data.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientTagActivity extends BaseActivity {

    @BindView(R.id.recyclerview_client_option)
    public RecyclerView recycle_option;

    @BindView(R.id.recyclerview_client_content)
    public RecyclerView recycle_client_list;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_tag);
        ButterKnife.bind(this);

        setCenterText("");
        setRightText("");
        setLeftText("客户标签");
    }
}
