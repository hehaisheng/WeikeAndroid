package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;

import com.weike.data.base.BaseVM;
import com.weike.data.business.client.AddClientActivity;


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

    /**
     *
     */
    public ObservableField<String> iconUrl = new ObservableField<>();


    /**
     * 内容
     */
    public ObservableField<String> content = new ObservableField<>();

    /**
     * 标题
     */
    public ObservableField<String> title = new ObservableField<>();

    /**
     * 标签名
     */
    public ObservableField<String> tagName = new ObservableField<>();


    public SearchItemVM(Activity activity){
        this.activity = activity;
    }

    public void jumpActivity(){
        AddClientActivity.startActivity(activity,clientId.get());
    }
}
