package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

import com.weike.data.base.BaseVM;

/**
 * Created by LeoLu on 2018/5/29.
 */

public class MainOptionVM extends BaseVM {

    public ObservableField<Integer> picId = new ObservableField<>();

    public ObservableField<String>  text = new ObservableField<>();



    public void jumpOptionDetail() {

    }
}
