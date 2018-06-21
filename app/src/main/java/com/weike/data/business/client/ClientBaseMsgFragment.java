package com.weike.data.business.client;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.business.log.RemindSettingActivity;
import com.weike.data.databinding.FragmentClientBaseMsgBinding;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.viewmodel.AddPhoneVM;
import com.weike.data.model.viewmodel.ClientBaseMsgVM;
import com.weike.data.model.viewmodel.AddClientRelateItemVM;
import com.weike.data.util.LogUtil;
import com.weike.data.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by LeoLu on 2018/6/4.
 * 客户基本信息Fragment
 */

public class ClientBaseMsgFragment extends BaseFragment implements View.OnClickListener,AddPhoneVM.OnPhoneClickListener,AddClientRelateItemVM.AddClientRelateItemListener {



    public List<AddPhoneVM> addPhoneVMS = new ArrayList<>();
    BaseDataBindingAdapter addPhoneAdapter;

    public List<AddClientRelateItemVM> clientRelateItemVMS = new ArrayList<>();
    BaseDataBindingAdapter clientRelateAdapter;

    public void setPhoneNum(String[] phoneNum) {
        addPhoneVMS.clear();

        for(int i = 0 ; i < phoneNum.length ; i++) {

            LogUtil.d("phoneNum","i:" + i + "," + phoneNum[i]);
            if (TextUtils.isEmpty(phoneNum[i])) {
                continue;
            }

            AddPhoneVM addPhoneVM  = new AddPhoneVM(getActivity());
            addPhoneVM.isFirst.set(false);
            addPhoneVM.isShowModify.set(true);
            addPhoneVM.phoneNumber.set(phoneNum[i]);
            addPhoneVM.isModify.set(false);
            addPhoneVM.setListener(this);
            addPhoneVMS.add(addPhoneVM);

        }


        binding.addPhoneNum.setVisibility(View.VISIBLE);
        addPhoneAdapter.notifyDataSetChanged();
    }


    public ClientBaseMsgVM clientBaseMsgVM;

    public FragmentClientBaseMsgBinding binding;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 用于生日
         */
        if(requestCode == RemindSettingActivity.CODE_OF_REQUEST && resultCode == RESULT_OK && data != null) {
            ToDo toDo = data.getParcelableExtra(RemindSettingActivity.KEY_OF_TODO);
            clientBaseMsgVM.onBirthdayRemindResult(toDo);

        }
    }

    @Override
    public void onRightClick(boolean status) {
        super.onRightClick(status);
        clientBaseMsgVM.isModify.set(status);

        updateModify(status);
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

    private void addHeadPhone(){
        AddPhoneVM addPhoneVM = new AddPhoneVM(getActivity());
        addPhoneVM.isFirst.set(true);
        addPhoneVM.isModify.set(false);
        addPhoneVM.isShowModify.set(false);
        addPhoneVM.setListener(this);
        addPhoneVMS.add(0,addPhoneVM);
    }

    /**
     * 初始化电话号码
     */
    private void initPhoneRecycle(){


        addHeadPhone();
        //加载添加号码那个框 把后面两个显示去掉

        addPhoneAdapter = new BaseDataBindingAdapter(getActivity(),R.layout.widget_layout_add_phone,addPhoneVMS, BR.addPhoneVM);
        binding.addPhoneNum.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.addPhoneNum.setAdapter(addPhoneAdapter);
        binding.addPhoneNum.setVisibility(View.GONE);
    }

    /**
     * 刷新状态
     * @param isModify
     */
    private void updateModify(boolean isModify) {
        if (isModify == false) {
            addPhoneVMS.remove(0);

            if(addPhoneVMS.size() == 0){
                binding.addPhoneNum.setVisibility(View.GONE);
                return;
            }

            for(int i = 0 ; i < addPhoneVMS.size() ; i++) {
                addPhoneVMS.get(i).isFirst.set(false); //隐藏添加号码
                addPhoneVMS.get(i).isModify.set(false); //隐藏编辑
                addPhoneVMS.get(i).isShowModify.set(true);
            }
        } else {
            binding.addPhoneNum.setVisibility(View.VISIBLE);
            for(int i = 0 ; i < addPhoneVMS.size() ; i++) {
                addPhoneVMS.get(i).isFirst.set(false); //显示添加号码
                addPhoneVMS.get(i).isModify.set(true);
                addPhoneVMS.get(i).isShowModify.set(false);
            }
            addHeadPhone();
        }
        addPhoneAdapter.notifyDataSetChanged();
    }



    private void initClientRelateRecycle(){

        AddClientRelateItemVM vm = new AddClientRelateItemVM();
        vm.isFirst.set(true);
        vm.isModify.set(false);
        vm.setAddClientRelateItemListener(this);
        clientRelateItemVMS.add(vm);

        clientRelateAdapter = new BaseDataBindingAdapter(getActivity(),R.layout.widget_add_client_relate_item,clientRelateItemVMS, BR.clientRelateForAddVM);
        binding.addRelateClient.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.addRelateClient.setAdapter(clientRelateAdapter);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_client_base_msg,container,false);
        clientBaseMsgVM = new ClientBaseMsgVM(this);
        clientBaseMsgVM.isModify.set(isModify);
        binding.setClientBaseVM(clientBaseMsgVM);

        initPhoneRecycle();
        initClientRelateRecycle();


        return binding.getRoot();
    }


    private int phoneNumCount = 0;


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPhoneItemClick(AddPhoneVM vm, int type) {
        if(type == ADD) {
            if(addPhoneVMS.size() >= 6) {
                ToastUtil.showToast("最多只能添加5个号码");
                return;
            }
            AddPhoneVM addPhoneVM  = new AddPhoneVM(getActivity());
            addPhoneVM.isFirst.set(false);
            addPhoneVM.isShowModify.set(false);
            addPhoneVM.isModify.set(true);
            addPhoneVM.setListener(this);
            addPhoneVMS.add(addPhoneVM);
            addPhoneAdapter.notifyDataSetChanged();
        } else {
            addPhoneVMS.remove(vm);
            addPhoneAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRelateItemClick(AddClientRelateItemVM vm, int type) {
        if (type ==AddClientRelateItemVM.AddClientRelateItemListener. ADD_ITEM) {
            AddClientRelateItemVM current = new AddClientRelateItemVM();
            current.isFirst.set(false);
            current.isModify.set(false);
            current.setAddClientRelateItemListener(this);

            clientRelateItemVMS.add(current);
            clientRelateAdapter.notifyDataSetChanged();


        } else if (type == AddClientRelateItemVM.AddClientRelateItemListener.ADD_RELATE) {
            //TODO showDialog
        } else if(type == AddClientRelateItemVM.AddClientRelateItemListener.REDUCE){
            clientRelateItemVMS.remove(vm);
            clientRelateAdapter.notifyDataSetChanged();
        }

    }
}
