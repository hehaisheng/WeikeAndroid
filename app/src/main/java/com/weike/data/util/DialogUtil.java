package com.weike.data.util;

import android.app.Dialog;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.params.ProgressParams;

/**
 * Created by LeoLu on 2018/6/19.
 */

public class DialogUtil {
    public static DialogFragment showLoadingDialog(FragmentManager manager , String title){
        return new CircleDialog.Builder()
                .setProgressText(title)
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(manager);
    }
}
