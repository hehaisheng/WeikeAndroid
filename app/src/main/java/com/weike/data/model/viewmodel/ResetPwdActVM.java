package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

import com.weike.data.R;
import com.weike.data.base.BaseVM;

public class ResetPwdActVM extends BaseVM {
    public ObservableField<Integer> pwdImageId = new ObservableField<>(R.mipmap.icon_hide);

    public ObservableField<Integer> pwd2ImageId = new ObservableField<>(R.mipmap.icon_hide);

    public ObservableField<String> vCode = new ObservableField<>();

    public ObservableField<String> pwd = new ObservableField<>();

    public ObservableField<String> pwd2 = new ObservableField<>();

    public void submit(){

    }
}
