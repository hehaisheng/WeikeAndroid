package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

import com.weike.data.R;
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

    public ObservableField<String> content = new ObservableField<>();

    public ObservableField<Boolean> isRemind = new ObservableField<>();

    public ObservableField<Integer> remindIcon = new ObservableField<>(R.mipmap.ic_mute);

    public String productId;

    public ToDo toDo = new ToDo();


    public OnReduceListener<ProductItemVM> listener;

    public void goToDodo(){

    }

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
