package com.weike.data.business.myself;

import android.bluetooth.le.AdvertisingSetCallback;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.databinding.ActivityVipOpenupBinding;
import com.weike.data.model.viewmodel.OpenUpVipActVM;

/**
 * Created by LeoLu on 2018/6/1.
 * 开通VIP
 */
public class VipOpenUpActivity extends BaseActivity {

    ActivityVipOpenupBinding binding;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_vip_openup);
        OpenUpVipActVM vm = new OpenUpVipActVM();
        binding.setOpenUpActVM(vm);


        setCenterText("");
        setLeftText("开通VIP会员");
        setRightText("");
    }
}
