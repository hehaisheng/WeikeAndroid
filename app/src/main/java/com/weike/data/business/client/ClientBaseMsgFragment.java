package com.weike.data.business.client;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.business.log.RemindSettingActivity;
import com.weike.data.business.setting.AttrManagerActivity;
import com.weike.data.databinding.FragmentClientBaseMsgBinding;
import com.weike.data.model.business.ClientRelated;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.resp.GetClientDetailMsgResp;
import com.weike.data.model.viewmodel.AddClientRelateItemVM;
import com.weike.data.model.viewmodel.AddPhoneVM;
import com.weike.data.model.viewmodel.AnniversariesItemVM;
import com.weike.data.model.viewmodel.ClientBaseMsgVM;
import com.weike.data.util.DialogUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.NoScrollLinearLayoutManager;
import com.weike.data.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by LeoLu on 2018/6/4.
 * 客户基本信息Fragment
 */

public class ClientBaseMsgFragment extends BaseFragment implements View.OnClickListener,AddPhoneVM.OnPhoneClickListener,AddClientRelateItemVM.AddClientRelateItemListener,AnniversariesItemVM.AnniversariseDayClickListener {

    /**
     * 纪念日专用
     */
    public static int REQUEST_ANNIDAY_CODE = 1000;

    /**
     * 添加号码适配器
     */
    public List<AddPhoneVM> addPhoneVMS = new ArrayList<>();
    BaseDataBindingAdapter addPhoneAdapter;

    /**
     * 客户关联适配器
     */
    public List<AddClientRelateItemVM> clientRelateItemVMS = new ArrayList<>();
    BaseDataBindingAdapter clientRelateAdapter;


    /**
     * 纪念日适配器
     */
    public List<AnniversariesItemVM> anniDayVMS = new ArrayList<>();
    BaseDataBindingAdapter anniDayAdapter;

    /**
     * 上一次点击使用
     */
    private AddClientRelateItemVM lastRelateClientVM;

    public ClientBaseMsgVM clientBaseMsgVM;

    public FragmentClientBaseMsgBinding binding;

    private AnniversariesItemVM lastAnniversariesItemVM;


    /**
     * 纪念日集合
     */
    public void updateAnnaDay(List<GetClientDetailMsgResp.AnniversaryListBean> list){

    }

    /**
     * 这里是 获取客户信息 也就是有电话信息的时候 才调用 一般不用
     * @param phoneNum
     */
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

