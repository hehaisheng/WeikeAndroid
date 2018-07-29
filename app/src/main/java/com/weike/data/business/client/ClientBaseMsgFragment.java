package com.weike.data.business.client;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.business.log.RemindSettingActivity;
import com.weike.data.business.setting.AttrManagerActivity;
import com.weike.data.config.Config;
import com.weike.data.databinding.FragmentClientBaseMsgBinding;
import com.weike.data.model.business.AnotherAttributes;
import com.weike.data.model.business.ClientRelated;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.req.DelAniDayReq;
import com.weike.data.model.resp.GetClientDetailMsgResp;
import com.weike.data.model.viewmodel.AddClientRelateItemVM;
import com.weike.data.model.viewmodel.AddPhoneVM;
import com.weike.data.model.viewmodel.AnniversariesItemVM;
import com.weike.data.model.viewmodel.AnotherAttrItemVM;
import com.weike.data.model.viewmodel.ClientBaseMsgVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.DialogUtil;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.NoScrollLinearLayoutManager;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by LeoLu on 2018/6/4.
 * 客户基本信息Fragment
 */

public class ClientBaseMsgFragment extends BaseFragment implements View.OnClickListener, AddPhoneVM.OnPhoneClickListener, AddClientRelateItemVM.AddClientRelateItemListener, AnniversariesItemVM.AnniversariseDayClickListener {

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


    public List<AnotherAttrItemVM> anotherAttrItemVMS = new ArrayList<>();
    BaseDataBindingAdapter anotherAdapter;

    /**
     * 上一次点击使用
     */
    private AddClientRelateItemVM lastRelateClientVM;

    public ClientBaseMsgVM clientBaseMsgVM;

    public FragmentClientBaseMsgBinding binding;

    private AnniversariesItemVM lastAnniversariesItemVM;


    public void showDisplayContent(GetClientDetailMsgResp data, boolean isMoidfy) {

        LogUtil.d("acthome", "shoDisplay:" + isMoidfy + "," + data);

        if (isMoidfy) {
            clientBaseMsgVM.emailDisplay.set(true);
            clientBaseMsgVM.companyDisplay.set(true);
            clientBaseMsgVM.jobDisplay.set(true);
            clientBaseMsgVM.companyLocDisplay.set(true);
            clientBaseMsgVM.houseLocDisplay.set(true);
            clientBaseMsgVM.sexDisplay.set(true);
            clientBaseMsgVM.birthdayDisplay.set(true);
            clientBaseMsgVM.idCardDisplay.set(true);
            clientBaseMsgVM.marryDisplay.set(true);
            clientBaseMsgVM.heightDisplay.set(true);
            clientBaseMsgVM.widgetDispaly.set(true);
            clientBaseMsgVM.anniDisplay.set(true);
            clientBaseMsgVM.bearDisplay.set(true);
            clientBaseMsgVM.clientRelateDisplay.set(true);
        } else if (data != null) {

            clientBaseMsgVM.emailDisplay.set(TextUtils.isEmpty(data.getEmail()) ? false : true);
            clientBaseMsgVM.companyDisplay.set(TextUtils.isEmpty(data.getCompany()) ? false : true);
            clientBaseMsgVM.companyLocDisplay.set(TextUtils.isEmpty(data.getCompanyDetailAddress()) ? false : true);
            clientBaseMsgVM.jobDisplay.set(TextUtils.isEmpty(data.getOffice()) ? false : true);
            clientBaseMsgVM.houseLocDisplay.set(TextUtils.isEmpty(data.getHomeDetailAddress()) ? false : true);
            clientBaseMsgVM.birthdayDisplay.set(TextUtils.isEmpty(data.getBirthday()) ? false : true);
            clientBaseMsgVM.idCardDisplay.set(TextUtils.isEmpty(data.getIdCard()) ? false : true);
            clientBaseMsgVM.heightDisplay.set(TextUtils.isEmpty(data.getHeight()) ? false : true);
            clientBaseMsgVM.widgetDispaly.set(TextUtils.isEmpty(data.getWeight()) ? false : true);

            if (data.getClientRelatedList().size() == 0) {
                clientBaseMsgVM.clientRelateDisplay.set(false);
            } else {
                clientBaseMsgVM.clientRelateDisplay.set(true);
            }

            if (data.getAnniversaryList().size() > 0) {
                clientBaseMsgVM.anniDisplay.set(true);
            } else {
                clientBaseMsgVM.anniDisplay.set(false);
            }


            if (TextUtils.isEmpty(data.getSonNum()) == false || TextUtils.isEmpty(data.getDaughterNum()) == false) {
                clientBaseMsgVM.bearDisplay.set(true);
            } else {
                clientBaseMsgVM.bearDisplay.set(false);
            }


            if (TextUtils.isEmpty(data.getMarriage()) || Integer.parseInt(data.getSex()) == -1) {
                clientBaseMsgVM.marryDisplay.set(false);
            } else {
                clientBaseMsgVM.marryDisplay.set(true);
            }

          /*  if (data.getAnniversaryList().size() == 0) {
                clientBaseMsgVM.anniDisplay.set(false);
            } else {
                clientBaseMsgVM.anniDisplay.set(true);
            }*/


            if (TextUtils.isEmpty(data.getSex()) || Integer.parseInt(data.getSex()) == -1) {
                clientBaseMsgVM.sexDisplay.set(false);
            } else {
                clientBaseMsgVM.sexDisplay.set(true);
            }


        }

    }

