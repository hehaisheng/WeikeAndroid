package com.weike.data.util;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by LeoLu on 2018/5/31.
 */

public class BindingUtil {


    @BindingAdapter({"loadImage"})
    public static void loadImage(ImageView imageView ,String url){

        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    @BindingAdapter({"loadImageId"})
    public static void loadImageId(ImageView imageView , int id) {
        imageView.setImageResource(id);
    }
}
