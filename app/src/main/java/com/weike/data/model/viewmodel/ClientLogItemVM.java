package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

import com.weike.data.base.BaseVM;

/**
 * Created by LeoLu on 2018/6/27.
 */

public class ClientLogItemVM extends BaseVM {
    public ObservableField<String> content = new ObservableField<>();

    public ObservableField<String> time = new ObservableField<>();

}