    public void initAnotherAttr() {
        anotherAttrItemVMS.clear();
        List<AnotherAttributes> list = SpUtil.getInstance().getUser().anotherAttributes;
        for (int i = 0; i < list.size(); i++) {
            AnotherAttrItemVM vm = new AnotherAttrItemVM();
            vm.name.set(list.get(i).attributesName);
            vm.value.set("");
            vm.id = list.get(i).attributesId;
            vm.isModify.set(true);
            anotherAttrItemVMS.add(vm);
        }

        anotherAdapter = new BaseDataBindingAdapter(getActivity(), R.layout.widget_another_attr_list_litem, anotherAttrItemVMS, BR.anotherItemVM);
        NoScrollLinearLayoutManager linearLayoutManager = new NoScrollLinearLayoutManager(getActivity());
        linearLayoutManager.setScrollEnabled(false);

        binding.anotherAttrRecycle.setLayoutManager(linearLayoutManager);
        binding.anotherAttrRecycle.setAdapter(anotherAdapter);
    }

    private List<GetClientDetailMsgResp.AnotherAttrBean> lastAnotherAttr;



    public void loadDefault(BaseResp<GetClientDetailMsgResp> getClientDetailMsgRespBaseResp) {
        GetClientDetailMsgResp data = getClientDetailMsgRespBaseResp.getDatas();
        showDisplayContent(data, false);
        String[] phone = new String[5];
        phone[0] = data.getOnePhoneNumber();
        phone[1] = data.getTwoPhoneNumber();
        phone[2] = data.getThreePhoneNumber();
        phone[3] = data.getFourPhoneNumber();
        phone[4] = data.getFivePhoneNumber();
        setPhoneNum(phone); //电话号码


        clientBaseMsgVM.email.set(data.getEmail());
        clientBaseMsgVM.companyName.set(data.getCompany());
        clientBaseMsgVM.companyLocation.set(data.getCompanyDetailAddress());

        if (TextUtils.isEmpty(data.getSex()) || Integer.parseInt(data.getSex()) == -1) {
            clientBaseMsgVM.sex.set("");
        } else {
            clientBaseMsgVM.sex.set(Integer.parseInt(data.getSex()) == 1 ? "男" : "女");
        }


        clientBaseMsgVM.birthday.set(data.getBirthday());
        if (TextUtils.isEmpty(data.getMarriage()) == false) {
            int marry = Integer.parseInt(data.getMarriage());
            if (marry == 1) {
                clientBaseMsgVM.marry.set("已婚");
            } else if (marry == 2) {
                clientBaseMsgVM.marry.set("未婚");
            } else if (marry == 3) {
                clientBaseMsgVM.marry.set("离异");
            } else if (marry == -1) {
                clientBaseMsgVM.marry.set("");
            }
        }

        clientBaseMsgVM.idCard.set(data.getIdCard());
        clientBaseMsgVM.son.set(data.getSonNum());
        clientBaseMsgVM.job.set(data.getOffice());
        clientBaseMsgVM.houseLocation.set(data.getHomeDetailAddress());
        clientBaseMsgVM.gril.set(data.getDaughterNum());
        clientBaseMsgVM.clientHeight.set(data.getHeight());
        clientBaseMsgVM.clientWidght.set(data.getWeight());


        LogUtil.d("ActHomeAddClient", "-->" + data.getBirthdayjson());
        if (data.getBirthdayjson() != null && TextUtils.isEmpty(data.getBirthdayjson().id) == false) {
            //生日提醒
            ToDo birthdayRemind = new ToDo();
            birthdayRemind.isRemind = data.getBirthdayjson().isRemind;
            birthdayRemind.id = data.getBirthdayjson().id;
            birthdayRemind.isRepeat = data.getBirthdayjson().isRepeat;
            birthdayRemind.repeatDateType = data.getBirthdayjson().repeatDateType;
            birthdayRemind.repeatInterval = data.getBirthdayjson().repeatInterval;
            birthdayRemind.isAdvance = data.getBirthdayjson().isAdvance;
            birthdayRemind.advanceDateType = data.getBirthdayjson().advanceDateType;
            birthdayRemind.advanceInterval = data.getBirthdayjson().advanceInterval;
            birthdayRemind.birthdaydate = data.getBirthdayjson().remindDate;
            birthdayRemind.content = data.getBirthdayjson().content;
            if (birthdayRemind.isRemind == 1) {
                clientBaseMsgVM.birthdayRemindIcon.set(R.mipmap.ic_remind);
            } else {
                clientBaseMsgVM.birthdayRemindIcon.set(R.mipmap.ic_remind_dis);
            }

            clientBaseMsgVM.birthDayTodo = birthdayRemind;
        }

        updateClientRelate(data.getClientRelatedList());
        updateAnnaDay(data.getAnniversaryList()); //纪念日
        updateAnotherType(data.getUserAttributesList());
    }

