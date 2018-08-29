package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

import com.weike.data.base.BaseVM;

public class AnotherAttrItemVM extends BaseVM{

    public ObservableField<String> name = new ObservableField<>();

    public ObservableField<String> value = new ObservableField<>();

    public ObservableField<Boolean> isModify = new ObservableField<>();

    public  String id;

    public  String attributesValueId;

    public ObservableField<Boolean> lineShow = new ObservableField<>(true);



}
