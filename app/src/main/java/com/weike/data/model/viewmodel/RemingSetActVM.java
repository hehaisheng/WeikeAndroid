package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;
import android.widget.TimePicker;

import com.weike.data.base.BaseVM;

public class RemingSetActVM extends BaseVM {
    /**
     * 时间
     */
    public ObservableField<String> time = new ObservableField<>();
    /**
     * 内容
     */
    public ObservableField<String> content = new ObservableField<>();

    /**
     * 是否重复
     */
    public ObservableField<Boolean> IsRepeat = new ObservableField<>(false);

    /**
     * 是否重复文字
     */
    public ObservableField<String>  repeatText = new ObservableField<>();

    /**
     * 提醒时间
     */
    public ObservableField<String> remindTime = new ObservableField<>();

    /**
     * 是否提醒
     */
    public ObservableField<Boolean> isRemind = new ObservableField<>(true);


    public void init(){

    }


    public void repeatClick(){

    }

    /**
     * 时间Dialog
     */
    public void timeClick(){

    }

    /**
     *
     */
    public void remindClick() {

    }

    /**
     *
     */
    public void unRemindClick(){

    }
}
