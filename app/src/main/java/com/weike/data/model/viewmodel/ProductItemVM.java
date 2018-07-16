package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

import com.weike.data.R;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseVM;
import com.weike.data.business.log.RemindSettingActivity;
import com.weike.data.listener.OnReduceListener;
import com.weike.data.model.business.ToDo;

/**
 * Created by LeoLu on 2018/6/27.
 */

public class ProductItemVM extends BaseVM {

    public ObservableField<Boolean> isFirst = new ObservableField<>();

    public ObservableField<Boolean> isModify = new ObservableField<>();

    public ObservableField<Boolean> isShowContent = new ObservableField<>();

    public ObservableField<String> content = new ObservableField<>();

    public ObservableField<Boolean> isRemind = new ObservableField<>();

    public ObservableField<Integer> remindIcon = new ObservableField<>(R.mipmap.ic_mute);

    public String productId;

    public ToDo toDo = null;

    private BaseFragment fragment;

    public ProductItemVM(BaseFragment fragment) {
        this.fragment = fragment;
    }

    public OnProductListener listener;

    public interface OnProductListener{
        void onClick(ProductItemVM vm , int type);
    }

    public void goToDodo(){
        listener.onClick(this,3);

    }

    public void setListener(OnProductListener listener) {
        this.listener = listener;
    }

    public void onAdd(){
        listener.onClick(this,1);
    }

    public void onReduce(){
        listener.onClick(this,2);
    }
}
