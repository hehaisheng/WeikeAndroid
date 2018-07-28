package com.weike.data.business.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.weike.data.R;
import com.weike.data.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ${huneng} on 2018/7/26 09:28
 */

public class BigPicActivity extends BaseActivity {

    @BindView(R.id.iv_show_bigpic)
    public ImageView bigPic;


    public static void startActivity(Activity activity ,String url) {
        Intent intent = new Intent(activity,BigPicActivity.class);
        intent.putExtra("data",url);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_pic);
        ButterKnife.bind(this);

        String data = getIntent().getStringExtra("data");
        Glide.with(this).load(data).error(R.mipmap.icon_normal_3).placeholder(R.mipmap.icon_normal_3).into(bigPic);
    }
}
