package com.weike.data.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * ${huneng} on 2018/7/13 14:24
 */

public class NoScrollLinearLayoutManager extends LinearLayoutManager {
    public boolean isScrollEnabled = true;

    public NoScrollLinearLayoutManager(Context context) {
        super(context);
    }

    public NoScrollLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public NoScrollLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {

        return isScrollEnabled && super.canScrollVertically();
    }
}
