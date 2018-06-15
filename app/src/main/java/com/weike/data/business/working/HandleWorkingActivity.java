package com.weike.data.business.working;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.flyco.tablayout.SlidingTabLayout;
import com.weike.data.R;
import com.weike.data.adapter.FragmentBaseAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseFragment;
import com.weike.data.view.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 代办事处
 */
public class HandleWorkingActivity extends BaseActivity {


    String[] tagTitle = {"待办事项","已办事项","过期事项"};

    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    @BindView(R.id.sliding_tab_layout)
    public SlidingTabLayout slidingTabLayout;

    @BindView(R.id.viewpager_activity_handle_working)
    public NoScrollViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_working);
        setCenterText("");
        setLeftText("待办事项");
        setRightText("");
        ButterKnife.bind(this);

        HandlerWorkingFragment handlerWorkingFragment = new HandlerWorkingFragment();

        AlreadyHandledFragment alreadyHandledFragment = new AlreadyHandledFragment();

        ExpireWorkingFragment expireWorkingFragment = new ExpireWorkingFragment();



        fragments.add(handlerWorkingFragment);
        fragments.add(alreadyHandledFragment);
        fragments.add(expireWorkingFragment);


        viewPager.setAdapter(new FragmentBaseAdapter(getSupportFragmentManager(),fragments,tagTitle));
        slidingTabLayout.setViewPager(viewPager);
    }
}
