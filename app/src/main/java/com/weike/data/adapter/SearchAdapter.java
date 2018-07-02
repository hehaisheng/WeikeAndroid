package com.weike.data.adapter;

import android.content.Context;
import android.graphics.Color;

import com.weike.data.databinding.WidgetSearchListItemBinding;
import com.weike.data.model.viewmodel.SearchItemVM;
import com.weike.data.util.KeyWordUtil;
import com.weike.data.util.LogUtil;

import java.util.List;

/**
 * Created by LeoLu on 2018/6/21.
 */

public class SearchAdapter extends BaseDataBindingAdapter {
    private String keyword;

    public SearchAdapter(Context context, int layoutId, List list, int resId) {
        super(context, layoutId, list, resId);
    }


    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        super.onBindViewHolder(holder, position);


        WidgetSearchListItemBinding  binding = (WidgetSearchListItemBinding) holder.getBinding();
        SearchItemVM serviceMsgVM =  binding.getSearchItemVM();

        binding.tvContent.setText(KeyWordUtil.setKeyWordColor(serviceMsgVM.content.get(),keyword));
        binding.tvName.setText(KeyWordUtil.setKeyWordColor(serviceMsgVM.title.get(),keyword));


    }

}
