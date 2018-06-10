package com.weike.data.business.client;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;

/**
 * Created by LeoLu on 2018/6/7.
 */

public class ClientLogFragment extends BaseFragment {

    private RecyclerView recyclerView;

    private BaseDataBindingAdapter adapter;

    @Override
    protected int setUpLayoutId() {
        return 0;
    }

    @Override
    protected void loadFinish(View view) {
        recyclerView = view.findViewById(R.id.recycler_log_list);

    }
}
