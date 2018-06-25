package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;
import android.support.v4.app.FragmentActivity;

import com.weike.data.base.BaseVM;

/**
 * Created by LeoLu on 2018/6/1.
 */

public class MsgDetailItemVM extends BaseVM {

    public ObservableField<String> content = new ObservableField<>();

    public ObservableField<String> title = new ObservableField<>();

    public ObservableField<String> leftTextColor = new ObservableField<>();

    public ObservableField<Boolean> isSystemMsg = new ObservableField<>(false);



    public MsgDetailItemVM(Activity activity) {
        super(activity);
    }
}
