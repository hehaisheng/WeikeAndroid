package com.weike.data.business.client;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.weike.data.BR;
import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.adapter.BaseDataBindingAdapter;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.business.log.RemindSettingActivity;
import com.weike.data.business.setting.AttrManagerActivity;
import com.weike.data.config.Config;
import com.weike.data.config.DataConfig;
import com.weike.data.databinding.FragmentClientBaseMsgBinding;
import com.weike.data.model.business.AnotherAttributes;
import com.weike.data.model.business.ClientRelated;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.business.User;
import com.weike.data.model.req.DelAniDayReq;
import com.weike.data.model.req.DeleteRelatedClientReq;
import com.weike.data.model.req.GetAttrListReq;
import com.weike.data.model.resp.GetAttrListResp;
import com.weike.data.model.resp.GetClientDetailMsgResp;
import com.weike.data.model.viewmodel.AddClientRelateItemVM;
import com.weike.data.model.viewmodel.AddPhoneVM;
import com.weike.data.model.viewmodel.AnniversariesItemVM;
import com.weike.data.model.viewmodel.AnotherAttrItemVM;
import com.weike.data.model.viewmodel.AttrItemVM;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by LeoLu on 2018/6/4.
 * 客户基本信息Fragment
 */

public class ClientBaseMsgFragment extends BaseFragment implements View.OnClickListener, AddPhoneVM.OnPhoneClickListener, AddClientRelateItemVM.AddClientRelateItemListener, AnniversariesItemVM.AnniversariseDayClickListener,View.OnLongClickListener {

    /**
     * 纪念日专用
     */
    public static final int REQUEST_ANNIDAY_CODE = 1000;

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

    private HashMap<String,AnotherAttributes> tmpMap = new HashMap<>();


    public  ClipboardManager  myClipboard;
    public     ClipData     myClip;



    List<AnotherAttributes>  fromNetList=new ArrayList<>();

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
            clientBaseMsgVM.sonDisplay.set(true);
            clientBaseMsgVM.girlDisplay.set(true);
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


            if(TextUtils.isEmpty(data.getSonNum())  && TextUtils.isEmpty(data.getDaughterNum()) ){
                clientBaseMsgVM.bearDisplay.set(false);
            }else{
                clientBaseMsgVM.bearDisplay.set(true);
                if(TextUtils.isEmpty(data.getSonNum())){
                    clientBaseMsgVM.sonDisplay.set(false);
                }else{
                    clientBaseMsgVM.sonDisplay.set(true);
                }

                if(TextUtils.isEmpty(data.getDaughterNum())){
                    clientBaseMsgVM.girlDisplay.set(false);
                }else{
                    clientBaseMsgVM.girlDisplay.set(true);
                }
            }
//
//            if (TextUtils.isEmpty(data.getSonNum()) == false || TextUtils.isEmpty(data.getDaughterNum()) == false) {
//                clientBaseMsgVM.bearDisplay.set(true);
//            } else {
//                clientBaseMsgVM.bearDisplay.set(false);
//            }


