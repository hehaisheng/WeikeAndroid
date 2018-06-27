package com.weike.data.adapter;

import android.content.Context;

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

       /* WidgetNearbyshopHotsearchKeywordItemBinding binding = (WidgetNearbyshopHotsearchKeywordItemBinding) holder.getBinding();
        NearByShopHotSearchHistoryItemVM serviceMsgVM =  binding.getHotSearchItemVM();
        binding.tvHistoryItem.setText(KeyWordUtil.setKeyWordColor(serviceMsgVM.name.get(),keyword));*/


    }

}
