package com.weike.data.business.client;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weike.data.R;
import com.weike.data.base.BaseFragment;
import com.weike.data.databinding.FragmentClientServiceMsgBinding;
import com.weike.data.model.viewmodel.ClientServiceMsgVM;

/**
 * Created by LeoLu on 2018/6/4.
 * 客户服务信息Fragment
 */
public class ClientServiceMsgFragment extends BaseFragment {

    FragmentClientServiceMsgBinding binding;
    ClientServiceMsgVM vm;

    @Override
    protected int setUpLayoutId() {
       return 0;
    }

    @Override
    protected void loadFinish(View view) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_client_service_msg,container,false);
        vm = new ClientServiceMsgVM();

        vm.carType.set("奥迪");
        vm.financialAssets.set("1000");
        vm.clickable.set(true);
        vm.moneyIn.set("100");
        vm.liabilities.set("100");
        binding.setClientServiceVM(vm);

        return binding.getRoot();
    }
}
