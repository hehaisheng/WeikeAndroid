package com.weike.data.util;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.weike.data.R;
import com.weike.data.WKBaseApplication;

/**
 * Created by LeoLu on 2018/5/28.
 */

public class ToastUtil {

    private static Toast mToast;



    public static Toast showToast(String text){
        if (mToast == null) {
            mToast = Toast.makeText(WKBaseApplication.getInstance(), text, Toast.LENGTH_SHORT);

        }
        mToast.setText(text);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
        return mToast;
    }

    public static void myToastMesage( String text) {
        int bgId = R.drawable.shape_toast_bg;
        View toastRoot = LayoutInflater.from(WKBaseApplication.getInstance().getApplicationContext()).inflate(
                R.layout.widget_mytoast_layout, null);
        Toast toast = new Toast(WKBaseApplication.getInstance());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastRoot);

        TextView tv = (TextView) toastRoot.findViewById(R.id.toast_message_tip);
        tv.setText(text);
        tv.setBackgroundResource(bgId);
        toast.show();
    }
}
