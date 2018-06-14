package com.weike.data.business.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.weike.data.R;
import com.weike.data.adapter.ViewPagerGuideAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.business.login.LoginActivity;
import com.weike.data.util.ActivitySkipUtil;
import com.weike.data.util.SpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeoLu on 2018/5/26.
 *
 * 引导页
 */
public class GuideActivity extends BaseActivity {

    private List<View>  viewData = new ArrayList<>();

    private static final int[] ids = {
            R.mipmap.bg_opening_1,R.mipmap.bg_opening_2,R.mipmap.bg_opening_3
    };

    private ViewPagerGuideAdapter guideAdapter;

    @BindView(R.id.vp_guide_core)
    public ViewPager viewPager;

    @BindView(R.id.btn_goto_next)
    public Button next;

    @OnClick(R.id.btn_goto_next)
    public void onClick(View view) {
        ActivitySkipUtil.skipAnotherAct(this,LoginActivity.class,true);
        SpUtil.getInstance().saveIsFirstOpen();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);



        for(int i = 0 ; i < ids.length ; i ++) {
            View view = LayoutInflater.from(this).inflate(R .layout.widget_guide_fragment,null);
            ImageView bg = view.findViewById(R.id.iv_guide_pic);
            bg.setImageResource(ids[i]);
            viewData.add(view);
        }

        guideAdapter = new ViewPagerGuideAdapter(viewData);
        viewPager.setAdapter(guideAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == ids.length - 1) {
                    next.setVisibility(View.VISIBLE);
                } else {
                    next.setVisibility(View.GONE);
                }
            }
        });

    }
}
