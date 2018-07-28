package com.weike.data.util;

import android.databinding.BindingAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.weike.data.R;

/**
 * Created by LeoLu on 2018/5/31.
 */

public class BindingUtil {


    @BindingAdapter({"loadImage"})
    public static void loadImage(ImageView imageView ,String url){
        LogUtil.d("BindingUtil","------>" + url);
        Glide.with(imageView.getContext()).load(url)
                .error(R.mipmap.icon_normal_3).into(imageView);
    }

    @BindingAdapter({"loadImageId"})
    public static void loadImageId(ImageView imageView , int id) {
        imageView.setImageResource(id);
    }


    @BindingAdapter({"updateEditInput"})
    public static void updateEditInput(EditText editText , boolean isModify) {
        if (isModify == false) {
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
        } else {
            editText.setFocusableInTouchMode(true);
            editText.setFocusable(true);

            editText.requestFocus();
        }
    }
}
