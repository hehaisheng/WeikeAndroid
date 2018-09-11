package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

import com.weike.data.base.BaseVM;
import com.weike.data.listener.OnReduceListener;
import com.weike.data.model.business.ToDo;

/**
 * Created by LeoLu on 2018/6/27.
 */

public class ClientLogItemVM extends BaseVM {
    public ObservableField<String> content = new ObservableField<>();

    public ObservableField<String> time = new ObservableField<>();

    /**
     * 是否是编辑
     */
    public ObservableField<Boolean> isModify = new ObservableField<>();

    public ObservableField<Boolean> isRemind = new ObservableField<>();

    /**
     * 提醒
     */
    public ObservableField<Integer> remindIcon = new ObservableField<>();

    public String id;

    public ToDo toDo;

    /**
     *
     */
    public ObservableField<Boolean> isShowLine = new ObservableField<>(true);

    public OnReduceListener<ClientLogItemVM> listener;

    public void setListener(OnReduceListener<ClientLogItemVM> listener){
        this.listener = listener;
    }

    public void remind(){
        listener.onAdd(this);
    }

    public void delete(){
        listener.onReduce(this);
    }


    public void change(){
        onChangeListener.change(this);
    }


    public interface  OnChangeListener{
        void change(ClientLogItemVM clientLogItemVM);
    }

    public OnChangeListener onChangeListener;
    public void setOnChangeListener(OnChangeListener onChangeListener){
        this.onChangeListener=onChangeListener;

    }





}
