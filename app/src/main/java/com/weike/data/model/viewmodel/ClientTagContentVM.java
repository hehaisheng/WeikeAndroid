package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

import com.weike.data.base.BaseVM;

/**
 * Created by LeoLu on 2018/6/14.
 * 客户标签里面的客户内容
 */
public class ClientTagContentVM extends BaseVM {
    /**
     * 是否是选择
     */
    public ObservableField<Boolean> isSelector = new ObservableField<>(false);

    /**
     * 是否点击
     */
    public ObservableField<Boolean> isCheck = new ObservableField<>(false);

    /**
     * 标题
     */
    public ObservableField<String> title = new ObservableField<>();

    /**
     * 内容
     */
    public ObservableField<String> content = new ObservableField<>();

    /**
     * 图标url
     */
    public ObservableField<String> photoUrl = new ObservableField<>();

    /**
     * 是否有未读信息
     */
    public ObservableField<Boolean> isUnreadMsg = new ObservableField<>();

    /**
     * 用户id
     */
    public ObservableField<String> id = new ObservableField<>();
}
