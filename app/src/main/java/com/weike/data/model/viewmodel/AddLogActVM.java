package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;
import com.weike.data.MainActivity;
import com.weike.data.adapter.CheckedAdapter;
import com.weike.data.base.BaseVM;
import com.weike.data.business.log.RemindSettingActivity;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.resp.GetClientListResp;
import com.weike.data.util.ActivitySkipUtil;
import com.weike.data.util.PickerUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;

import java.util.List;

import cn.addapp.pickers.picker.DateTimePicker;

/**
 * Created by LeoLu on 2018/5/30.
 */

public class AddLogActVM extends BaseVM {

    public ObservableField<String> time = new ObservableField<>();

    public ObservableField<String> content = new ObservableField<>();



    public ToDo toDo = new ToDo();

    public AddLogActVM(Activity activity){
        this.activity =activity;

    }

    public void setToDo(ToDo toDo) {
        this.toDo = toDo;
    }


    public void timeClick(){
        PickerUtil.onYearMonthDayTimePicker(null, new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String s, String s1, String s2, String s3, String s4) {
                time.set(s + "-" + s1 + "-" + s2 + " " + s3 + ":" + s4);
            }
        },activity);
    }

    public  AddLogActVM(FragmentActivity activity) {
        this.activity = activity;
    }

    /**
     * 添加关联
     */



    public void goToRemindSetting(){


        RemindSettingActivity.startActivity(activity,toDo);

    }
}
