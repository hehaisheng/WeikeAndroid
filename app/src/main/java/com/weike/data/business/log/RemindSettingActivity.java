package com.weike.data.business.log;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseFragment;
import com.weike.data.config.DataConfig;
import com.weike.data.databinding.ActivityRemindSettingBinding;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.viewmodel.RemingSetActVM;
import com.weike.data.util.JsonUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.ToastUtil;

/**
 * Created by LeoLu on 2018/6/6.
 */

public class RemindSettingActivity extends BaseActivity {

    ActivityRemindSettingBinding binding;
    RemingSetActVM vm;

    public static final String KEY_OF_TODO = "com.RemindSettingActivity.KEY_OF_TODO";
    public static final String KEY_OF_ID = "com.RemindSettingActivity.KEY_OF_ID";

    public static final int CODE_OF_REQUEST = 500;


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
        intent.putExtra(KEY_OF_TODO , id);
        fragment.startActivityForResult(intent,CODE_OF_REQUEST);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_remind_setting);
        vm = new RemingSetActVM(this);

        toDo = getIntent().getParcelableExtra(KEY_OF_TODO);
        LogUtil.d("acthome","-->" +toDo);
        if (toDo != null) {
            LogUtil.d("RedmingSettingActivity","--->" + JsonUtil.GsonString(toDo));
            initCurrentTodo();
        }

        binding.setRemindSettingVM(vm);
        setRightText("保存");
        setCenterText("设置提醒");


    }

    private void initCurrentTodoByNet(){

    }

    private void initCurrentTodo(){
        vm.isRemind.set(toDo.isRemind == 1  ? true : false);
        vm.isUnRemind.set(toDo.isRemind == 2  ? true : false);
        vm.content.set(toDo.content); //内容
        vm.heightCheck.set(toDo.priority == 1 ? true : false);
        vm.midCheck.set(toDo.priority == 2 ? true : false);
        vm.lowCheck.set(toDo.priority == 3 ? true :false);
        vm.time.set(toDo.toDoDate);
        vm.remindTime.set(toDo.beforeRemindDay + "天");
        if (toDo.isRepeat == 1) { //重复
            if (toDo.dateType == DataConfig.RemindDateType.TYPE_OF_DAY){
                //天
                vm.repeatText.set("" + toDo.repeatIntervalHour + "天");
            } else if (toDo.dateType == DataConfig.RemindDateType.TYPE_OF_WEEK) {
                vm.repeatText.set("" + toDo.repeatIntervalHour + "周");
            } else if (toDo.dateType == DataConfig.RemindDateType.TYPE_OF_MONTH) {
                vm.repeatText.set("" + toDo.repeatIntervalHour + "月");
            } else if (toDo.dateType == DataConfig.RemindDateType.TYPE_OF_YEAR) {
                vm.repeatText.set("" + toDo.repeatIntervalHour + "年");
            }
        } else {
            vm.repeatText.set("不重复");
        }
    }

    @Override
    public void onRightClick() {



        if (TextUtils.isEmpty(vm.content.get())){
            ToastUtil.showToast("内容不能为空");
            return;
        }

        if(TextUtils.isEmpty(vm.time.get())) {
            ToastUtil.showToast("请选择时间");
            return;
        }

        if (TextUtils.isEmpty(vm.remindTime.get())){
            ToastUtil.showToast("请设置您的提前提醒时间");
            return;
        }


        if (TextUtils.isEmpty(vm.repeatText.get())){
            ToastUtil.showToast("请设置您的重复提醒时间");
            return;
        }



        Intent intent = new Intent();
        intent.putExtra(KEY_OF_TODO,compass());
        setResult(RESULT_OK,intent);
        finish();
    }

    private ToDo compass(){



        toDo.isRemind = vm.isRemind.get() ? DataConfig.RemindType.TYPE_REMIND : DataConfig.RemindType.TYPE_UNREMIND;
        toDo.content = vm.content.get();
        toDo.toDoDate = vm.time.get();
        if (vm.heightCheck.get()) {
            toDo.priority = DataConfig.RemindLevel.TYPE_OF_HEIGHT;
        } else if (vm.heightCheck.get()) {
            toDo.priority = DataConfig.RemindLevel.TYPE_OF_HEIGHT;
        } else if (vm.heightCheck.get()) {
            toDo.priority = DataConfig.RemindLevel.TYPE_OF_HEIGHT;
        }
        if (vm.repeatText.get().contains("不重复")) {
            toDo.isRepeat = DataConfig.RemindRepeat.TYPE_UNREPEAT;
        } else {
            toDo.isRepeat = DataConfig.RemindRepeat.TYPE_REPEAT;

        }
        toDo.repeatIntervalHour = vm.repeatIntervalHour;
        toDo.dateType = vm.dateType;
        toDo.beforeRemindDay = vm.beforeRemindDay;

        return toDo;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
