package com.weike.data.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LeoLu on 2018/5/21.
 */

public abstract class BaseFragment extends android.support.v4.app.Fragment {

    public boolean isModify;

    protected Context context;

    /**
     * 布局id
     * @return
     */
    protected abstract int setUpLayoutId();


    /**
     * 加载完毕
     * @param view
     */
    protected  abstract void loadFinish(View view);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setUpLayoutId(),null);

        loadFinish(view);

        return view;
    }




    public void onRightClick(boolean status){

    }

    public void onRightClick(){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }
}

