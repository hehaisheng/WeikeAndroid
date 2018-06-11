package com.weike.data.business.setting;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.databinding.ActivityResetPwdBinding;
import com.weike.data.model.viewmodel.ResetPwdActVM;

/**
 * 重置密码页面
 */
public class ResetPwdActivity extends BaseActivity {
    ActivityResetPwdBinding binding;

    ResetPwdActVM vm;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_reset_pwd);
        vm = new ResetPwdActVM();
        binding.setResetPwdVM(vm);

        setCenterText("");
        setLeftText("重置密码");
        setRightText("");
    }
}
