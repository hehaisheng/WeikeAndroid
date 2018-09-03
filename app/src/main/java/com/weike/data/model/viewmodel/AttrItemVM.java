package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

import org.w3c.dom.Attr;

/**
 * Created by LeoLu on 2018/6/7.
 */

public class AttrItemVM {
    public ObservableField<String> name = new ObservableField<>("");

    public ObservableField<String> id = new ObservableField<>("");

    public String  attrContent;


    public ObservableField<Boolean> isDisplayReduce = new ObservableField<>(false);

    public OnReduceListener listener;

    public void setListener(OnReduceListener listener) {
        this.listener = listener;
    }

    public void onReduce(){
        listener.onReduce(this);
    }

    public interface OnReduceListener{
        void onReduce(AttrItemVM vm);
    }
}
