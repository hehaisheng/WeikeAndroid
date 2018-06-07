package com.weike.data.business.client;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Button;

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
 * Created by LeoLu on 2018/5/31.
 * 添加客户和修改客户信息都是这个Activity
 */
public class AddClientActivity extends BaseActivity {

    @BindView(R.id.sliding_tab_layout)
    public SlidingTabLayout tabLayout;

    @BindView(R.id.viewpager_activity_client_add)
    public NoScrollViewPager viewPager;

    public String[] titles = {"基本信息","服务信息","跟踪日志"};

    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    boolean isModify = false;

    public static void startActivity(String clientId){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_add);
        ButterKnife.bind(this);

        setCenterText("");
        setLeftText("编辑客户");
        setRightText("编辑");

        ClientBaseMsgFragment clientBaseMsgFragment = new ClientBaseMsgFragment();
        ClientServiceMsgFragment serviceMsgFragment = new ClientServiceMsgFragment();

        ClientServiceMsgFragment serviceMsgFragment2 = new ClientServiceMsgFragment();

        fragments.add(clientBaseMsgFragment);
        fragments.add(serviceMsgFragment);
        fragments.add(serviceMsgFragment2);


        FragmentBaseAdapter adapter = new FragmentBaseAdapter(getSupportFragmentManager(),fragments,titles);
        viewPager.setAdapter(adapter);

        tabLayout.setViewPager(viewPager);
    }

    @Override
    public void onRightClick() {
        super.onRightClick();

    }
}
