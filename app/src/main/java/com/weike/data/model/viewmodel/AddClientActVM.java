package com.weike.data.model.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;

import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigTitle;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.TitleParams;
import com.weike.data.base.BaseVM;
import com.weike.data.business.client.AddClientActivity;
import com.weike.data.business.setting.ClientTagSettingActivity;
import com.weike.data.util.LQRPhotoSelectUtils;

import java.io.File;

import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

import static com.weike.data.business.client.AddClientActivity.KEY_OF_LABEL;

public class AddClientActVM extends BaseVM {

    LQRPhotoSelectUtils mLqrPhotoSelectUtils;

    public ObservableField<String> userName = new ObservableField<>();

    public ObservableField<String> remarks = new ObservableField<>();

    public ObservableField<String> photoUrl = new ObservableField<>();

    public ObservableField<String> label = new ObservableField<>("添加标签");

    public ObservableField<Boolean> isModify = new ObservableField<>();

    public void setmLqrPhotoSelectUtils(LQRPhotoSelectUtils mLqrPhotoSelectUtils) {
        this.mLqrPhotoSelectUtils = mLqrPhotoSelectUtils;
    }

    public AddClientActVM(Activity activity) {
        this.activity = activity;

    }



    public void labelClick(){
        Intent intent = new Intent(activity, ClientTagSettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivityForResult(intent,KEY_OF_LABEL);

    }



    public void photoClick(){
        final String[] items = {"拍照", "从相册选择"};
        new CircleDialog.Builder()
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.backgroundColorPress = Color.CYAN;
                        //增加弹出动画
                    }
                })
                .setTitle("提示")
                .configTitle(new ConfigTitle() {
                    @Override
                    public void onConfig(TitleParams params) {
                    }
                })
                .setItems(items, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int
                            position, long id) {
                        if (position == 0) {
                            PermissionGen.with(activity)
                                    .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.CAMERA
                                    ).request();
                        } else {
                            PermissionGen.needPermission(activity,
                                    LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE}
                            );
                        }
                    }
                })
                .setNegative("取消", null)
                .show(((FragmentActivity)activity).getSupportFragmentManager());
    }
}
