package com.weike.data.util;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.weike.data.R;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * Created by LeoLu on 2018/5/31.
 */

public class BindingUtil {


    @BindingAdapter({"loadImage"})
    public static void loadImage(ImageView imageView ,String url){

        if (TextUtils.isEmpty(url)) {
            return;
        }
        Glide.with(imageView.getContext()).load(url)
                .into(imageView);
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
