package com.weike.data.business.msg;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.weike.data.R;
import com.weike.data.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by LeoLu on 2018/5/21.
 *
 * 消息的Fragment
 */
public class MsgFragment extends BaseFragment {


    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_msg_notice;
    }

    @Override
    protected void loadFinish(View view) {
        DataBindingUtil.bind(view);
    }
}
