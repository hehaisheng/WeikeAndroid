package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseVM;

public class ExpandGroupVM  extends BaseVM{
    public String id;

    public ObservableField<String> name = new ObservableField<>();
}
