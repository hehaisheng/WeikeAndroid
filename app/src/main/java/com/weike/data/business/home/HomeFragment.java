package com.weike.data.business.home;

import android.app.Activity;
import android.databinding.DataBindingUtil;
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
import com.weike.data.databinding.FragmentHomeBinding;
import com.weike.data.model.viewmodel.HomeFragmentVM;
import com.youth.banner.Banner;

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
        vm = new HomeFragmentVM((Activity) context);
        binding.setHomeFragmentVM(vm);


        return binding.getRoot();
    }


}
