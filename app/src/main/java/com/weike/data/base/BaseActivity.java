package com.weike.data.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.weike.data.R;
import com.weike.data.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeoLu on 2018/5/21.
 */

public class BaseActivity extends Activity implements View.OnClickListener {


    protected TextView leftText;

    protected TextView center;

    protected TextView rightBtn;

    protected ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ImageView back = findViewById(R.id.iv_widget_actionbar_back);
        leftText = findViewById(R.id.tv_widget_actionbar_left);
        center =  findViewById(R.id.tv_widget_actionbar_center);
        rightBtn = findViewById(R.id.tv_actionbar_right);
        back = findViewById(R.id.iv_widget_actionbar_back);
        if (back !=null) {
            back.setOnClickListener(this);
            rightBtn.setOnClickListener(this);
        }
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

    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);

    }

    public void onRightClick(){

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_actionbar_right) {
            onRightClick();
        }
    }
}
