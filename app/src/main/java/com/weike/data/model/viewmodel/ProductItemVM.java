package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

import com.weike.data.base.BaseVM;
import com.weike.data.listener.OnReduceListener;
import com.weike.data.model.business.ToDo;

/**
 * Created by LeoLu on 2018/6/27.
 */

public class ProductItemVM extends BaseVM {

    public ObservableField<Boolean> isFirst = new ObservableField<>();

    public ObservableField<Boolean> isModify = new ObservableField<>();

    public ObservableField<Boolean> isShowContent = new ObservableField<>();

    public ObservableField<String> product = new ObservableField<>();

    public ObservableField<Boolean> isRemind = new ObservableField<>();

    public ToDo toDo;

    public ObservableField<Integer> remindIcon = new ObservableField<>();

    public OnReduceListener<ProductItemVM> listener;

    public void setListener(OnReduceListener<ProductItemVM> listener) {
        this.listener = listener;
    }

    public void onAdd(){
        listener.onAdd(this);
    }

    public void onReduce(){
        listener.onReduce(this);
    }
}
