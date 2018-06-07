package com.weike.data.business.setting;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.databinding.ActivityAppSettingBinding;
import com.weike.data.model.viewmodel.AppSettingActVM;
import com.weike.data.util.ActivitySkipUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeoLu on 2018/6/1.
 * app 设置页面
 */
public class AppSettingActivity extends BaseActivity {

    ActivityAppSettingBinding binding;
    AppSettingActVM vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_app_setting);
        vm = new AppSettingActVM(this);
        binding.setAppSettingVM(vm);
        setLeftText("应用设置");
        setCenterText("");
        setRightText("");

    }
}
