package com.weike.data.business.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;


import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseFragment;
import com.weike.data.business.client.ClientFragment;
import com.weike.data.business.msg.MsgFragment;
import com.weike.data.business.myself.MySelfFragment;
import com.weike.data.model.business.TabEntity;
import com.weike.data.view.BottomBarLayout;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LeoLu on 2018/5/21.
 */

public class HomeActivity extends BaseActivity {

    @BindView(R.id.bottom_bar)
    public BottomBarLayout bottomBarLayout;



    private List<TabEntity> tabEntities = new ArrayList<>();

    private int normalTextColor = Color.parseColor("#333333");
    private int selectTextColor = Color.parseColor("#009EE5");

    private String[] tabText = {"首页","客户","消息","我的"};

    private int[] normalIcon = {
        R.mipmap.ic_home_nor,R.mipmap.ic_add_client_nor,
            R.mipmap.ic_message_nor,R.mipmap.ic_home_me_nor

    };
    private int[] selectorIcon = {
            R.mipmap.ic_home_sel,R.mipmap.ic_add_client_sel,
            R.mipmap.ic_message_sel,R.mipmap.ic_home_me_sel

    };

    private void initBottomLayout(){
        for (int i=0;i<tabText.length;i++){
            TabEntity item = new TabEntity();
            item.setText(tabText[i]);
            item.setNormalIconId(normalIcon[i]);
            item.setSelectIconId(selectorIcon[i]);

            if(i==0 || i==1){
                item.setShowPoint(true);
            }else {
               item.setNewsCount(0);
            }
            tabEntities.add(item);
        }

        bottomBarLayout.setNormalTextColor(normalTextColor);
        bottomBarLayout.setSelectTextColor(selectTextColor);
        bottomBarLayout.setTabList(tabEntities);
        bottomBarLayout.setOnItemClickListener(new BottomBarLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                replaceFragment(position);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void replaceFragment(int position) {
        BaseFragment fragment;
        if (position == 0) {
            fragment = new HomeFragment();
            setCenterText("首页");
        } else if(position == 1) {
            fragment = new ClientFragment();
            setCenterText("客户");
        } else if (position == 2) {
            fragment = new MsgFragment();
            setCenterText("消息");
        } else {
            setCenterText("个人中心");
            fragment = new MySelfFragment();
        }

        if (position == 2) {
            setRightText("编辑");
        } else {
            setRightText("");
        }

        if (position == 3) {
            hideAll(false);
        } else {
            hideAll(true);
        }

        getFragmentManager().beginTransaction().replace(R.id.framgnet_home,fragment).commit();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);




        initBottomLayout();
        BaseFragment fragment = new HomeFragment();
        isShowBack(false);
        setRightText("");
        getFragmentManager().beginTransaction().replace(R.id.framgnet_home,fragment).commit();





    }


}
