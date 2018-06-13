package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;

import com.weike.data.base.BaseVM;
import com.weike.data.business.client.AddClientActivity;
import com.weike.data.databinding.ActivityAddLogBinding;

public class SearchItemVM extends BaseVM {

    /**
     * 是否是客户类型
     */
    public ObservableField<Boolean> isClient = new ObservableField<>();

    /**
     * 客户标题
     */
    public ObservableField<Boolean> isShowClientTag = new ObservableField<>();

    /**
     * 标题显示
     */
    public ObservableField<Boolean> isShowLogTag = new ObservableField<>();

    /**
     * 客户ID
     */
    public ObservableField<String> clientId = new ObservableField<>();

    /**
     * 日志ID
     */
    public ObservableField<String> logId = new ObservableField<>();

    public SearchItemVM(Activity activity){
        this.activity = activity;
    }

    public void jumpActivity(){
        if (isClient.get()) {
            AddClientActivity.startActivity(activity,clientId.get());
        } else {

        }
    }
}
