package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

import com.weike.data.base.BaseVM;

/**
 * Created by LeoLu on 2018/6/11.
 */

public class LabelOptionItemVM extends BaseVM {

    public ObservableField<Boolean> isClick = new ObservableField<>();

    public ObservableField<String> text = new ObservableField<>();

    public ObservableField<String> id = new ObservableField<>();
}