    public String getAnotherAttr() {

        List<AnotherAttributes> list = new ArrayList<>();

        for (int i = 0; i < anotherAttrItemVMS.size(); i++) {
            String value = anotherAttrItemVMS.get(i).value.get();

            AnotherAttributes atr = new AnotherAttributes();
            atr.attributesName = anotherAttrItemVMS.get(i).name.get();
            atr.attributesContent = anotherAttrItemVMS.get(i).value.get();
            atr.attributesId = anotherAttrItemVMS.get(i).id;
            list.add(atr);
        }

        if (list.size() == 0) {
            return "";
        } else {
            return "" + JsonUtil.GsonString(list) + "";
        }

    }

    /**
     * 纪念日集合
     */
    public void updateAnnaDay(List<GetClientDetailMsgResp.AnniversaryListBean> list) {
        anniDayVMS.clear();
        initAniDayHead();
        for (int i = 0; i < list.size(); i++) {
            AnniversariesItemVM vm = new AnniversariesItemVM(getActivity());
            LogUtil.d("ClientBaseMsgFragment", "aniId:" + list.get(i).getId());
            vm.id.set(list.get(i).getId() + "");

            if (list.get(i).getRemind() != null && TextUtils.isEmpty(list.get(i).getRemind().content) == false) {

                ToDo toDo = new ToDo();
                toDo.content = list.get(i).getRemind().content;
                toDo.id = list.get(i).getRemind().id;
                toDo.isAdvance = Integer.parseInt(list.get(i).getRemind().isAdvance);
                toDo.advanceInterval = Integer.parseInt(list.get(i).getRemind().advanceInterval);
                toDo.advanceDateType = Integer.parseInt(list.get(i).getRemind().advanceDateType);
                toDo.repeatInterval = Integer.parseInt(list.get(i).getRemind().repeatInterval);
                toDo.repeatDateType = Integer.parseInt(list.get(i).getRemind().repeatDateType);
                toDo.isRepeat = Integer.parseInt(list.get(i).getRemind().isRepeat);
                toDo.birthdaydate = list.get(i).getRemind().remindDate;
                if (toDo.isRemind == 1) {
                    vm.remindIcon.set(R.mipmap.ic_remind);
                } else {
                    vm.remindIcon.set(R.mipmap.ic_remind_dis);
                }
                vm.toDo = toDo;
            }

            vm.isModify.set(true);
            vm.isFirst.set(false);

            vm.setListener(this);
            vm.name.set(list.get(i).getAnniversaryName());
            vm.time.set(list.get(i).getAnniversaryDate());
            anniDayVMS.add(0, vm);
        }
        anniDayAdapter.notifyDataSetChanged();

    }

