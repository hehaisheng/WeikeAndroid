package com.weike.data.business.client;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;

/**
 * Created by LeoLu on 2018/6/7.
 */

@SuppressLint("ValidFragment")
public class ClientLogFragment extends BaseFragment {

    private RecyclerView recyclerView;

    private BaseDataBindingAdapter adapter;

    private String clientId;

    @SuppressLint("ValidFragment")
    public ClientLogFragment(String clientId) {
        this.clientId = clientId;
    }

    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_client_service_msg;
    }

    @Override
    protected void loadFinish(View view) {


    }
}
