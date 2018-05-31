package com.weike.data.util;

import android.app.Activity;
import android.content.Intent;

import java.util.HashMap;

/**
 * Created by LeoLu on 2018/5/28.
 * activity 跳转的简单封装类
 */
public class ActivitySkipUtil {

    public static void skipAnotherAct(Activity act, Class<? extends Activity> target, boolean finish) {
        skipAnotherAct(act, target);
        if (finish) {
            act.finish();
        }
    }

    public static void skipAnotherAct(Activity act, Class<? extends Activity> target) {
        Intent intent = new Intent(act, target);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        act.startActivity(intent);
    }


    public static void skipAnotherAct(Activity activity, Class<? extends Activity> target,
                                      HashMap<String, ? extends Object> hashMap) {

    }

}
