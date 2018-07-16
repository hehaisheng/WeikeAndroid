package com.weike.data.business.setting;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.databinding.ActivityForcePwdBinding;
import com.weike.data.model.viewmodel.ForcePwdActVM;

/**
 * ${huneng} on 2018/7/16 10:11
 */

public class ForcePwdActivity extends BaseActivity {

    ActivityForcePwdBinding binding;

    ForcePwdActVM vm;


    @Override
    public void onLeftClick() {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_force_pwd);
        vm = new ForcePwdActVM(this);
        binding.setForcePwdActVM(vm);

        setCenterText("");
        setLeftText("设置密码");
        setRightText("");
    }
}
