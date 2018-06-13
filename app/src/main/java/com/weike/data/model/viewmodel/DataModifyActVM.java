package com.weike.data.model.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;

import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigTitle;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.TitleParams;
import com.weike.data.base.BaseVM;
import com.weike.data.model.business.User;
import com.weike.data.util.LQRPhotoSelectUtils;
import com.weike.data.util.SpUtil;
import com.weike.data.util.ToastUtil;

import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class DataModifyActVM extends BaseVM {

    public ObservableField<String> photoUrl = new ObservableField<>();

    public ObservableField<String> phoneNum = new ObservableField<>();

    public ObservableField<String> userName = new ObservableField<>();

    public ObservableField<String> job = new ObservableField<>();

    public ObservableField<String> location = new ObservableField<>();

    public ObservableField<String> email = new ObservableField<>();

    public ObservableField<String> detailAddress = new ObservableField<>();

    public DataModifyActVM(Activity activity) {
        this.activity = activity;

        User user = SpUtil.getInstance().getUser();
        photoUrl.set(user.iconUrl);
        phoneNum.set(user.phoneNumber);
        userName.set(user.userName);
        job.set(user.job);
        location.set(user.address);
        email.set(user.email);
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
