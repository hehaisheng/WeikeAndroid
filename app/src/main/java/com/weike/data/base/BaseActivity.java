package com.weike.data.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weike.data.R;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeoLu on 2018/5/21.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    public boolean isModify = false;

    public void setModify(boolean isModify) {
        this.isModify = isModify;
    }

    protected TextView leftText;

    protected TextView center;

    protected TextView rightBtn;

    protected View allLayout;

    protected View back;

    public static final String ACTION_OUT_OF_LOGIN = "com.outofLogin.ACTION_OUT_OF_LOGIN";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        registerReceiver(outOfLoginBr,new IntentFilter(ACTION_OUT_OF_LOGIN));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(outOfLoginBr);
    }

    private BroadcastReceiver outOfLoginBr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SpUtil.getInstance().saveCurrentToken("");
            finish();
        }
    };

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        leftText = findViewById(R.id.tv_widget_actionbar_left);
        center =  findViewById(R.id.tv_widget_actionbar_center);
        rightBtn = findViewById(R.id.tv_actionbar_right);
        back = findViewById(R.id.iv_widget_actionbar_back);
        allLayout = findViewById(R.id.include_acionbar);

        if (back !=null) {
            back.setOnClickListener(this);
            rightBtn.setOnClickListener(this);
        }
    }

    public void hideAll(boolean isShow){
        if (allLayout != null)
            allLayout.setVisibility(isShow ?View.VISIBLE : View.GONE);
    }

    public void setRightText(String right) {
        rightBtn.setText(right);
    }

    public void setLeftText(String text ) {
        leftText.setText(text);
    }

    public void setCenterText(String text){
        center.setText(text);
    }

    public void isShowBack(boolean isShow){
        back.setVisibility( isShow ? View.VISIBLE :View.GONE );
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);

    }


    public void onRightClick(){
        isModify = isModify ? false : true;
        setRightText(isModify ? "完成":"编辑");
        onRightClick(isModify);
    }

    public void onRightClick(boolean isModify) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_actionbar_right) {
            onRightClick();
        } else if (view.getId() == R.id.iv_widget_actionbar_back ) {
            finish();
        }
    }
}
