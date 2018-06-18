package com.weike.data.model.viewmodel;

import com.weike.data.base.BaseVM;
import com.weike.data.listener.OnReduceListener;

public class RelateCLientItemVM extends BaseVM {
    public String name;

    public String id;

    public OnReduceListener<RelateCLientItemVM> lientItemVMOnReduceListener;

    public void setLientItemVMOnReduceListener(OnReduceListener<RelateCLientItemVM> lientItemVMOnReduceListener) {
        this.lientItemVMOnReduceListener = lientItemVMOnReduceListener;
    }

    public void onReduce(){
        lientItemVMOnReduceListener.onReduce(this);
    }
}
