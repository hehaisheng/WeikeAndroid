package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

import com.weike.data.base.BaseVM;

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
     *
     */
    public ObservableField<Boolean> isShowLine = new ObservableField<>(true);



    public void modify(ClientLogItemVM vm){

    }

    public void delete(ClientLogItemVM vm){

    }



}
