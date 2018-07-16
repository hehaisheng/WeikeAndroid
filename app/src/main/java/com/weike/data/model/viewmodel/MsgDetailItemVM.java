package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;
import android.support.v4.app.FragmentActivity;

import com.weike.data.R;
import com.weike.data.base.BaseVM;
import com.weike.data.listener.OnReduceListener;

/**
 * Created by LeoLu on 2018/6/1.
 */

public class MsgDetailItemVM extends BaseVM {

    public ObservableField<String> content = new ObservableField<>();

    public ObservableField<String> title = new ObservableField<>();

    public String id;

    public int remindType;

    public int handleType;


    public ObservableField<String> rightText = new ObservableField<>("稍后提醒");

    public ObservableField<String> time = new ObservableField<>();

    public ObservableField<Boolean> isTextRemind = new ObservableField<>(true);


    /**
     * 是否是已读
     */
    public ObservableField<Boolean> isRead = new ObservableField<>();

    /**
     * 是否选中
     */
    public ObservableField<Boolean> isSle = new ObservableField<>();

    public ObservableField<Boolean> isCheck = new ObservableField<>();

    public ObservableField<Boolean> isSystemMsg = new ObservableField<>(false);

    public OnReduceListener<MsgDetailItemVM> listener;

    public void setListener(OnReduceListener<MsgDetailItemVM> listener) {
        this.listener = listener;
    }

    public void rightClick(){
        listener.onReduce(this);
    }

    public MsgDetailItemVM(Activity activity) {
        super(activity);
    }
}
