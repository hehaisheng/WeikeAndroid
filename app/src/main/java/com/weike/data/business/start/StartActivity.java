package com.weike.data.business.start;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.business.home.HomeActivity;
import com.weike.data.business.login.LoginActivity;
import com.weike.data.util.ActivitySkipUtil;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SpUtil;

/**
 * Created by LeoLu on 2018/5/21.
 *
 * 启动页
 */
public class StartActivity extends BaseActivity {

    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        boolean isFirst = SpUtil.getInstance().getIsFirstOpen();

       handler.postDelayed(() -> {
            if (isFirst) {
                ActivitySkipUtil.skipAnotherAct(this, GuideActivity.class,true);
            } else {

                //如果本地的token都是空的 那么一次也没登录过 只是滑动过完了引导页

                LogUtil.d("acthome","token:" + SpUtil.getInstance().getCurrentToken());

                if (TextUtils.isEmpty(SpUtil.getInstance().getCurrentToken())) {
                    ActivitySkipUtil.skipAnotherAct(this, LoginActivity.class,true);
                } else {
                    ActivitySkipUtil.skipAnotherAct(this, HomeActivity.class,true);
                }

            }

        }, 2000);


    }

}
