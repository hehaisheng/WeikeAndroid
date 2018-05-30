package com.weike.data.business.home;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;


import com.google.gson.reflect.TypeToken;
import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.TabEntity;
import com.weike.data.model.req.MainPageDataReq;
import com.weike.data.model.resp.MainPageDataResp;
import com.weike.data.model.viewmodel.MainOptionVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.BannerImageLoader;
import com.weike.data.util.SpUtil;
import com.weike.data.util.TransformerUtils;
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



    List<MainOptionVM> vms = new ArrayList<>();

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

            }
        });

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);




        initBottomLayout();
        BaseFragment fragment = new HomeFragment();

        getFragmentManager().beginTransaction().replace(R.id.framgnet_home,fragment).commit();




    }


}
