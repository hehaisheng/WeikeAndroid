package com.weike.data;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.weike.data.payment.wechat.WXRegister;

import java.text.SimpleDateFormat;

/**
 * Created by LeoLu on 2018/5/21.
 */

public class WKBaseApplication extends Application {

    private static WKBaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        WXRegister.rg(this); //注册微信
        Logger.addLogAdapter(new AndroidLogAdapter());
        initSmartLayout();
    }

    private void initSmartLayout() {

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter

                return new BallPulseFooter(context).setAnimatingColor(context.getResources().getColor(R.color.color_41BCF6));

            }
        });


    }

    public static WKBaseApplication getInstance(){
        return instance;
    }
}
