package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.net.Uri;

import com.weike.data.base.BaseVM;

/**
 * Created by LeoLu on 2018/6/19.
 */

public class AddPhoneVM  extends BaseVM{
    public ObservableField<String> phoneNumber = new ObservableField<>();

    public ObservableField<Boolean> isModify = new ObservableField<>(false);

    public ObservableField<Boolean> isFirst = new ObservableField<>(false);

    public ObservableField<Boolean> isShowModify = new ObservableField<>(false);

    public OnPhoneClickListener listener;

    public AddPhoneVM(Activity activity) {
        this.activity = activity;
    }

    public void setListener(OnPhoneClickListener listener) {
        this.listener = listener;
    }

    public interface OnPhoneClickListener{
        int ADD = 1;
        int REDUCE = 2;


        void onPhoneItemClick(AddPhoneVM vm , int type);
    }

    public void add(){
        listener.onPhoneItemClick(this,OnPhoneClickListener.ADD);
    }

    public void reduce(){
        listener.onPhoneItemClick(this,OnPhoneClickListener.REDUCE);
    }

    public void hitUpPhone(){
        Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber.get()));
        activity.startActivity(dialIntent);

    }
}
