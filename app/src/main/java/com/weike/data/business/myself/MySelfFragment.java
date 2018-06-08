package com.weike.data.business.myself;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weike.data.R;
import com.weike.data.base.BaseFragment;
import com.weike.data.databinding.FragmentPersonalCenterBinding;
import com.weike.data.model.viewmodel.PersonalFragmentVM;

/**
 * Created by LeoLu on 2018/5/21.
 * 个人的Fragment
 */
public class MySelfFragment extends BaseFragment {

    private FragmentPersonalCenterBinding binding;

    private PersonalFragmentVM vm;

    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_personal_center;
    }

    @Override
    protected void loadFinish(View view) {



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_personal_center,container,false);

        vm = new PersonalFragmentVM((FragmentActivity) context);
        binding.setPersonalVM(vm);

        return binding.getRoot();
    }
}
