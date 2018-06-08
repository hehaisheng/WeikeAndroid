package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigText;
import com.mylhyl.circledialog.callback.ConfigTitle;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.TextParams;
import com.mylhyl.circledialog.params.TitleParams;
import com.weike.data.R;
import com.weike.data.WKBaseApplication;
import com.weike.data.base.BaseVM;
import com.weike.data.business.setting.ResetPwdActivity;
import com.weike.data.util.ActivitySkipUtil;
import com.weike.data.util.FileCacheUtils;
import com.weike.data.util.LogUtil;
import com.weike.data.util.ToastUtil;

import java.io.File;

public class AppSettingActVM extends BaseVM {

    public ObservableField<String> cacheSize = new ObservableField<>("");


    public AppSettingActVM(FragmentActivity activity) {
        this.activity = activity;
        loadCache();
    }

    private void loadCache() {
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File outCachePath = activity.getExternalCacheDir();
        File outFilePath = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            String outCacheSize = FileCacheUtils.getCacheSize(outCachePath);
            String outFileSize = FileCacheUtils.getCacheSize(outFilePath);


            LogUtil.d("AppSettingVM", "--->" + outCacheSize + "," + outFileSize);
            cacheSize.set(outCacheSize);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置密码
     */
    public void setPwd() {
        ActivitySkipUtil.skipAnotherAct(activity, ResetPwdActivity.class);
    }

    /**
     * 清除缓存
     */
    public void clearCash() {
        new CircleDialog.Builder()
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.backgroundColor = Color.WHITE;
                        params.backgroundColorPress = Color.BLUE;
                    }
                })
                .setTitle("提示").configTitle(new ConfigTitle() {
            @Override
            public void onConfig(TitleParams params) {
                params.textColor = activity.getResources().getColor(R.color.color_content);
            }
        })
                .setText("是否确定清除缓存")
                .configText(new ConfigText() {
                    @Override
                    public void onConfig(TextParams params) {
                        params.padding = new int[]{100, 0, 100, 50};
                    }
                })
                .setNegative("取消", null).configNegative(new ConfigButton() {
            @Override
            public void onConfig(ButtonParams params) {
                params.backgroundColorPress = Color.WHITE;
            }
        })
                .setPositive("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FileCacheUtils.cleanExternalCache(WKBaseApplication.getInstance());
                        loadCache();
                        ToastUtil.showToast("清除缓存成功");
                    }
                })
                .configPositive(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.backgroundColorPress = Color.WHITE;
                    }
                })
                .show(((AppCompatActivity) activity).getSupportFragmentManager());
    }

    /**
     * 退出登录
     */
    public void outOfLogin() {
        //TODO show Dialog
    }

    /**
     * 检查版本更新
     */
    public void checkVersion() {
        //TODO showDialog
    }

    /**
     * 通知重置
     */
    public void resetNotice() {

    }
}
