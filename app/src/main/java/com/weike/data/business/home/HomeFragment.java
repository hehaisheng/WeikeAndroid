package com.weike.data.business.home;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.base.BaseFragment;
import com.weike.data.databinding.FragmentHomeBinding;
import com.weike.data.model.viewmodel.HomeFragmentVM;

/**
 * Created by LeoLu on 2018/5/21.
 *
 * 首页的Fragment
 */
public class HomeFragment extends BaseFragment {

    FragmentHomeBinding binding;

    HomeFragmentVM vm;

    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void loadFinish(View view) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);

        binding.swipeRefreshLayout.setColorSchemeColors(WKBaseApplication.getInstance().getResources().getColor((R.color.color_41BCF6)));
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.swipeRefreshLayout.setRefreshing(false);
                    }
                },300);
            }
        });
        vm = new HomeFragmentVM((Activity) context);
        binding.setHomeFragmentVM(vm);


        return binding.getRoot();
    }


}
