package com.weike.data.business.home;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weike.data.R;
import com.weike.data.base.BaseFragment;

/**
 * Created by LeoLu on 2018/5/21.
 *
 * 首页的Fragment
 */
public class HomeFragment extends BaseFragment {


    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void loadFinish(View view) {

    }
}
