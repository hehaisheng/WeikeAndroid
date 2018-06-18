package com.weike.data.business.client;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.weike.data.R;
import com.weike.data.base.BaseFragment;
import com.weike.data.databinding.FragmentClientBaseMsgBinding;
import com.weike.data.model.business.Client;
import com.weike.data.model.viewmodel.ClientBaseMsgVM;
import com.weike.data.util.ToastUtil;

import java.util.HashMap;

import okhttp3.internal.http.RetryAndFollowUpInterceptor;

/**
 * Created by LeoLu on 2018/6/4.
 * 客户基本信息Fragment
 */

public class ClientBaseMsgFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout phoneLinearLayout;
    private LinearLayout relateClient;


    public ClientBaseMsgVM clientBaseMsgVM;

    public FragmentClientBaseMsgBinding binding;

    HashMap<String , View> phoneNumMap = new HashMap<>();


    @Override
    public void onRightClick(boolean status) {
        super.onRightClick(status);
        clientBaseMsgVM.isModify.set(status);
        if (status) {//编辑


        } else { //完成

        }
    }

    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_client_base_msg;
    }

    @Override
    protected void loadFinish(View view) {


    }

    private void initClientMsg(){

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_client_base_msg,container,false);
        clientBaseMsgVM = new ClientBaseMsgVM(getActivity());
        binding.setClientBaseVM(clientBaseMsgVM);





        return binding.getRoot();
    }


    private int phoneNumCount = 0;


    @Override
    public void onClick(View v) {

    }
}
