package com.weike.data;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.baidu.mobstat.StatService;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.Bugly;
import com.weike.data.payment.wechat.WXRegister;

import java.text.SimpleDateFormat;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by LeoLu on 2018/5/21.
 */

public class WKBaseApplication extends Application {

    private static WKBaseApplication instance;
    //clientFragment是否要显示提示
    public boolean isShow=false;

    public boolean  hasNewLabel=false;
    public boolean  hasNoClientId=false;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        WXRegister.rg(this); //注册微信
        Logger.addLogAdapter(new AndroidLogAdapter());
        JPushInterface.init(this);//初始化jPush
        JPushInterface.setDebugMode(true);
        StatService.start(this);
        Bugly.init(getApplicationContext(), "7806577de6", false);
        initSmartLayout();
    }

    private void initSmartLayout() {

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.color_41BCF6, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context).setTimeFormat(new SimpleDateFormat("yyyy-MM-dd"));//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });


    }

    public static WKBaseApplication getInstance(){
        return instance;
    }
}