    public void updateAnnaDayStatus(boolean isModify) {

        for (int i = 0; i < anniDayVMS.size(); i++) {
            if (i == anniDayVMS.size() - 1) break;
            anniDayVMS.get(i).isModify.set(isModify);
        }
        anniDayAdapter.notifyDataSetChanged();

    }


    public void updateAnotherAttr(boolean isModify) {





        if (isModify) {
            List<AnotherAttributes> tmp = SpUtil.getInstance().getUser().anotherAttributes;
            for(int i = 0 ;i < anotherAttrItemVMS.size();i++){
                AnotherAttributes atr = new AnotherAttributes();
                atr.attributesId = anotherAttrItemVMS.get(i).id;
                atr.attributesContent = anotherAttrItemVMS.get(i).value.get();
                atr.attributesName = anotherAttrItemVMS.get(i).name.get();
                tmp.add(atr);
            }
            anotherAttrItemVMS.clear();
            for(int i = 0 ; i < tmp.size();i++) {
                AnotherAttrItemVM vm = new AnotherAttrItemVM();
                vm.value.set(tmp.get(i).attributesContent);
                vm.name.set(tmp.get(i).attributesName);
                vm.id = tmp.get(i).attributesId;
                anotherAttrItemVMS.add(vm);
            }
            anotherAdapter.notifyDataSetChanged();

        }

    }

