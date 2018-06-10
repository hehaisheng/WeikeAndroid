package com.weike.data.business.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.weike.data.R;
import com.weike.data.adapter.FragmentBaseAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.model.req.AddClientReq;
import com.weike.data.model.resp.AddClientResp;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;
import com.weike.data.view.CircleImageView;
import com.weike.data.view.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeoLu on 2018/5/31.
 * 添加客户和修改客户信息都是这个Activity
 */
public class AddClientActivity extends BaseActivity {

    @BindView(R.id.sliding_tab_layout)
    public SlidingTabLayout tabLayout;

    @BindView(R.id.viewpager_activity_client_add)
    public NoScrollViewPager viewPager;

    @BindView(R.id.iv_add_client_usericon)
    public CircleImageView userIcon;

    @OnClick(R.id.iv_add_client_usericon)
    public void updateUserIcon(View view){

    }

    public void labelClick(View view){

    }

    @BindView(R.id.tv_label_add)
    public TextView userLabel;



    private int position;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public String[] titles = {"基本信息","服务信息","跟踪日志"};

    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    boolean isModify = false;

    public static final String TAG_CLIENT_ID = "com.weike.data.TAG_CLIENT_ID";

    public static void startActivity(Activity activity,String clientId){
        Intent intent = new Intent(activity,AddClientActivity.class);
        intent.putExtra(TAG_CLIENT_ID,clientId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_add);
        ButterKnife.bind(this);

        setCenterText("");
        setLeftText("编辑客户");
        setRightText("编辑");

        getClientDetail();

        addFragment();

        //testAdd();
    }

    private void getClientDetail(){
        String id = getIntent().getStringExtra(TAG_CLIENT_ID); //如果为空 那么就是坑爹的空项
        if (TextUtils.isEmpty(id))return;


    }

    private void testAdd(){
        AddClientReq req = new AddClientReq();
        req.OnePhoneNumber = "15692022243";
        req.userName = "陆尼玛";
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.ADD_CLIENT)
        .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<AddClientResp>>(){

        })).subscribe(new BaseObserver<BaseResp<AddClientResp>>() {
            @Override
            protected void onSuccess(BaseResp<AddClientResp> addClientRespBaseResp) throws Exception {

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                LogUtil.d("acthome",e.getMessage());
            }
        });


    }



    private void addFragment() {
        viewPager.setNoScroll(true);
        ClientBaseMsgFragment clientBaseMsgFragment = new ClientBaseMsgFragment();
        ClientServiceMsgFragment serviceMsgFragment = new ClientServiceMsgFragment();

        ClientServiceMsgFragment serviceMsgFragment2 = new ClientServiceMsgFragment();

        fragments.add(clientBaseMsgFragment);
        fragments.add(serviceMsgFragment);
        fragments.add(serviceMsgFragment2);


        FragmentBaseAdapter adapter = new FragmentBaseAdapter(getSupportFragmentManager(),fragments,titles);
        viewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                AddClientActivity.this.position = position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        tabLayout.setViewPager(viewPager);
    }

    @Override
    public void onRightClick() {
        super.onRightClick();

    }
}
