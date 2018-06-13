package com.weike.data.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weike.data.R;
import com.weike.data.model.business.TabEntity;
import com.weike.data.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhao on 16/11/28.
 */

public class BottomBarLayout extends LinearLayout implements View.OnClickListener{

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position,boolean isFresh);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }




    private int normalTextColor;
    private int selectTextColor;
    private int textSize = 12;

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }


    private LinearLayout mLinearLayout;
    private List<TabEntity> tabList = new ArrayList<>();

    private List<View> current = new ArrayList<>();

    public BottomBarLayout(Context context) {
        super(context);
        init(context);
    }

    public BottomBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context) {
        mLinearLayout = (LinearLayout) View.inflate(context,R.layout.widget_bottom_layout_container,null);
        addView(mLinearLayout);
    }

    public void setNormalTextColor(int color){
        this.normalTextColor = color;
    }

    public void setSelectTextColor(int color){
        this.selectTextColor = color;
    }

    public void setTabList(List<TabEntity> tabs){
        if(tabs == null || tabs.size() == 0){
            return;
        }
        this.tabList = tabs;
        for (int i=0;i<tabs.size();i++) {
            View itemView = View.inflate(getContext(), R.layout.widget_item_tab_layout, null);
            itemView.setId(i);
            itemView.setOnClickListener(this);
            TextView text = (TextView) itemView.findViewById(R.id.tv_title);

            ImageView icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            View redPoint = itemView.findViewById(R.id.red_point);
            TextView number = (TextView) itemView.findViewById(R.id.tv_count);
            TabEntity itemTab = tabList.get(i);
            text.setText(itemTab.getText());
            text.setTextColor(normalTextColor);
            text.setTextSize(textSize);
            icon.setImageResource(itemTab.getNormalIconId());
            redPoint.setVisibility(View.GONE);

            if(itemTab.getNewsCount() == 0){
                number.setVisibility(View.GONE);
            }else if(itemTab.getNewsCount()>99){
                number.setVisibility(View.VISIBLE);
                number.setText("99+");
            }else {
                number.setVisibility(View.VISIBLE);
                number.setText(String.format("%d",itemTab.getNewsCount()));
            }
            mLinearLayout.addView(itemView);
            current.add(itemView);

            if(i==0){
                showTab(0,itemView,true);
            }
        }
    }


    @Override
    public void onClick(View view) {
        if(mOnItemClickListener == null){
            return;
        }
        switch(view.getId()){
            case 0:
                mOnItemClickListener.onItemClick(0,true);
                showTab(0,view,true);
                break;
            case 1:
                mOnItemClickListener.onItemClick(1,true);
                showTab(1,view,true);
                break;
            case 2:
                mOnItemClickListener.onItemClick(2,true);
                showTab(2,view,true);
                break;
            case 3:
                mOnItemClickListener.onItemClick(3,true);
                showTab(3,view,true);
                break;
        }
    }

    public void showTab(int position,View view,boolean isAddNum){
        clearStatus(0,-1);
        TextView text = (TextView) view.findViewById(R.id.tv_title);
        text.setTextColor(selectTextColor);
        ImageView icon = (ImageView) view.findViewById(R.id.iv_icon);
        icon.setImageResource(tabList.get(position).getSelectIconId());

    }




    public void clearStatus(int count , int position) {
        for (int i=0;i<mLinearLayout.getChildCount();i++){
            View itemView = mLinearLayout.getChildAt(i);
            ImageView icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            TextView text = (TextView) itemView.findViewById(R.id.tv_title);
            TextView number = itemView.findViewById(R.id.tv_count);
            text.setTextColor(normalTextColor);
            icon.setImageResource(tabList.get(i).getNormalIconId());

            if (i == 2)
                number.setVisibility(View.VISIBLE);
            else
                number.setVisibility(View.GONE);

            if (position == -1) continue;
            if (i == 2 && count >= 99) {
                number.setText("99+");
            } else if (i == 2 && count <= 99) {
                number.setText(count + "");
            }

        }
    }
}
