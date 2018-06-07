package com.weike.data.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.weike.data.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by LeoLu on 2018/6/7.
 */

public class FragmentBaseAdapter extends FragmentStatePagerAdapter {
    private ArrayList<BaseFragment> fragments;
    private String[] tabTitle;

    public FragmentBaseAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments, String[] tabTitle) {
        super(fm);
        this.fragments = fragments;
        this.tabTitle = tabTitle;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabTitle[position];


    }
}
