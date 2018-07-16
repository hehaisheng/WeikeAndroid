package com.weike.data.business.log;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.config.DataConfig;
import com.weike.data.databinding.ActivityRemindSettingBinding;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.req.GetOneTodoStatusReq;
import com.weike.data.model.req.ModifyOneTodoReq;
import com.weike.data.model.resp.GetOneTodoStatusResp;
import com.weike.data.model.viewmodel.RemingSetActVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

/**
 * Created by LeoLu on 2018/6/6.
 */

public class RemindSettingActivity extends BaseActivity {

    ActivityRemindSettingBinding binding;
    RemingSetActVM vm;

    public static final String KEY_OF_TODO = "com.RemindSettingActivity.KEY_OF_TODO";
    public static final String KEY_OF_ID = "com.RemindSettingActivity.KEY_OF_ID";

    public static final int CODE_OF_REQUEST = 500;

    private String id;
    private ToDo toDo;

    public static void startActivity(Activity activity, ToDo toDo){
        Intent intent = new Intent(activity,RemindSettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(KEY_OF_TODO , toDo);
        activity.startActivityForResult(intent,CODE_OF_REQUEST);
    }

    /**
     * 用户Fragment
     * @param fragment
     * @param toDo
     */
    public static void startActivity(BaseFragment fragment, ToDo toDo){
        Intent intent = new Intent(fragment.getContext(),RemindSettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(KEY_OF_TODO , toDo);
        fragment.startActivityForResult(intent,CODE_OF_REQUEST);
    }

    public static void startActivity(BaseFragment fragment, String id){
        Intent intent = new Intent(fragment.getContext(),RemindSettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(KEY_OF_ID , id);
        fragment.startActivityForResult(intent,CODE_OF_REQUEST);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_remind_setting);
        vm = new RemingSetActVM(this);

        toDo = getIntent().getParcelableExtra(KEY_OF_TODO);
        id = getIntent().getStringExtra(KEY_OF_ID);


        if (toDo != null) {
            initCurrentTodo();
        }

        if (id != null) {
            initCurrentTodoByNet();
        }

        binding.setRemindSettingVM(vm);
        setRightText("保存");
        setLeftText("设置提醒");
        setCenterText("");


    }

    private void initCurrentTodoByNet(){
        GetOneTodoStatusReq req = new GetOneTodoStatusReq();
        req.toDoId = id;
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_ONE_TODO_STATUS)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetOneTodoStatusResp>>() {

                })).subscribe(new BaseObserver<BaseResp<GetOneTodoStatusResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetOneTodoStatusResp> getHandleWorkListRespBaseResp) throws Exception {
                vm.isRemind.set(getHandleWorkListRespBaseResp.getDatas().isRemind == 1  ? true : false); //是否是提醒 和 不提醒
                vm.isUnRemind.set(getHandleWorkListRespBaseResp.getDatas().isRemind == 2  ? true : false);


                vm.content.set(getHandleWorkListRespBaseResp.getDatas().content); //内容
                vm.heightCheck.set(getHandleWorkListRespBaseResp.getDatas().priority == 1 ? true : false);
                vm.midCheck.set(getHandleWorkListRespBaseResp.getDatas().priority == 2 ? true : false);
                vm.lowCheck.set(getHandleWorkListRespBaseResp.getDatas().priority == 3 ? true :false);
                vm.time.set(getHandleWorkListRespBaseResp.getDatas().birthdaydate);

                vm.advanceDateType = getHandleWorkListRespBaseResp.getDatas().advanceDateType;
                vm.advanceInterval = getHandleWorkListRespBaseResp.getDatas().advanceInterval;
                vm.isAdvance = getHandleWorkListRespBaseResp.getDatas().isAdvance;

                vm.isRepeat = getHandleWorkListRespBaseResp.getDatas().isRepeat;
                vm.repeatDateType = getHandleWorkListRespBaseResp.getDatas().repeatDateType;
                vm.repeatInterval = getHandleWorkListRespBaseResp.getDatas().repeatInterval;


                if (getHandleWorkListRespBaseResp.getDatas().isRepeat == 1) { //重复
                    if (getHandleWorkListRespBaseResp.getDatas().repeatDateType == DataConfig.RemindDateType.TYPE_OF_DAY){
                        //天
                        vm.repeatText.set("" + getHandleWorkListRespBaseResp.getDatas().repeatInterval + "天");
                    } else if (getHandleWorkListRespBaseResp.getDatas().repeatDateType == DataConfig.RemindDateType.TYPE_OF_WEEK) {
                        vm.repeatText.set("" + getHandleWorkListRespBaseResp.getDatas().repeatInterval + "周");
                    } else if (getHandleWorkListRespBaseResp.getDatas().repeatDateType == DataConfig.RemindDateType.TYPE_OF_MONTH) {
                        vm.repeatText.set("" + getHandleWorkListRespBaseResp.getDatas().repeatInterval + "月");
                    } else if (getHandleWorkListRespBaseResp.getDatas().repeatDateType == DataConfig.RemindDateType.TYPE_OF_YEAR) {
                        vm.repeatText.set("" + getHandleWorkListRespBaseResp.getDatas().repeatInterval + "年");
                    } else if (getHandleWorkListRespBaseResp.getDatas().repeatDateType == DataConfig.RemindDateType.TYPE_OF_MIN) {
                        vm.repeatText.set("" + getHandleWorkListRespBaseResp.getDatas().repeatInterval + "分钟");
                    } else if (getHandleWorkListRespBaseResp.getDatas().repeatDateType == DataConfig.RemindDateType.TYPE_OF_HOUR) {
                        vm.repeatText.set("" + getHandleWorkListRespBaseResp.getDatas().repeatInterval + "小时");
                    } else if (getHandleWorkListRespBaseResp.getDatas().repeatDateType == 0) {
                        vm.repeatText.set("不重复");
                    }
                } else {
                    vm.repeatText.set("不重复");
                }

                if (getHandleWorkListRespBaseResp.getDatas().isAdvance == 1) { //重复
                    if (getHandleWorkListRespBaseResp.getDatas().advanceDateType == DataConfig.RemindDateType.TYPE_OF_DAY){
                        //天
                        vm.remindTime.set("提前" + getHandleWorkListRespBaseResp.getDatas().advanceInterval + "天");
                    } else if (getHandleWorkListRespBaseResp.getDatas().advanceDateType == DataConfig.RemindDateType.TYPE_OF_WEEK) {
                        vm.remindTime.set("提前" + getHandleWorkListRespBaseResp.getDatas().advanceInterval + "周");
                    } else if (getHandleWorkListRespBaseResp.getDatas().advanceDateType == DataConfig.RemindDateType.TYPE_OF_MONTH) {
                        vm.remindTime.set("提前" + getHandleWorkListRespBaseResp.getDatas().advanceInterval + "月");
                    } else if (getHandleWorkListRespBaseResp.getDatas().advanceDateType == DataConfig.RemindDateType.TYPE_OF_YEAR) {
                        vm.remindTime.set("提前" + getHandleWorkListRespBaseResp.getDatas().advanceInterval + "年");
                    } else if (getHandleWorkListRespBaseResp.getDatas().advanceDateType == DataConfig.RemindDateType.TYPE_OF_MIN) {
                        vm.remindTime.set("提前" + getHandleWorkListRespBaseResp.getDatas().advanceInterval + "分钟");
                    } else if (getHandleWorkListRespBaseResp.getDatas().advanceDateType == DataConfig.RemindDateType.TYPE_OF_HOUR) {
                        vm.remindTime.set("提前" + getHandleWorkListRespBaseResp.getDatas().advanceInterval + "小时");
                    } else if (getHandleWorkListRespBaseResp.getDatas().advanceDateType == 0) {
                        vm.remindTime.set("不提前");
                    }
                } else {
                    vm.remindTime.set("不提前");
                }

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    private void initCurrentTodo(){
        LogUtil.d("ActhomeRemindSetting","" + JsonUtil.GsonString(toDo));

        vm.isRemind.set(toDo.isRemind == 1  ? true : false);
        vm.isUnRemind.set(toDo.isRemind == 2  ? true : false);
        vm.content.set(toDo.content); //内容
        vm.heightCheck.set(toDo.priority == 1 ? true : false);
        vm.midCheck.set(toDo.priority == 2 ? true : false);
        vm.lowCheck.set(toDo.priority == 3 ? true :false);
        vm.time.set(toDo.birthdaydate);
        vm.advanceDateType = toDo.advanceDateType;
        vm.advanceInterval = toDo.advanceInterval;
        vm.isAdvance = toDo.isAdvance;
        vm.isRepeat = toDo.isRepeat;
        vm.repeatDateType = toDo.repeatDateType;
        vm.repeatInterval = toDo.repeatInterval;

        //


        if (toDo.isRepeat == 1) { //重复
            if (toDo.repeatDateType == DataConfig.RemindDateType.TYPE_OF_DAY){
                //天
                vm.repeatText.set("" + toDo.repeatInterval + "天");
            } else if (toDo.repeatDateType == DataConfig.RemindDateType.TYPE_OF_WEEK) {
                vm.repeatText.set("" + toDo.repeatInterval + "周");
            } else if (toDo.repeatDateType == DataConfig.RemindDateType.TYPE_OF_MONTH) {
                vm.repeatText.set("" + toDo.repeatInterval + "月");
            } else if (toDo.repeatDateType == DataConfig.RemindDateType.TYPE_OF_YEAR) {
                vm.repeatText.set("" + toDo.repeatInterval + "年");
            } else if (toDo.repeatDateType == DataConfig.RemindDateType.TYPE_OF_MIN) {
                vm.repeatText.set("" + toDo.repeatInterval + "分钟");
            } else if (toDo.repeatDateType == DataConfig.RemindDateType.TYPE_OF_HOUR) {
                vm.repeatText.set("" + toDo.repeatInterval + "小时");
            }
        } else {
            vm.repeatText.set("不重复");
        }

        if (toDo.isAdvance == 1) { //提前
            if (toDo.advanceDateType == DataConfig.RemindDateType.TYPE_OF_DAY){
                //天
                vm.remindTime.set("提前" + toDo.advanceInterval + "天");
            } else if (toDo.advanceDateType == DataConfig.RemindDateType.TYPE_OF_WEEK) {
                vm.remindTime.set("提前" + toDo.advanceInterval + "周");
            } else if (toDo.advanceDateType == DataConfig.RemindDateType.TYPE_OF_MONTH) {
                vm.remindTime.set("提前" + toDo.advanceInterval + "月");
            } else if (toDo.advanceDateType == DataConfig.RemindDateType.TYPE_OF_YEAR) {
                vm.remindTime.set("提前" + toDo.advanceInterval + "年");
            } else if (toDo.repeatDateType == DataConfig.RemindDateType.TYPE_OF_MIN) {
                vm.remindTime.set("提前" + toDo.advanceInterval + "分钟");
            } else if (toDo.repeatDateType == DataConfig.RemindDateType.TYPE_OF_HOUR) {
                vm.remindTime.set("提前" + toDo.advanceInterval + "小时");
            }
        } else {
            vm.remindTime.set("不提前");
        }
    }

    @Override
    public void onRightClick() {



        if (TextUtils.isEmpty(vm.content.get()) && vm.isRemind.get()){
            ToastUtil.showToast("内容不能为空");
            return;
        }

        if(TextUtils.isEmpty(vm.time.get())  && vm.isRemind.get()) {
            ToastUtil.showToast("请选择时间");
            return;
        }

        if (TextUtils.isEmpty(vm.remindTime.get())  && vm.isRemind.get()){
            ToastUtil.showToast("请设置您的提前提醒时间");
            return;
        }


        if (TextUtils.isEmpty(vm.repeatText.get())  && vm.isRemind.get()){
            ToastUtil.showToast("请设置您的重复提醒时间");
            return;
        }


        toDo = compass();
        LogUtil.d("addlog","bef:" + JsonUtil.GsonString(toDo));

        if (id != null) { //如果ID 不为空 那么就是修改
            modifyOneTodo();
        } else {
            Intent intent = new Intent();
            intent.putExtra(KEY_OF_TODO,toDo);
            setResult(RESULT_OK,intent);
            finish();
        }



    }

    private void modifyOneTodo(){
        ModifyOneTodoReq req  = new ModifyOneTodoReq();
        req.isRemind = toDo.isRemind;
        req.content = toDo.content;
        req.remindId = id;
        req.isRepeat = toDo.isRepeat;
        req.priority = toDo.priority;
        req.birthdaydate = toDo.birthdaydate;
        req.isAdvance = toDo.isAdvance;
        req.advanceDateType = toDo.advanceDateType;
        req.advanceInterval = toDo.advanceInterval;
        req.isRepeat = toDo.isRepeat;
        req.repeatDateType = toDo.repeatDateType;


        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.MODIFY_ONE_TODO_STATUS)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetOneTodoStatusResp>>() {

                })).subscribe(new BaseObserver<BaseResp<GetOneTodoStatusResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetOneTodoStatusResp> getHandleWorkListRespBaseResp) throws Exception {
                Intent intent = new Intent();
                intent.putExtra(KEY_OF_TODO,toDo);
                setResult(RESULT_OK,intent);
                finish();
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    private ToDo compass(){

        ToDo toDo = new ToDo();


        if (vm.isRemind.get()) {
            toDo.isRemind = 1;
        } else {
            toDo.isRemind = 2;
        }

        toDo.content = vm.content.get();
        toDo.birthdaydate = vm.time.get();
        if (vm.heightCheck.get()) {
            toDo.priority = DataConfig.RemindLevel.TYPE_OF_HEIGHT;
        } else if (vm.midCheck.get()) {
            toDo.priority = DataConfig.RemindLevel.TYPE_OF_MID;
        } else if (vm.lowCheck.get()) {
            toDo.priority = DataConfig.RemindLevel.TYPE_OF_LOAD;
        }



        if (vm.repeatText.get().contains("不重复")) {
            toDo.isRepeat = DataConfig.RemindRepeat.TYPE_UNREPEAT;
        } else {
            toDo.isRepeat = DataConfig.RemindRepeat.TYPE_REPEAT;
            toDo.repeatInterval = vm.repeatInterval;
            toDo.repeatDateType = vm.repeatDateType;
        }

        if (vm.remindTime.get().contains("不提前")) {
            toDo.isAdvance = DataConfig.RemindRepeat.TYPE_UNREPEAT;
        } else {
            toDo.isAdvance = DataConfig.RemindRepeat.TYPE_REPEAT;
            toDo.advanceInterval = vm.advanceInterval;
            toDo.advanceDateType = vm.advanceDateType;
        }


        return toDo;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
