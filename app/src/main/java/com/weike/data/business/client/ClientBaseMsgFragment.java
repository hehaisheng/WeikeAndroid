package com.weike.data.business.client;

import android.view.View;
import android.widget.LinearLayout;

import com.weike.data.R;
import com.weike.data.base.BaseFragment;

/**
 * Created by LeoLu on 2018/6/4.
 * 客户基本信息Fragment
 */

public class ClientBaseMsgFragment extends BaseFragment {

    private LinearLayout phoneLinearLayout;


    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_client_base_msg;
    }

    @Override
    protected void loadFinish(View view) {


    }
}
