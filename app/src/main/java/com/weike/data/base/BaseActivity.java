package com.weike.data.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

    protected Button rightBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ImageView back = findViewById(R.id.iv_widget_actionbar_back);
        TextView leftText = findViewById(R.id.tv_widget_actionbar_left);
        if (back !=null) {
            back.setOnClickListener(this);
        }
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);

    }

    @Override
    public void onClick(View view) {

    }
}
