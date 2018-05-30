package com.weike.data.business.home;

import android.databinding.DataBindingUtil;
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

    private int[] normalIcon = {


    };
    private int[] selectorIcon = {};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        BaseFragment fragment = new HomeFragment();

        getFragmentManager().beginTransaction().replace(R.id.framgnet_home,fragment).commit();

    }


}
