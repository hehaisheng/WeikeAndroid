package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;

import com.weike.data.base.BaseVM;
import com.weike.data.util.ActivitySkipUtil;

import java.util.HashMap;

/**
 * Created by LeoLu on 2018/5/31.
 */

public class MessageItemVM extends BaseVM
{

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
     * 每一条的ClientId
     */
    public String clientId;

    /**
     * 是否选中
     */
    public ObservableField<Boolean> isSel = new ObservableField<>();


    public MessageItemVM(Activity activity) {
        super(activity);
    }


    public void jumpMsgDetail() {
        HashMap<String , String > map  = new HashMap<>();

    }
}
