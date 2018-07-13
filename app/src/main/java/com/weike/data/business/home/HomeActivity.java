package com.weike.data.business.home;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseFragment;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.business.client.ClientFragment;
import com.weike.data.business.msg.MsgFragment;
import com.weike.data.business.myself.MySelfFragment;
import com.weike.data.config.Config;
import com.weike.data.model.business.AnotherAttributes;
import com.weike.data.model.business.TabEntity;
import com.weike.data.model.business.User;
import com.weike.data.model.req.GetAttrListReq;
import com.weike.data.model.req.GetClientListReq;
import com.weike.data.model.req.GetClientTagListReq;
import com.weike.data.model.req.MainPageDataReq;
import com.weike.data.model.resp.GetAttrListResp;
import com.weike.data.model.resp.GetClientListResp;
import com.weike.data.model.resp.GetClientTagListResp;
import com.weike.data.model.resp.MainPageDataResp;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.ClientTagComparator;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.TransformerUtils;
import com.weike.data.view.BottomBarLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeoLu on 2018/5/21.
 */

public class HomeActivity extends BaseActivity {

    @BindView(R.id.bottom_bar)
    public BottomBarLayout bottomBarLayout;

    @BindView(R.id.view_pager)
    public ViewPager viewPager;

    @BindView(R.id.btn_delete)
    public Button btn_deleteMsg;

    /**
     * 删除
     * @param view
     */
    @OnClick(R.id.btn_delete)
    public void deleteMsgClick(View view){
        ((MsgFragment) currentFragment).onBottomClick(); //响应Fragment的点击
    }


    private android.support.v4.app.FragmentManager fm;
    private android.support.v4.app.FragmentTransaction ft;



    public BaseFragment currentFragment,homeFragment ,clientFragment, msgFragment , mySelfFragment ;

    /**
     * 当前的Fragment位置
     */
    private int currentPosition;
    /**
     * 是否是选择
     */
    private boolean isSle = false;

    private List<BaseFragment> fragmentList = new ArrayList<>();

    /**
     * 底部栏的tag
     */
    private List<TabEntity> tabEntities = new ArrayList<>();

    private int normalTextColor = Color.parseColor("#333333");
    private int selectTextColor = Color.parseColor("#009EE5");

    private String[] tabText = {"首页", "客户", "消息", "我的"};

    private int[] normalIcon = {
            R.mipmap.ic_home_nor, R.mipmap.ic_add_client_nor,
            R.mipmap.ic_message_nor, R.mipmap.ic_home_me_nor

    };
    private int[] selectorIcon = {
            R.mipmap.ic_home_sel, R.mipmap.ic_add_client_sel,
            R.mipmap.ic_message_sel, R.mipmap.ic_home_me_sel

    };

    private void initBottomLayout() {
        for (int i = 0; i < tabText.length; i++) {
            TabEntity item = new TabEntity();
            item.setText(tabText[i]);
            item.setNormalIconId(normalIcon[i]);
            item.setSelectIconId(selectorIcon[i]);

            item.setNewsCount(0);
            item.setShowPoint(true);
            tabEntities.add(item);
        }

        bottomBarLayout.setNormalTextColor(normalTextColor);
        bottomBarLayout.setSelectTextColor(selectTextColor);
        bottomBarLayout.setTabList(tabEntities);
        bottomBarLayout.setOnItemClickListener(new BottomBarLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int position,boolean isAdd) {
                replaceFragment(position);
                currentPosition = position;

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void replaceFragment(int position) {
        if (position == 0) {
            //switchFragment(homeFragment);
            currentFragment = homeFragment;
            viewPager.setCurrentItem(0);
            setCenterText("维客助手");
        } else if (position == 1) {
            //switchFragment(clientFragment);
            currentFragment = clientFragment;
            setCenterText("客户");
        } else if (position == 2) {

            currentFragment = msgFragment;
            setCenterText("消息");
        } else {
            //switchFragment(mySelfFragment);
            currentFragment = mySelfFragment;
            setCenterText("个人中心");
        }
        viewPager.setCurrentItem(position);

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



    }




    @Override
    public void onRightClick() {
        super.onRightClick();

        //currentFragment.onRightClick();

        if (currentPosition == 2) { //如果是消息页面
            isSle = isSle == true ? false:true;
            setRightText(isSle ? "保存":"编辑");
            if (isSle) {
                btn_deleteMsg.setVisibility(View.VISIBLE);
            } else {
                btn_deleteMsg.setVisibility(View.GONE);
            }
        } else {
            setRightText("");
            isSle = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setCenterText("维客助手");
        initBottomLayout();

        isShowBack(false);
        setRightText("");


        getClientList();

        initLabel();

        new Handler().postAtTime(new Runnable() {
            @Override
            public void run() {
                getUnreadMsg();
            }
        },3000);

        initFragment();
    }

    private void initFragment(){
        homeFragment = new HomeFragment();
        clientFragment = new ClientFragment();
         msgFragment = new MsgFragment();
         mySelfFragment = new MySelfFragment();

         fragmentList.add(homeFragment);
         fragmentList.add(clientFragment);
         fragmentList.add(msgFragment);
         fragmentList.add(mySelfFragment);
         viewPager.setOffscreenPageLimit(4);

         viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
             @Override
             public Fragment getItem(int position) {
                 return fragmentList.get(position);
             }

             @Override
             public int getCount() {
                 return fragmentList.size();
             }
         });

         viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
             @Override
             public void onPageSelected(int position) {
                 super.onPageSelected(position);
                bottomBarLayout.checkCurrent(position);

                 if (position == 0) {
                     setCenterText("维客助手");
                 } else if (position == 1) {
                     setCenterText("客户");
                 } else if (position == 2) {
                     setCenterText("消息");
                 } else if (position == 3){
                     setCenterText("个人中心");
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
             }
         });






    }





