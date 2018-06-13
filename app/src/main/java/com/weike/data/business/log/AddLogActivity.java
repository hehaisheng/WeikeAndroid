package com.weike.data.business.log;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.databinding.ActivityAddLogBinding;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.viewmodel.AddLogActVM;

/**
 * Created by LeoLu on 2018/5/30.
 * 新建日志页面
 */
public class AddLogActivity extends BaseActivity {

    public static String keyOfLog = "AddLogActivity";
    ActivityAddLogBinding binding;
    private ToDo toDo;
    AddLogActVM logActVM;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_log);
        logActVM = new AddLogActVM(this);
        binding.setAddLogVM(logActVM);
        setCenterText("");
        setRightText("");
        setLeftText("编辑日志");
    }
}
