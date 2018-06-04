package com.weike.data.business.myself;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.load.data.LocalUriFetcher;
import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.databinding.ActivityMyQrcodeBinding;
import com.weike.data.model.viewmodel.MyQRCodeActVM;

/**
 * Created by LeoLu on 2018/6/1.
 * 我的二维码Activity
 */
public class MyQRCodeActivity extends BaseActivity {
    MyQRCodeActVM vm;

    ActivityMyQrcodeBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_qrcode);
        vm = new MyQRCodeActVM();
        binding.setMyqrcodeVM(vm);


        setRightText("");
        setCenterText("");
        setLeftText("我的二维码");

    }
}