    /**
     * 进来获取客户列表 用于缓存使用
     */
    public void getClientList(){
        GetClientListReq req = new GetClientListReq();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req,Config.GET_CLIENT_LIST)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetClientListResp>>(){

                }))
                .subscribe(new BaseObserver<BaseResp<GetClientListResp>>(){

                    @Override
                    protected void onSuccess(BaseResp<GetClientListResp> getClientListRespBaseResp) throws Exception {
                        List<GetClientListResp.ClientListSub> clientListSubs = getClientListRespBaseResp.getDatas().allClientList;
                        User user = SpUtil.getInstance().getUser();
                        user.clietList = clientListSubs;
                        SpUtil.getInstance().saveNewsUser(user);


                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

                    }
                });
    }

    private void initLabel(){

        GetClientTagListReq req = new GetClientTagListReq();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_CLIENT_TAG_LIST)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetClientTagListResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetClientTagListResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetClientTagListResp> getClientTagListRespBaseResp) throws Exception {
                List<GetClientTagListResp.TagSub> list = getClientTagListRespBaseResp.getDatas().clientLabelList;
                Collections.sort(list,new ClientTagComparator());
                User user = SpUtil.getInstance().getUser();
                user.labelList = list;


                GetClientTagListResp.TagSub zero = new GetClientTagListResp.TagSub();
                zero.labelName = "未分组客户";

                zero.id = "";
                user.labelList.add(0,zero);

                SpUtil.getInstance().saveNewsUser(user);

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    public void initAnotherType(){
        GetAttrListReq req = new GetAttrListReq();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);


        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_ATTR_LIST)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetAttrListResp>>(){

                })).subscribe(new BaseObserver<BaseResp<GetAttrListResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetAttrListResp> getAttrListRespBaseResp) throws Exception {

                User user = SpUtil.getInstance().getUser();
                List<AnotherAttributes> list = user.anotherAttributes;
                list.clear();

                for(int i = 0 ; i < getAttrListRespBaseResp.getDatas().attributesValueList.size();i++) {
                    AnotherAttributes atr = new AnotherAttributes();
                    atr.attributesName = getAttrListRespBaseResp.getDatas().attributesValueList.get(i).attributesName;
                    atr.attributesId = getAttrListRespBaseResp.getDatas().attributesValueList.get(i).id;
                    list.add(atr);
                }
                SpUtil.getInstance().saveNewsUser(user);

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    private void getUnreadMsg() {


        MainPageDataReq req = new MainPageDataReq();
        req.token = SpUtil.getInstance().getCurrentToken();
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.MAIN_PAGE_DATA)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<MainPageDataResp>>() {

                })).subscribe(new BaseObserver<BaseResp<MainPageDataResp>>() {

            @Override
            protected void onSuccess(BaseResp<MainPageDataResp> mainPageData) throws Exception {
                if(mainPageData.getDatas().count ==0) return;
                bottomBarLayout.clearStatus(mainPageData.getDatas().count,2);

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });

    }

}
