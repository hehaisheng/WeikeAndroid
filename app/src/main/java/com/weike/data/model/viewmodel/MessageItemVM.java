package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;

import com.weike.data.base.BaseVM;
import com.weike.data.business.msg.MsgDetailActivity;

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
     * 消息iD
     */
    public String msgId;

    /**
     * 是否显示编辑
     */
    public ObservableField<Boolean> isSel = new ObservableField<>(false);

    /*
     *是否点击
     */
    public ObservableField<Boolean> isCheck = new ObservableField<>(false);

    public MessageItemVM(Activity activity) {
        super(activity);
    }

    /**
     * 这里是方便跳转如果是 选择 那么就是 编辑 否则就是 进入详情
     *
     */
    public void itemClick(){
        if (isSel.get()) {
            isCheck.set(isCheck.get() == true ? false : true);
        } else {
            MsgDetailActivity.startActivity(activity,title.get(),clientId);
        }


    }




}