        if (phoneNum.length == 0) {
            binding.addPhoneNum.setVisibility(View.GONE);
        }else {
            binding.addPhoneNum.setVisibility(View.VISIBLE);
        }
        addPhoneAdapter.notifyDataSetChanged();
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 用于生日
         */
        if(requestCode == RemindSettingActivity.CODE_OF_REQUEST && resultCode == RESULT_OK && data != null) {
            ToDo toDo = data.getParcelableExtra(RemindSettingActivity.KEY_OF_TODO);
            clientBaseMsgVM.onBirthdayRemindResult(toDo);

        } else if (requestCode == RelateClientActivity.REQUEST_CODE && data != null) {
            List<ClientRelated> list = (List<ClientRelated>) data.getSerializableExtra("data");

            lastRelateClientVM.clientId = list.get(0).clientId;
            lastRelateClientVM.clientName.set(list.get(0).name);
            clientRelateAdapter.notifyDataSetChanged();


        } else if (requestCode == AttrManagerActivity.CONTEXT_INCLUDE_CODE) {

        } else if (requestCode == REQUEST_ANNIDAY_CODE && resultCode == RESULT_OK && data != null){

            ToDo toDo = data.getParcelableExtra(RemindSettingActivity.KEY_OF_TODO);
            lastAnniversariesItemVM.toDo = toDo;
            if (toDo.isRemind == 1) {
                lastAnniversariesItemVM.remindIcon.set(R.mipmap.ic_remind);
            } else {
                lastAnniversariesItemVM.remindIcon.set(R.mipmap.ic_remind_dis);
            }
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

    private void initAniDay(){
        anniDayAdapter = new BaseDataBindingAdapter(getActivity(),R.layout.widget_layout_ani_day,anniDayVMS, BR.anniverVM);
        NoScrollLinearLayoutManager linearLayoutManager = new NoScrollLinearLayoutManager(getActivity());
        linearLayoutManager.setScrollEnabled(false);
        binding.recycleAnnidayList.setLayoutManager(linearLayoutManager);
        binding.recycleAnnidayList.setAdapter(anniDayAdapter);

        initAniDayHead();

        anniDayAdapter.notifyDataSetChanged();
    }

    private void initAniDayHead(){
        for(int i = 0 ; i < 2;i++) {
            AnniversariesItemVM vm = new AnniversariesItemVM(getActivity());
            if (i == 0) {
                vm.isModify.set(false);
                vm.isFirst.set(true);
            } else {
                vm.isModify.set(true);
                vm.isFirst.set(false);
            }
            vm.setListener(this);

            anniDayVMS.add(vm);

        }

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
        NoScrollLinearLayoutManager linearLayoutManager = new NoScrollLinearLayoutManager(getActivity());
        linearLayoutManager.setScrollEnabled(false);
        binding.addPhoneNum.setLayoutManager(linearLayoutManager);
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
        NoScrollLinearLayoutManager linearLayoutManager = new NoScrollLinearLayoutManager(getActivity());
        linearLayoutManager.setScrollEnabled(false);
        binding.addRelateClient.setLayoutManager(linearLayoutManager);
        binding.addRelateClient.setAdapter(clientRelateAdapter);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_client_base_msg,container,false);
        clientBaseMsgVM = new ClientBaseMsgVM(this);
        clientBaseMsgVM.activity = getActivity();
        clientBaseMsgVM.isModify.set(isModify);

        binding.setClientBaseVM(clientBaseMsgVM);

        initPhoneRecycle();
        initClientRelateRecycle();
        initAniDay();
        updateModify(clientBaseMsgVM.isModify.get());//更新一下电话的状态

        binding.edCompany.requestFocus();
        binding.edCompany.requestFocusFromTouch();

        binding.scrollView.post(new Runnable() {
            @Override
            public void run() {
                binding.scrollView.fullScroll(View.FOCUS_UP);
            }
        });

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
            DialogUtil.showButtonDialog(getFragmentManager(), "提示", "是否删除电话?", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPhoneVMS.remove(vm);
                    addPhoneAdapter.notifyDataSetChanged();
                }
            });

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

            RelateClientActivity.startActivity(true,this,RelateClientActivity.REQUEST_CODE);
            lastRelateClientVM = vm;

        } else if(type == AddClientRelateItemVM.AddClientRelateItemListener.REDUCE){

            DialogUtil.showButtonDialog(getFragmentManager(), "提示", "是否删除关联客户", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clientRelateItemVMS.remove(vm);
                    clientRelateAdapter.notifyDataSetChanged();
                }
            });

        }

    }

    @Override
    public void anniDayClick(int type, AnniversariesItemVM item) {
        if (type == 1) { //add
            AnniversariesItemVM news = new AnniversariesItemVM(getActivity());
            news.isFirst.set(false);
            news.isModify.set(true);
            news.setListener(this);
            anniDayVMS.add(news);
            anniDayAdapter.notifyDataSetChanged();
        } else if (type == 2) { //reduce
            DialogUtil.showButtonDialog(getFragmentManager(), "提示", "是否移除该纪念日", new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    anniDayVMS.remove(item);
                    anniDayAdapter.notifyDataSetChanged();
                }
            });
        } else {
            lastAnniversariesItemVM = item;
            RemindSettingActivity.startActivity(this,lastAnniversariesItemVM.toDo,REQUEST_ANNIDAY_CODE);
        }
    }
}
