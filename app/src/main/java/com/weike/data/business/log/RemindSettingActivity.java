package com.weike.data.business.log;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.databinding.ActivityRemindSettingBinding;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.viewmodel.RemingSetActVM;

/**
 * Created by LeoLu on 2018/6/6.
 */

public class RemindSettingActivity extends BaseActivity {

    ActivityRemindSettingBinding binding;
    RemingSetActVM vm;

    private ToDo toDo;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_remind_setting);
        vm = new RemingSetActVM();
        binding.setRemindSettingVM(vm);
        setRightText("保存");
        setCenterText("设置提醒");


    }


}