            if (TextUtils.isEmpty(data.getMarriage()) || Integer.parseInt(data.getMarriage()) == -1) {
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

    /**
     * 初始化
     */
    public void initAnotherAttr() {

        anotherAdapter = new BaseDataBindingAdapter(getActivity(), R.layout.widget_another_attr_list_litem, anotherAttrItemVMS, BR.anotherItemVM);



        GetAttrListReq req = new GetAttrListReq();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);

        fromNetList.clear();
        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_ATTR_LIST)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetAttrListResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetAttrListResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetAttrListResp> getAttrListRespBaseResp) throws Exception {





                fromNetList=new ArrayList<>();

                LogUtil.d("test","fragment进入数据"+JsonUtil.GsonString(getAttrListRespBaseResp.getDatas().attributesValueList));
                for(int i = 0; i < getAttrListRespBaseResp.getDatas().attributesValueList.size();i++) {

                    AnotherAttributes anotherAttributes=new AnotherAttributes();
                    anotherAttributes.attributesId=getAttrListRespBaseResp.getDatas().attributesValueList.get(i).id;
                    anotherAttributes.attributesName=getAttrListRespBaseResp.getDatas().attributesValueList.get(i).attributesName;
                    for(int j=0;j<anotherAttrItemVMS.size();j++){
                        if(anotherAttributes.attributesName.equals(anotherAttrItemVMS.get(j).name.get())){
                            anotherAttributes.attributesValueId=anotherAttrItemVMS.get(j).attributesValueId;
                            anotherAttributes.attributesContent=anotherAttrItemVMS.get(j).value.get();
                        }
                    }
                    fromNetList.add(anotherAttributes);

                }
                tmpMap.clear();
                anotherAttrItemVMS.clear();
                List<AnotherAttributes> list = SpUtil.getInstance().getUser().anotherAttributes;

                if(list.size()==0){

                    for(int j=0;j<fromNetList.size();j++){

                        AnotherAttrItemVM vm = new AnotherAttrItemVM();
                        vm.name.set(fromNetList.get(j).attributesName);

                        vm.id = fromNetList.get(j).attributesId;
                        vm.attributesValueId=fromNetList.get(j).attributesValueId;
                        vm.value.set(fromNetList.get(j).attributesContent);
//                        if(fromNetList.get(j).attributesContent==null||fromNetList.get(j).attributesContent.equals("")){
//                            vm.toVisibility=false;
//                        }
                        vm.isModify.set(true);
                        anotherAttrItemVMS.add(vm);


                    }

                }else{



                    LogUtil.d("test","本地有数据");
                    for(int j=0;j<list.size();j++){

                        AnotherAttrItemVM vm = new AnotherAttrItemVM();
                        vm.name.set(list.get(j).attributesName);

                        vm.id = list.get(j).attributesId;
                        vm.attributesValueId=list.get(j).attributesValueId;
                        vm.value.set(list.get(j).attributesContent);
//                        if(list.get(j).attributesContent==null||list.get(j).attributesContent.equals("")){
//                            vm.toVisibility=false;
//                        }
                        vm.isModify.set(true);
                        anotherAttrItemVMS.add(vm);


                    }
                    for(int k=0;k<fromNetList.size();k++){
                        boolean isToAdd=true;
                        for(int j=0;j<list.size()&&isToAdd;j++){
                            if(list.get(j).attributesName.equals(fromNetList.get(k).attributesName)){
                                isToAdd=false;
                            }
                        }
                        if(isToAdd){
                            AnotherAttrItemVM vm = new AnotherAttrItemVM();
                            vm.name.set(fromNetList.get(k).attributesName);

                            vm.id = fromNetList.get(k).attributesId;
                            vm.attributesValueId=fromNetList.get(k).attributesValueId;
                            vm.value.set(fromNetList.get(k).attributesContent);
//                            if(fromNetList.get(k).attributesContent==null||fromNetList.get(k).attributesContent.equals("")){
//                                vm.toVisibility=false;
//                            }
                            vm.isModify.set(true);
                            anotherAttrItemVMS.add(vm);
                        }
                    }
                }


               // anotherAdapter = new BaseDataBindingAdapter(getActivity(), R.layout.widget_another_attr_list_litem, anotherAttrItemVMS, BR.anotherItemVM);
                NoScrollLinearLayoutManager linearLayoutManager = new NoScrollLinearLayoutManager(getActivity());
                linearLayoutManager.setScrollEnabled(false);
                binding.anotherAttrRecycle.setLayoutManager(linearLayoutManager);
                binding.anotherAttrRecycle.setAdapter(anotherAdapter);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                LogUtil.d("test","fragment进入数据失败");
            }
        });


    }





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
            if (birthdayRemind.isRemind == DataConfig.RemindType.TYPE_REMIND) {
                clientBaseMsgVM.birthdayRemindIcon.set(R.mipmap.ic_remind);
            } else {
                clientBaseMsgVM.birthdayRemindIcon.set(R.mipmap.ic_remind_dis);
            }

            clientBaseMsgVM.birthDayTodo = birthdayRemind;
        }

        updateClientRelate(data.getClientRelatedList());
        //纪念日相关
        updateAnnaDay(data.getAnniversaryList()); //纪念日

        updateAnotherType(data.getUserAttributesList());
    }

    /**
     * 用于上传
     * @return
     */
    public String getAnotherAttr() {

        List<AnotherAttributes> list = new ArrayList<>();

        for (int i = 0; i < anotherAttrItemVMS.size(); i++) {

            if (TextUtils.isEmpty(anotherAttrItemVMS.get(i).value.get())&&TextUtils.isEmpty(anotherAttrItemVMS.get(i).attributesValueId)){
                continue;
            }


            AnotherAttributes atr = new AnotherAttributes();
            atr.attributesName = anotherAttrItemVMS.get(i).name.get();
            atr.attributesContent = anotherAttrItemVMS.get(i).value.get()==null?"":anotherAttrItemVMS.get(i).value.get();
            atr.attributesId = anotherAttrItemVMS.get(i).id;

            atr.attributesValueId=anotherAttrItemVMS.get(i).attributesValueId;
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
    //纪念日相关
    public void updateAnnaDay(List<GetClientDetailMsgResp.AnniversaryListBean> list) {
        anniDayVMS.clear();
        for (int i = 0; i < list.size(); i++) {
            AnniversariesItemVM vm = new AnniversariesItemVM(getActivity());

            vm.id.set(list.get(i).getId() + "");

            if (list.get(i).getRemind() != null && TextUtils.isEmpty(list.get(i).getRemind().content) == false) {

                ToDo toDo = new ToDo();
                toDo.content = list.get(i).getRemind().content;
                toDo.id = list.get(i).getRemind().id;
                toDo.isAdvance = Integer.parseInt(list.get(i).getRemind().isAdvance);

                toDo.advanceInterval = Integer.parseInt(list.get(i).getRemind().advanceInterval==null?"0":list.get(i).getRemind().advanceInterval);

                toDo.advanceDateType = Integer.parseInt(list.get(i).getRemind().advanceDateType==null?"0":list.get(i).getRemind().advanceDateType);
                toDo.repeatInterval = Integer.parseInt(list.get(i).getRemind().repeatInterval==null?"0":list.get(i).getRemind().repeatInterval);
                toDo.repeatDateType = Integer.parseInt(list.get(i).getRemind().repeatDateType==null?"0":list.get(i).getRemind().repeatDateType);
                toDo.isRepeat = Integer.parseInt(list.get(i).getRemind().isRepeat==null?"0":list.get(i).getRemind().isRepeat);
                toDo.birthdaydate = list.get(i).getRemind().remindDate;
                if (toDo.isRemind == 1) {
                    vm.remindIcon.set(R.mipmap.ic_remind);
                } else {
                    vm.remindIcon.set(R.mipmap.ic_remind_dis);
                }
                vm.toDo = toDo;
            }

            vm.isModify.set(false);
            vm.isFirst.set(false);
            vm.isShowContent.set(true);

            vm.setListener(this);
            vm.name.set(list.get(i).getAnniversaryName());
            vm.time.set(list.get(i).getAnniversaryDate());
            anniDayVMS.add(0, vm);
        }
        anniDayAdapter.notifyDataSetChanged();

    }

    //纪念日相关
    public void updateAnnaDayStatus(boolean isModify) {

        if (isModify ) {
            for (int i = 0; i < anniDayVMS.size(); i++) {
                anniDayVMS.get(i).isModify.set(true);
                anniDayVMS.get(i).isShowContent.set(false);

            }

            initAniDayHead(anniDayVMS.size());
            anniDayAdapter.notifyDataSetChanged();
        }

    }


    public void updateAnotherAttr(boolean isModify) {


        HashMap<String,AnotherAttributes> tmp= new HashMap<>();
        if (isModify) {



            fromNetList.clear();
            GetAttrListReq req = new GetAttrListReq();
            req.token = SpUtil.getInstance().getCurrentToken();
            req.sign = SignUtil.signData(req);


            RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_ATTR_LIST)
                    .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetAttrListResp>>(){

                    })).subscribe(new BaseObserver<BaseResp<GetAttrListResp>>() {
                @Override
                protected void onSuccess(BaseResp<GetAttrListResp> getAttrListRespBaseResp) throws Exception {



                    for(int i = 0 ; i < getAttrListRespBaseResp.getDatas().attributesValueList.size();i++) {

                        AnotherAttributes anotherAttributes=new AnotherAttributes();
                        anotherAttributes.attributesId=getAttrListRespBaseResp.getDatas().attributesValueList.get(i).id;
                        anotherAttributes.attributesName=getAttrListRespBaseResp.getDatas().attributesValueList.get(i).attributesName;
                        for(int j=0;j<anotherAttrItemVMS.size();j++){
                            if(anotherAttributes.attributesName.equals(anotherAttrItemVMS.get(j).name.get())){
                                anotherAttributes.attributesValueId=anotherAttrItemVMS.get(j).attributesValueId;
                                anotherAttributes.attributesContent=anotherAttrItemVMS.get(j).value.get();
                            }
                        }
                        fromNetList.add(anotherAttributes);



                    }


                    LogUtil.d("test","fragment进入数据"+JsonUtil.GsonString(getAttrListRespBaseResp.getDatas().attributesValueList));


                    tmpMap.clear();
                    anotherAttrItemVMS.clear();


                    List<AnotherAttributes> list = SpUtil.getInstance().getUser().anotherAttributes;



                    LogUtil.d("test","初始化属性长度"+fromNetList.size());


                    if(list.size()==0){
                        LogUtil.d("test","初始化本地沒有数据");
                        for(int j=0;j<fromNetList.size();j++){

                            AnotherAttrItemVM vm = new AnotherAttrItemVM();
                            vm.name.set(fromNetList.get(j).attributesName);

                            vm.id = fromNetList.get(j).attributesId;
                            vm.attributesValueId=fromNetList.get(j).attributesValueId;
                            vm.value.set(fromNetList.get(j).attributesContent);
//                            if(fromNetList.get(j).attributesContent==null||fromNetList.get(j).attributesContent.equals("")){
//                                vm.toVisibility=false;
//                            }

                            vm.isModify.set(true);
                            anotherAttrItemVMS.add(vm);


                        }

                    }else{
                        LogUtil.d("test","本地有数据");
                        for(int j=0;j<list.size();j++){

                            AnotherAttrItemVM vm = new AnotherAttrItemVM();
                            vm.name.set(list.get(j).attributesName);

                            vm.id = list.get(j).attributesId;
                            vm.attributesValueId=list.get(j).attributesValueId;
                            vm.value.set(list.get(j).attributesContent);
//                            if(list.get(j).attributesContent==null||list.get(j).attributesContent.equals("")){
//                                vm.toVisibility=false;
//                            }

                            vm.isModify.set(true);
                            anotherAttrItemVMS.add(vm);


                        }
                        for(int k=0;k<fromNetList.size();k++){
                            boolean isToAdd=true;
                            for(int j=0;j<list.size()&&isToAdd;j++){
                                if(list.get(j).attributesName.equals(fromNetList.get(k).attributesName)){
                                    isToAdd=false;
                                }
                            }
                            if(isToAdd){
                                AnotherAttrItemVM vm = new AnotherAttrItemVM();
                                vm.name.set(fromNetList.get(k).attributesName);

                                vm.id = fromNetList.get(k).attributesId;
                                vm.attributesValueId=fromNetList.get(k).attributesValueId;
                                vm.value.set(fromNetList.get(k).attributesContent);
//                                if(fromNetList.get(k).attributesContent==null||fromNetList.get(k).attributesContent.equals("")){
//                                    vm.toVisibility=false;
//                                }

                                vm.isModify.set(true);
                                anotherAttrItemVMS.add(vm);
                            }
                        }
                    }
                    anotherAdapter.notifyDataSetChanged();
                }

                @Override
                protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

                }
            });







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


    //ClientBaseMsgFragment返回
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        /**
         * 用于生日
         */
        if (requestCode == REQUEST_ANNIDAY_CODE && resultCode == RESULT_OK && data != null) {

            ToDo toDo = data.getParcelableExtra(RemindSettingActivity.KEY_OF_TODO);


            lastAnniversariesItemVM.toDo = toDo;
            if (toDo.isRemind == 1) {
                lastAnniversariesItemVM.remindIcon.set(R.mipmap.ic_remind);
            } else {
                lastAnniversariesItemVM.remindIcon.set(R.mipmap.ic_remind_dis);
            }
        } else if (requestCode == RemindSettingActivity.CODE_OF_REQUEST && resultCode == RESULT_OK && data != null) {
            ToDo toDo = data.getParcelableExtra(RemindSettingActivity.KEY_OF_TODO);
            clientBaseMsgVM.onBirthdayRemindResult(toDo);

        } else if (requestCode == RelateClientActivity.REQUEST_CODE && data != null) {
            List<ClientRelated> list = (List<ClientRelated>) data.getSerializableExtra("data");

            lastRelateClientVM.clientId = list.get(0).clientId;
            lastRelateClientVM.clientName.set(list.get(0).name);
            clientRelateAdapter.notifyDataSetChanged();


        } else if (requestCode == AttrManagerActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            LogUtil.d("test","添加属性后返回");
            anotherAttrItemVMS.clear();
            initAnotherAttr();

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

    public void updateAnotherType(List<GetClientDetailMsgResp.AnotherAttrBean> anotherAttrBeans) {

        User user=SpUtil.getInstance().getUser();
        List<AnotherAttributes>  anotherAttributesList=new ArrayList<>();

        for(int i=0;i<anotherAttrBeans.size();i++){
            AnotherAttributes anotherAttributes=new AnotherAttributes();
            anotherAttributes.attributesName=anotherAttrBeans.get(i).attributesName;
            anotherAttributes.attributesId=anotherAttrBeans.get(i).id;
            anotherAttributes.attributesContent=anotherAttrBeans.get(i).attributesValue;
            anotherAttributes.attributesValueId=anotherAttrBeans.get(i).attributesValueId;
            anotherAttributesList.add(anotherAttributes);
        }
        user.anotherAttributes=anotherAttributesList;

        SpUtil.getInstance().saveNewsUser(user);

        anotherAttrItemVMS.clear();
        for(int i = 0 ; i < anotherAttrBeans.size();i++){
            //AnotherAttributes tmp = tmpMap.get(attr.get(i).attributesName);
            if (TextUtils.isEmpty(anotherAttrBeans.get(i).attributesValue) == false) {//包含相同的名字
                AnotherAttrItemVM vm = new AnotherAttrItemVM();

                vm.name.set(anotherAttrBeans.get(i).attributesName);
                vm.attributesValueId=anotherAttrBeans.get(i).attributesValueId;

                vm.value.set(anotherAttrBeans.get(i).attributesValue);


                vm.id = anotherAttrBeans.get(i).id;

                vm.isModify.set(false);
                anotherAttrItemVMS.add(vm);
            }
        }


        if (anotherAttrItemVMS.size() > 0) {
            anotherAttrItemVMS.get(anotherAttrItemVMS.size() - 1).lineShow.set(false);
        }

        if (anotherAttrItemVMS.size() == 0) {
            clientBaseMsgVM.anotherAttrDisplay.set(false);
        } else {
            clientBaseMsgVM.anotherAttrDisplay.set(true);
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

    //纪念日相关
    private void initAniDay() {
        anniDayAdapter = new BaseDataBindingAdapter(getActivity(), R.layout.widget_layout_ani_day, anniDayVMS, BR.anniverVM);
        NoScrollLinearLayoutManager linearLayoutManager = new NoScrollLinearLayoutManager(getActivity());
        linearLayoutManager.setScrollEnabled(false);
        binding.recycleAnnidayList.setLayoutManager(linearLayoutManager);
        binding.recycleAnnidayList.setAdapter(anniDayAdapter);

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
            vm.id=list.get(i).id+"";
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


    //纪念日相关
    private void initAniDayHead(int position) {

        AnniversariesItemVM vm = new AnniversariesItemVM(getActivity());
        vm.setListener(this);

        vm.isModify.set(false);
        vm.isFirst.set(true);
        anniDayVMS.add(position,vm);


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
        addPhoneAdapter.setOnRecyclerViewItemClickListener(new BaseDataBindingAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                LinearLayout linearLayout=(LinearLayout) binding.addPhoneNum.getChildAt(position);
                TextView  phoneText=linearLayout.findViewById(R.id.ed_email_content);
                String phoneContent=phoneText.getText().toString();
                if(phoneContent.length()>0){
                    DialogUtil.showButtonDialog(getFragmentManager(), "提示", "是否要复制", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LogUtil.d("test","取消电话"+phoneContent);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myClip= ClipData.newPlainText("Simple test", phoneContent);
                            //把clip对象放在剪贴板中
                            myClipboard.setPrimaryClip(myClip);
                            myClip = myClipboard .getPrimaryClip();
                            //获取到内容
                            ClipData.Item item = myClip.getItemAt(0);
                            String text = item.getText().toString();
                            LogUtil.d("test","复制"+text);
                            LogUtil.d("test","电话"+phoneContent);
                        }
                    });

                }
            }
        });


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


        anotherAdapter = new BaseDataBindingAdapter(getActivity(), R.layout.widget_another_attr_list_litem, anotherAttrItemVMS, BR.anotherItemVM);
        NoScrollLinearLayoutManager linearLayoutManager = new NoScrollLinearLayoutManager(getActivity());
        linearLayoutManager.setScrollEnabled(false);
        binding.anotherAttrRecycle.setLayoutManager(linearLayoutManager);
        binding.anotherAttrRecycle.setAdapter(anotherAdapter);

        initPhoneRecycle();
        initClientRelateRecycle();
        initAniDay();
       // initAnotherAttr();
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

        myClipboard=(ClipboardManager) Objects.requireNonNull(getActivity()).getSystemService(CLIPBOARD_SERVICE);


        binding.edCompany.setOnLongClickListener(this);
        binding.edEmailContent.setOnLongClickListener(this);
        binding.edJob.setOnLongClickListener(this);
        binding.edCompanyLocation.setOnLongClickListener(this);
        binding.edHomeLocation.setOnLongClickListener(this);
        binding.edIdCardNum.setOnLongClickListener(this);

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
        LogUtil.d("test",type+"类型");
        if (type == AddClientRelateItemVM.AddClientRelateItemListener.ADD_ITEM) {
            AddClientRelateItemVM current = new AddClientRelateItemVM();
            current.isFirst.set(false);
            current.isModify.set(false);
            current.setAddClientRelateItemListener(this);

            clientRelateItemVMS.add(current);
            clientRelateAdapter.notifyDataSetChanged();


        } else if (type == AddClientRelateItemVM.AddClientRelateItemListener.ADD_RELATE) {

            RelateClientActivity.startActivity(true, this, RelateClientActivity.REQUEST_CODE,WKBaseApplication.getInstance().id);
            lastRelateClientVM = vm;

        } else if (type == AddClientRelateItemVM.AddClientRelateItemListener.REDUCE) {



            DialogUtil.showButtonDialog(getFragmentManager(), "提示", "是否删除关联客户", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LogUtil.d("test","取消");
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.d("test","删除");

                    if(vm.id==null){
                        clientRelateItemVMS.remove(vm);
                        clientRelateAdapter.notifyDataSetChanged();
                    }else{
                        DeleteRelatedClientReq deleteRelatedClientReq=new DeleteRelatedClientReq();
                        deleteRelatedClientReq.id = vm.id;
                        deleteRelatedClientReq.sign = SignUtil.signData(deleteRelatedClientReq);


                        RetrofitFactory.getInstance().getService().postAnything(deleteRelatedClientReq, Config.DELETE_RELATED_CLIENT)
                                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp>() {

                                })).subscribe(new BaseObserver<BaseResp>() {
                            @Override
                            protected void onSuccess(BaseResp baseResp) throws Exception {
                                LogUtil.d("test","删除");


                                vm.clientId = null;
                                vm.clientName.set("请选择");
                                clientRelateAdapter.notifyDataSetChanged();
                            }

                            @Override
                            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                                LogUtil.d("test","失败");
                            }
                        });
                    }


                }
            });

        } else if (type == AddClientRelateItemVM.AddClientRelateItemListener.CANCEL_FIRST) {

            DialogUtil.showButtonDialog(getFragmentManager(), "提示", "是否删除关联客户", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LogUtil.d("test","取消");
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.d("test","删除");

                    if(vm.id==null){
                        clientRelateItemVMS.remove(vm);
                        clientRelateAdapter.notifyDataSetChanged();
                    }else{
                        DeleteRelatedClientReq deleteRelatedClientReq=new DeleteRelatedClientReq();
                        deleteRelatedClientReq.id = vm.id;
                        deleteRelatedClientReq.sign = SignUtil.signData(deleteRelatedClientReq);


                        RetrofitFactory.getInstance().getService().postAnything(deleteRelatedClientReq, Config.DELETE_RELATED_CLIENT)
                                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp>() {

                                })).subscribe(new BaseObserver<BaseResp>() {
                            @Override
                            protected void onSuccess(BaseResp baseResp) throws Exception {
                                LogUtil.d("test","删除");


                                vm.clientId = null;
                                vm.clientName.set("请选择");
                                clientRelateAdapter.notifyDataSetChanged();
                            }

                            @Override
                            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                                LogUtil.d("test","失败");
                            }
                        });
                    }






                }
            });

        }

    }
    //纪念日相关
    @Override
    public void anniDayClick(int type, AnniversariesItemVM item) {
        if (type == 1) { //add
            AnniversariesItemVM news = new AnniversariesItemVM(getActivity());
            news.isFirst.set(false);
            news.isModify.set(true);
            news.isShowContent.set(false);
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


    //纪念日相关
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

    @Override
    public boolean onLongClick(View view) {
        String content=((EditText) view).getText().toString().trim();
        if(content.length()>0){

            DialogUtil.showButtonDialog(getFragmentManager(), "提示", "是否要复制", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LogUtil.d("test","取消"+content);
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myClip= ClipData.newPlainText("Simple test", content);
                    //把clip对象放在剪贴板中
                    myClipboard.setPrimaryClip(myClip);
                    myClip = myClipboard .getPrimaryClip();
                    //获取到内容
                    ClipData.Item item = myClip.getItemAt(0);
                    String text = item.getText().toString();
                    LogUtil.d("test","复制"+text);

                }
            });

         }
        return false;
    }
}
