package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

/**
 * Created by LeoLu on 2018/5/31.
 */

public class MessageItemVM {

    /**
     * 消息页面的item
     */
    public ObservableField<String> title = new ObservableField<>();

    /**
     * 消息页面的内容
     */
    public ObservableField<String> content = new ObservableField<>();
    /**
     * 图标url
     */
    public ObservableField<String> iconUrl = new ObservableField<>();
    /**
     * 是否已读
     */
    public ObservableField<Boolean> isReadMsg = new ObservableField<>(false);

    /**
     * 是否选中
     */
    public ObservableField<Boolean> isSel = new ObservableField<>();



    public void jumpMsgDetail() {

    }
}
