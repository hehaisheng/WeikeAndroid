package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

import com.weike.data.R;
import com.weike.data.base.BaseVM;

/**
 * Created by LeoLu on 2018/6/15.
 */

public class HandleWorkItemVM extends BaseVM {
    public int position;

    public ObservableField<String> content = new ObservableField<>();

    public ObservableField<String> time = new ObservableField<>();

    public ObservableField<String> userName = new ObservableField<>();

    public ObservableField<Integer> readClickBg = new ObservableField<>(R.mipmap.ic_right_yellow);

    /**
     * todo Id
     */
    public ObservableField<String> id = new ObservableField<>();

    public void readMsg(){
        listener.onClick(OnHandleWorkClickListener.TYPE_OF_CHECK,this);
    }

    public void deleteMsg(){
        listener.onClick(OnHandleWorkClickListener.TYPE_OF_MODIFY,this);
    }

    public void modifyMsg(){
        listener.onClick(OnHandleWorkClickListener.TYPE_OF_DELETE,this);
    }

    public OnHandleWorkClickListener listener;
    public void setListener(OnHandleWorkClickListener listener){
        this.listener = listener;
    }

    public interface OnHandleWorkClickListener<T>{
        int TYPE_OF_CHECK = 1;
        int TYPE_OF_MODIFY = 2;
        int TYPE_OF_DELETE = 3;

        void onClick(int type  ,T t);
    }
}
