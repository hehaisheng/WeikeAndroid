package com.weike.data.broadcast;

        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.view.WindowManager;

        import com.weike.data.base.BaseActivity;
        import com.weike.data.business.login.LoginActivity;
        import com.weike.data.util.ActivitySkipUtil;
        import com.weike.data.util.LogUtil;
        import com.weike.data.util.ToastUtil;
        import com.weike.data.wxapi.TokenFailedActivity;

        import cn.pedant.SweetAlert.SweetAlertDialog;

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
