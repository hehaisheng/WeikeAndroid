package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.weike.data.R;
import com.weike.data.base.BaseVM;
import com.weike.data.model.business.ToDo;
import com.weike.data.util.PickerUtil;
import com.weike.data.util.TimeUtil;

import java.util.List;

import cn.addapp.pickers.picker.DatePicker;

public class AnniversariesItemVM extends BaseVM {
    public ObservableField<Boolean> isFirst = new ObservableField<>();

    public ObservableField<Boolean> isModify = new ObservableField<>();

    public ObservableField<String> name = new ObservableField<>();

    public ObservableField<String> id = new ObservableField<>();

    public ObservableField<String> time = new ObservableField<>();

    public ObservableField<Integer> remindIcon = new ObservableField<>(R.mipmap.ic_remind_dis);

    public ObservableField<Boolean> isShowContent = new ObservableField<>();

    public AnniversariseDayClickListener listener;


    public ToDo toDo;

    public void add(){
        listener.anniDayClick(1,this);
    }

    public void reduce(){

        listener.anniDayClick(2,this);
    }

    public AnniversariesItemVM(Activity activity) {
        this.activity = activity;
    }

    public void timeSelector(){

        if (TextUtils.isEmpty(time.get())){
            List<Integer> tmp = TimeUtil.formatDateClick(TimeUtil.getTimeFormat("yyyy-MM-dd"));
            PickerUtil.onYearMonthDayPicker(tmp.get(0),tmp.get(1),tmp.get(2),activity, new DatePicker.OnYearMonthDayPickListener() {
                @Override
                public void onDatePicked(String s, String s1, String s2) {
                    time.set(s + "-" + s1 + "-" + s2);
                }
            });
        } else {
            List<Integer> tmp = TimeUtil.formatDateClick(time.get());
            PickerUtil.onYearMonthDayPicker(tmp.get(0),tmp.get(1),tmp.get(2),activity, new DatePicker.OnYearMonthDayPickListener() {
                @Override
                public void onDatePicked(String s, String s1, String s2) {
                    time.set(s + "-" + s1 + "-" + s2);
                }
            });
        }
    }

    public void setListener(AnniversariseDayClickListener listener) {
        this.listener = listener;
    }

    public void remind(){
        listener.anniDayClick(3,this);
    }

    public interface AnniversariseDayClickListener{
        public void anniDayClick(int type,AnniversariesItemVM item);
    }
}
