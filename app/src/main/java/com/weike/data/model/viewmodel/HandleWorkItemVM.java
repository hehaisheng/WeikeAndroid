package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

import com.weike.data.base.BaseVM;

/**
 * Created by LeoLu on 2018/6/15.
 */

public class HandleWorkItemVM extends BaseVM {
    public ObservableField<String> content = new ObservableField<>();

    public ObservableField<String> time = new ObservableField<>();

    public ObservableField<String> userName = new ObservableField<>();

    public void readMsg(){

    }

    public void deleteMsg(){

    }

    public void modifyMsg(){

    }
}
