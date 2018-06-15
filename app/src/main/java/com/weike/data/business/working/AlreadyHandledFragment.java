package com.weike.data.business.working;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.weike.data.R;
import com.weike.data.base.BaseFragment;

/**
 * 已处理
 */
public class AlreadyHandledFragment extends BaseFragment{



    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_handle_working;
    }

    @Override
    protected void loadFinish(View view) {
        view.findViewById(R.id.ll_handle_working_sort).setVisibility(View.GONE);
    }


}
