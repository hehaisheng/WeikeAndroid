package com.weike.data.broadcast;

        import android.app.AlertDialog;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;

        import com.weike.data.business.setting.TokenFailedActivity;

/**
 * Created by LeoLu on 2018/6/1.
 * token失效的广播发送
 */
public class TokenFailedBroadcast extends BroadcastReceiver {

    public static final String TOKEN_FAILED = "com.weike.data.TOKEN_FAILED";
    private AlertDialog alertDialog;

    private static boolean isShow = false;

    @Override
    public void onReceive(Context context, Intent intent) {


        Intent i = new Intent(context,TokenFailedActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }
}
