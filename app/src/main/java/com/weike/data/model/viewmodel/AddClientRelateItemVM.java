package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

/**
 * Created by LeoLu on 2018/6/19.
 *
 * 用于新增客户时候的添加客户
 */
public class AddClientRelateItemVM {

    public ObservableField<Boolean> isModify = new ObservableField<>();

    /**
     * 保留一条默认的
     */
    public ObservableField<Boolean> isFirst = new ObservableField<>();

    public ObservableField<String> clientName = new ObservableField<>();

    public String clientId;

    public interface AddClientRelateItemListener{
        int ADD_ITEM  = 1;
        int ADD_RELATE = 2;
        int REDUCE =3;
        void onRelateItemClick(AddClientRelateItemVM vm , int type);
    }

    private AddClientRelateItemListener listener;

    public void setAddClientRelateItemListener(AddClientRelateItemListener listener) {
        this.listener = listener;
    }


    public void reduce() {
        listener.onRelateItemClick(this,AddClientRelateItemListener.REDUCE);
    }

    public void addItem(){
        listener.onRelateItemClick(this,AddClientRelateItemListener.ADD_ITEM);
    }

    public void addRelate(){
        listener.onRelateItemClick(this,AddClientRelateItemListener.ADD_RELATE);
    }
}
