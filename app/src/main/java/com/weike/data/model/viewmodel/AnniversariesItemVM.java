package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

import com.weike.data.base.BaseVM;
import com.weike.data.listener.OnReduceListener;

public class AnniversariesItemVM extends BaseVM {
    public ObservableField<Boolean> isFirst = new ObservableField<>();

    public ObservableField<Boolean> isModify = new ObservableField<>();

    public ObservableField<String> name = new ObservableField<>();

    public ObservableField<String> id = new ObservableField<>();

    public ObservableField<String> time = new ObservableField<>();


    public OnReduceListener<AnniversariesItemVM> listener;

    public void add(){
        listener.onAdd(this);
    }

    public void reduce(){
        listener.onReduce(this);
    }
}
