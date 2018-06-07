package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;
import android.os.Environment;

import com.weike.data.base.BaseVM;
import com.weike.data.business.setting.ResetPwdActivity;
import com.weike.data.util.ActivitySkipUtil;
import com.weike.data.util.FileCacheUtils;
import com.weike.data.util.LogUtil;

import java.io.File;

public class AppSettingActVM extends BaseVM {

    public ObservableField<String> cacheSize = new ObservableField<>("");



    public AppSettingActVM(Activity activity) {
        this.activity = activity;
        loadCache();
    }

    private void loadCache(){
       String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File outCachePath = activity.getExternalCacheDir();
        File outFilePath = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            String outCacheSize = FileCacheUtils.getCacheSize(outCachePath);
            String outFileSize = FileCacheUtils.getCacheSize(outFilePath);


            LogUtil.d("AppSettingVM","--->" + outCacheSize + "," +outFileSize);
            cacheSize.set(outCacheSize);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置密码
     */
    public void setPwd(){
        ActivitySkipUtil.skipAnotherAct(activity, ResetPwdActivity.class);
    }

    /**
     * 清除缓存
     */
    public void clearCash(){
        //show dialog
    }

    /**
     * 退出登录
     */
    public void outOfLogin(){
        //TODO show Dialog
    }

    /**
     * 检查版本更新
     */
    public void checkVersion(){
        //TODO showDialog
    }

    /**
     * 通知重置
     */
    public void resetNotice(){

    }
}
