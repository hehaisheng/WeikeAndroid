package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;
import android.support.v4.app.FragmentActivity;

import com.weike.data.R;
import com.weike.data.base.BaseVM;

/**
 * Created by LeoLu on 2018/6/1.
 */

public class MsgDetailItemVM extends BaseVM {

    public ObservableField<String> content = new ObservableField<>();

    public ObservableField<String> title = new ObservableField<>();

    public ObservableField<Integer> leftTextColor = new ObservableField<>(R.color.color_bebebe);

    public ObservableField<Integer> rightTextColor = new ObservableField<>(R.color.color_bebebe);

    public ObservableField<String> leftText = new ObservableField<>();

    public ObservableField<String> rightText = new ObservableField<>();

    public ObservableField<String> time = new ObservableField<>();


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



    public void leftClick(){

    }

    public void rightClick(){

    }

    public MsgDetailItemVM(Activity activity) {
        super(activity);
    }
}
