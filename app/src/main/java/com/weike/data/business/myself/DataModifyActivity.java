package com.weike.data.business.myself;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
        initPhotoSel();

    }
    private void initPhotoSel(){

        utils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                LogUtil.d("test","path->" + outputUri.getPath());
                vm.photoUrl.set("");
                currentPath = outputUri.getPath();
                vm.photoUrl.set(currentPath);
                isUpload = true;


            }
        },true);
        vm.setmLqrPhotoSelectUtils(utils);
    }
    @Override
    protected void onResume() {
        super.onResume();
       // vm.init();
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
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                try {
                    size = fis.available();
                    LogUtil.d("test","文件大小"+size);
                    if(size>600000){
                        boolean success=compressBitmap(path,500,path);
                        size = fis.available();
                        LogUtil.d("test","压缩后文件大小"+size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
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

    private static Bitmap compressByResolution(String imgPath, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, opts);

        int width = opts.outWidth;
        int height = opts.outHeight;
        int widthScale = width / w;
        int heightScale = height / h;

        int scale;
        if (widthScale < heightScale) { //保留压缩比例小的
            scale = widthScale;
        } else {
            scale = heightScale;
        }

        if (scale < 1) {
            scale = 1;
        }
        Log.i(TAG, "图片分辨率压缩比例：" + scale);

        opts.inSampleSize = scale;

        opts.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, opts);

        return bitmap;
    }

    public static  String TAG="test";
    public static boolean compressBitmap(String srcPath, int ImageSize, String savePath) {
        int subtract;
        Log.i(TAG, "图片处理开始..");
        Bitmap bitmap = compressByResolution(srcPath, 1024, 720); //分辨率压缩
        if (bitmap == null) {
            Log.i(TAG, "bitmap 为空");
            return false;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        Log.i(TAG, "图片分辨率压缩后：" + baos.toByteArray().length / 1024 + "KB");


        while (baos.toByteArray().length > ImageSize * 1024) { //循环判断如果压缩后图片是否大于ImageSize kb,大于继续压缩
            subtract = setSubstractSize(baos.toByteArray().length / 1024);
            baos.reset();//重置baos即清空baos
            options -= subtract;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            Log.i(TAG, "图片压缩后：" + baos.toByteArray().length / 1024 + "KB");
        }
        Log.i(TAG, "图片处理完成!" + baos.toByteArray().length / 1024 + "KB");
        try {
            FileOutputStream fos = new FileOutputStream(new File(savePath));//将压缩后的图片保存的本地上指定路径中
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (bitmap != null) {
            bitmap.recycle();
        }

        return true; //压缩成功返回ture
    }
    private static int setSubstractSize(int imageMB) {

        if (imageMB > 1000) {
            return 60;
        } else if (imageMB > 750) {
            return 40;
        } else if (imageMB > 500) {
            return 20;
        } else {
            return 10;
        }
    }


 }
