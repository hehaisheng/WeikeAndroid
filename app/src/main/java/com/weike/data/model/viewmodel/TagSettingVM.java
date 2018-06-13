package com.weike.data.model.viewmodel;

import android.databinding.ObservableField;

import com.weike.data.base.BaseVM;

/**
 * 标签设置
 */
public class TagSettingVM extends BaseVM {
     public ObservableField<String> name = new ObservableField<>();

     public ObservableField<String> content = new ObservableField<>();

     public ObservableField<String> tagId = new ObservableField<>();

     public ObservableField<Boolean> isModify = new ObservableField<>();

     public TagSettingVM.OnReduceListener listener;

     public void setListener(TagSettingVM.OnReduceListener listener){
          this.listener = listener;
     }

     public interface OnReduceListener{
          void onReduce(TagSettingVM vm);
     }

     public void reduce(){
          listener.onReduce(this);
     }
}