    /**
     * 这里是 获取客户信息 也就是有电话信息的时候 才调用 一般不用
     *
     * @param phoneNum
     */
    public void setPhoneNum(String[] phoneNum) {
        addPhoneVMS.clear();
        boolean isEmpty = true;

        for (int i = 0; i < phoneNum.length; i++) {

            LogUtil.d("phoneNum", "i:" + i + "," + phoneNum[i]);
            if (TextUtils.isEmpty(phoneNum[i])) {
                continue;
            }

            AddPhoneVM addPhoneVM = new AddPhoneVM(getActivity());
            addPhoneVM.isFirst.set(false);
            addPhoneVM.isShowModify.set(true);
            addPhoneVM.phoneNumber.set(phoneNum[i]);
            addPhoneVM.isModify.set(false);
            addPhoneVM.setListener(this);
            addPhoneVMS.add(addPhoneVM);
            isEmpty = false;
        }

        if (isEmpty) {
            binding.addPhoneNum.setVisibility(View.GONE);
        } else {
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
        if (requestCode == RemindSettingActivity.CODE_OF_REQUEST && resultCode == RESULT_OK && data != null) {
            ToDo toDo = data.getParcelableExtra(RemindSettingActivity.KEY_OF_TODO);
            clientBaseMsgVM.onBirthdayRemindResult(toDo);

        } else if (requestCode == RelateClientActivity.REQUEST_CODE && data != null) {
            List<ClientRelated> list = (List<ClientRelated>) data.getSerializableExtra("data");

            lastRelateClientVM.clientId = list.get(0).clientId;
            lastRelateClientVM.clientName.set(list.get(0).name);
            clientRelateAdapter.notifyDataSetChanged();


        } else if (requestCode == AttrManagerActivity.REQUEST_CODE) {
            anotherAttrItemVMS.clear();
            initAnotherAttr();

        } else if (requestCode == REQUEST_ANNIDAY_CODE && resultCode == RESULT_OK && data != null) {

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
        updateAnnaDayStatus(status);


        updatePhoneStatus(status);
        updateAnotherAttr(status);
        if (status) {//编辑
            showDisplayContent(null, true);
        } else { //完成

        }
    }

    public void updateAnotherType(List<GetClientDetailMsgResp.AnotherAttrBean> attr) {


        List<AnotherAttributes> local = SpUtil.getInstance().getUser().anotherAttributes;

        for (int i = 0; i < local.size(); i++) {
           for(int j = 0 ; j < attr.size();j++){

           }

        }

        anotherAdapter.notifyDataSetChanged();

    }


    @Override
    protected int setUpLayoutId() {
        return R.layout.fragment_client_base_msg;
    }

    @Override
    protected void loadFinish(View view) {


    }

    private void initAniDay() {
        anniDayAdapter = new BaseDataBindingAdapter(getActivity(), R.layout.widget_layout_ani_day, anniDayVMS, BR.anniverVM);
        NoScrollLinearLayoutManager linearLayoutManager = new NoScrollLinearLayoutManager(getActivity());
        linearLayoutManager.setScrollEnabled(false);
        binding.recycleAnnidayList.setLayoutManager(linearLayoutManager);
        binding.recycleAnnidayList.setAdapter(anniDayAdapter);

        initAniDayHead();

        anniDayAdapter.notifyDataSetChanged();
    }

    /**
     * 更新客户关联列表
     *
     * @param list
     */
    public void updateClientRelate(List<GetClientDetailMsgResp.ClientRelateBean> list) {

        clientRelateItemVMS.clear();


        for (int i = 0; i < list.size(); i++) {
            AddClientRelateItemVM vm = new AddClientRelateItemVM();
            if (i == 0) {
                vm.isFirst.set(true);
                vm.isModify.set(false);
            } else {
                vm.isFirst.set(false);
                vm.isModify.set(false);
            }
            vm.clientId = list.get(i).relatedClientId;
            vm.clientName.set(list.get(i).relatedUserName);
            vm.setAddClientRelateItemListener(this);
            clientRelateItemVMS.add(vm);
        }
        if (list.size() == 0) {
            AddClientRelateItemVM vm = new AddClientRelateItemVM();
            vm.isFirst.set(true);
            vm.isModify.set(false);
            vm.setAddClientRelateItemListener(this);
            clientRelateItemVMS.add(vm);
        }

        clientRelateAdapter.notifyDataSetChanged();

    }

    private void initAniDayHead() {

        AnniversariesItemVM vm = new AnniversariesItemVM(getActivity());
        vm.setListener(this);

        vm.isModify.set(false);
        vm.isFirst.set(true);
        anniDayVMS.add(vm);


    }

    private void addHeadPhone() {
        AddPhoneVM addPhoneVM = new AddPhoneVM(getActivity());
        addPhoneVM.isFirst.set(true);
        addPhoneVM.isModify.set(false);
        addPhoneVM.isShowModify.set(false);
        addPhoneVM.setListener(this);
        addPhoneVMS.add(addPhoneVM);
    }

    /**
     * 初始化电话号码
     */
    private void initPhoneRecycle() {


        addHeadPhone();
        //加载添加号码那个框 把后面两个显示去掉

        addPhoneAdapter = new BaseDataBindingAdapter(getActivity(), R.layout.widget_layout_add_phone, addPhoneVMS, BR.addPhoneVM);
        NoScrollLinearLayoutManager linearLayoutManager = new NoScrollLinearLayoutManager(getActivity());
        linearLayoutManager.setScrollEnabled(false);
        binding.addPhoneNum.setLayoutManager(linearLayoutManager);
        binding.addPhoneNum.setAdapter(addPhoneAdapter);
        binding.addPhoneNum.setVisibility(View.GONE);
    }

    /**
     * 刷新状态
     *
     * @param isModify
     */
    private void updatePhoneStatus(boolean isModify) {
        if (isModify == false) {
            addPhoneVMS.remove(0);

            if (addPhoneVMS.size() == 0) {
                binding.addPhoneNum.setVisibility(View.GONE);
                return;
            }

            for (int i = 0; i < addPhoneVMS.size(); i++) {
                addPhoneVMS.get(i).isFirst.set(false); //隐藏添加号码
                addPhoneVMS.get(i).isModify.set(false); //隐藏编辑
                addPhoneVMS.get(i).isShowModify.set(true);
            }
        } else {
            binding.addPhoneNum.setVisibility(View.VISIBLE);
            for (int i = 0; i < addPhoneVMS.size(); i++) {
                addPhoneVMS.get(i).isFirst.set(false); //显示添加号码
                addPhoneVMS.get(i).isModify.set(true);
                addPhoneVMS.get(i).isShowModify.set(false);
            }
            addHeadPhone();
        }
        addPhoneAdapter.notifyDataSetChanged();
    }


    private void initClientRelateRecycle() {

        AddClientRelateItemVM vm = new AddClientRelateItemVM();
        vm.isFirst.set(true);
        vm.isModify.set(false);
        vm.setAddClientRelateItemListener(this);
        clientRelateItemVMS.add(vm);

        clientRelateAdapter = new BaseDataBindingAdapter(getActivity(), R.layout.widget_add_client_relate_item, clientRelateItemVMS, BR.clientRelateForAddVM);
        NoScrollLinearLayoutManager linearLayoutManager = new NoScrollLinearLayoutManager(getActivity());
        linearLayoutManager.setScrollEnabled(false);
        binding.addRelateClient.setLayoutManager(linearLayoutManager);
        binding.addRelateClient.setAdapter(clientRelateAdapter);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_client_base_msg, container, false);
        clientBaseMsgVM = new ClientBaseMsgVM(this);
        clientBaseMsgVM.activity = getActivity();
        clientBaseMsgVM.isModify.set(isModify);

        binding.setClientBaseVM(clientBaseMsgVM);

        initPhoneRecycle();
        initClientRelateRecycle();
        initAniDay();
        initAnotherAttr();
        updatePhoneStatus(clientBaseMsgVM.isModify.get());//更新一下电话的状态
        updateAnnaDayStatus(clientBaseMsgVM.isModify.get());

        binding.edCompany.requestFocus();
        binding.edCompany.requestFocusFromTouch();

        binding.scrollView.post(new Runnable() {
            @Override
            public void run() {
                binding.scrollView.fullScroll(View.FOCUS_UP);
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AddClientActivity act = (AddClientActivity) getActivity();
                act.addFource();
            }
        }, 500);

        showDisplayContent(null, true);

        return binding.getRoot();
    }


    private int phoneNumCount = 0;


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPhoneItemClick(AddPhoneVM vm, int type) {
        if (type == ADD) {
            if (addPhoneVMS.size() >= 6) {
                ToastUtil.showToast("最多只能添加5个号码");
                return;
            }
            AddPhoneVM addPhoneVM = new AddPhoneVM(getActivity());
            addPhoneVM.isFirst.set(false);
            addPhoneVM.isShowModify.set(false);
            addPhoneVM.isModify.set(true);
            addPhoneVM.setListener(this);
            addPhoneVMS.add(0, addPhoneVM);
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
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onRelateItemClick(AddClientRelateItemVM vm, int type) {
        if (type == AddClientRelateItemVM.AddClientRelateItemListener.ADD_ITEM) {
            AddClientRelateItemVM current = new AddClientRelateItemVM();
            current.isFirst.set(false);
            current.isModify.set(false);
            current.setAddClientRelateItemListener(this);

            clientRelateItemVMS.add(current);
            clientRelateAdapter.notifyDataSetChanged();


        } else if (type == AddClientRelateItemVM.AddClientRelateItemListener.ADD_RELATE) {

            RelateClientActivity.startActivity(true, this, RelateClientActivity.REQUEST_CODE);
            lastRelateClientVM = vm;

        } else if (type == AddClientRelateItemVM.AddClientRelateItemListener.REDUCE) {

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

        } else if (type == AddClientRelateItemVM.AddClientRelateItemListener.CANCEL_FIRST) {
            DialogUtil.showButtonDialog(getFragmentManager(), "提示", "是否删除关联客户", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vm.clientId = null;
                    vm.clientName.set("请选择");
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
            anniDayVMS.add(0, news);
            anniDayAdapter.notifyDataSetChanged();
        } else if (type == 2) { //reduce
            DialogUtil.showButtonDialog(getFragmentManager(), "提示", "是否移除该纪念日", new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAniDay(item);

                }
            });
        } else {
            lastAnniversariesItemVM = item;
            RemindSettingActivity.startActivity(this, lastAnniversariesItemVM.toDo, REQUEST_ANNIDAY_CODE);
        }
    }

    private void removeAniDay(AnniversariesItemVM item) {

        DelAniDayReq req = new DelAniDayReq();
        req.id = item.id.get();
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.DEL_ANIDAY)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp>() {

                })).subscribe(new BaseObserver<BaseResp>() {
            @Override
            protected void onSuccess(BaseResp baseResp) throws Exception {
                anniDayVMS.remove(item);
                anniDayAdapter.notifyDataSetChanged();
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });

    }
}
