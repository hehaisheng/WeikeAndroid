package com.weike.data.business.client;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.model.viewmodel.MessageItemVM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeoLu on 2018/5/21.
 *
 * 客户 Fragment
 */
public class ClientFragment extends BaseFragment {


    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_client_add;
    }

    @Override
    protected void loadFinish(View view) {

    }
}

