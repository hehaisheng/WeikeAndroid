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

import com.google.gson.reflect.TypeToken;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigTitle;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.TitleParams;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.base.BaseVM;
import com.weike.data.business.client.AddClientActivity;
import com.weike.data.business.client.ClientTagActivity;
import com.weike.data.business.setting.ClientTagSettingActivity;
import com.weike.data.config.Config;
import com.weike.data.model.business.User;
import com.weike.data.model.req.GetClientTagListReq;
import com.weike.data.model.resp.GetClientTagListResp;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.ClientTagComparator;
import com.weike.data.util.LQRPhotoSelectUtils;
import com.weike.data.util.PickerUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.SpUtil;
import com.weike.data.util.TransformerUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;

import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.SinglePicker;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

import static com.weike.data.business.client.AddClientActivity.KEY_OF_LABEL;
import static com.weike.data.business.client.AddClientActivity.TAG_CLIENT_ID;

public class AddClientActVM extends BaseVM {

    LQRPhotoSelectUtils mLqrPhotoSelectUtils;

    public ObservableField<String> userName = new ObservableField<>();

    public ObservableField<String> remarks = new ObservableField<>();

    public ObservableField<String> photoUrl = new ObservableField<>();

    public ObservableField<String> label = new ObservableField<>("添加标签");

    public String labelId;

    public ObservableField<Boolean> isModify = new ObservableField<>(false);


    public void setmLqrPhotoSelectUtils(LQRPhotoSelectUtils mLqrPhotoSelectUtils) {
        this.mLqrPhotoSelectUtils = mLqrPhotoSelectUtils;
    }

    public AddClientActVM(Activity activity) {
        this.activity = activity;

    }



    public void labelClick(){
        User user = SpUtil.getInstance().getUser();

        String[] array = new String[user.labelList.size()];
        for(int i = 0 ; i < user.labelList.size();i++){
            String str = user.labelList.get(i).sort + "." + user.labelList.get(i).labelName;;
            if(i == 0)
                str = str.replace(".","");
            array[i] = str;
        }

        SinglePicker<String> picker = new SinglePicker<String>(activity,array);
        PickerUtil.onConstellationPicker(null, picker, new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int i, String s) {
                label.set(user.labelList.get(i).labelName);
                labelId = user.labelList.get(i).id;
            }
        });

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
