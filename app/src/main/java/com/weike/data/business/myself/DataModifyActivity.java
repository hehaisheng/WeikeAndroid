package com.weike.data.business.myself;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.params.ProgressParams;
import com.weike.data.R;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.databinding.ActivityDataModifyBinding;
import com.weike.data.model.req.ModifyDataReq;
import com.weike.data.model.resp.UpLoadFileResp;
import com.weike.data.model.viewmodel.DataModifyActVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.util.LQRPhotoSelectUtils;
import com.weike.data.util.LogUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;

import java.io.File;

import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by LeoLu on 2018/6/1.
 */

public class DataModifyActivity extends BaseActivity {

    LQRPhotoSelectUtils utils;

    ActivityDataModifyBinding binding;

    DataModifyActVM vm;

    private android.support.v4.app.DialogFragment dialogFragment;

    private boolean isUpload = false;

    public String currentPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_data_modify);
        vm = new DataModifyActVM(this);
        binding.setModifyDataVM(vm);

        setCenterText("");
        setRightText("保存");
        setLeftText("资料修改");
        utils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                LogUtil.d("ActhomeDataModifyActivity","path->" + outputUri.getPath());

                currentPath = outputFile.getPath();
                vm.photoUrl.set(currentPath);
                isUpload = true;

            }
        },true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        vm.init();
    }

    @Override
    public void onLeftClick() {
        setResult(RESULT_OK,null);
        finish();
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        utils.takePhoto();
    }


    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        utils.selectPhoto();
    }



    @Override
    public void onRightClick() {
        super.onRightClick();
        if (TextUtils.isEmpty(vm.userName.get())) {
            ToastUtil.showToast("用户名不能为空");
            return;
        }
        setRightText("保存");
        dialogFragment = new CircleDialog.Builder()
                .setProgressText("正在保存")
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(getSupportFragmentManager());

        if (isUpload){ //如果有图片改过
            uploadAvatar(currentPath);
        } else {
            uploadData();
        }

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK,null);
        finish();
    }

    private void uploadData(){
        ModifyDataReq req = new ModifyDataReq();
        req.userName = vm.userName.get();
        req.email = vm.email.get();
        req.photoUrl = vm.photoUrl.get();
        req.occupation = vm.job.get();
        req.detailAddress = vm.location.get();
        req.sign = SignUtil.signData(req);


        RetrofitFactory.getInstance().getService().postAnything(req,Config.MODIFY_USER)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp>(){

                })).subscribe(new BaseObserver<BaseResp>() {
            @Override
            protected void onSuccess(BaseResp baseResp) throws Exception {
                dialogFragment.dismiss();
                ToastUtil.showToast("保存完成");
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });

    }

    private void uploadAvatar(String path) {
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RetrofitFactory.getInstance().getService().uploadAvatar(part, Config.UPLOAD_FILE)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<UpLoadFileResp>>(){

                })).subscribe(new BaseObserver<BaseResp<UpLoadFileResp>>() {
            @Override
            protected void onSuccess(BaseResp<UpLoadFileResp> upLoadFileRespBaseResp) throws Exception {

                vm.photoUrl.set(upLoadFileRespBaseResp.getDatas(). photoUrl);
                uploadData();


            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                ToastUtil.showToast("上传失败");
                dialogFragment.dismiss();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        utils.attachToActivityForResult(requestCode,resultCode,data);
    }


}
