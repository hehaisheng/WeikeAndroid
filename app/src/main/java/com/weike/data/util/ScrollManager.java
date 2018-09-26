package com.weike.data.util;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by pc- on 2018/6/10.
 */

public class ScrollManager {

    private static  ScrollManager listenerManager;
    public static  ScrollManager newInstance(){
        if(listenerManager==null){
            synchronized (ScrollManager.class){
                if(listenerManager==null){
                    listenerManager=new ScrollManager();
                }
            }
        }
        return  listenerManager;
    }
    public RecyclerView setScrollListener(final RecyclerView recyclerView, final Activity activity){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView1, int newState) {
                super.onScrollStateChanged(recyclerView1, newState);

                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView1.getLayoutManager();
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        //获取最后一个完全显示的ItemPosition
                        int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                        int item=manager.findLastVisibleItemPosition();
                        int totalItemCount=manager.getItemCount();
                        if(totalItemCount>0){
                            View view=recyclerView1.getChildAt(0);
                            if(lastVisibleItem==totalItemCount-1&&view.getHeight()*(item+1)+40>=recyclerView1.getHeight()){
                                scrollListener.scroll();
                            }
                        }


                    }

            }
        });

        return recyclerView;
    }

    private ScrollListener scrollListener;
    public void setListener(ScrollListener scrollListener){
        this.scrollListener=scrollListener;
    }
    public   interface  ScrollListener{
        void scroll();
    }
}
