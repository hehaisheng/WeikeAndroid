package com.weike.data.business.myself;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigTitle;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.TitleParams;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.weike.data.R;
import com.weike.data.adapter.ViewPagerGuideAdapter;
import com.weike.data.base.BaseActivity;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.config.Config;
import com.weike.data.databinding.ActivityMyQrcodeBinding;
import com.weike.data.model.req.GetSharePicReq;
import com.weike.data.model.resp.GetSharePicResp;
import com.weike.data.model.viewmodel.MyQRCodeActVM;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.payment.wechat.WXRegister;
import com.weike.data.util.BitmapUtil;
import com.weike.data.util.LQRPhotoSelectUtils;
import com.weike.data.util.SignUtil;
import com.weike.data.util.ToastUtil;
import com.weike.data.util.TransformerUtils;
import com.weike.data.wxapi.WXEntryActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

import static com.weike.data.util.BitmapUtil.PIC_NAME;
import static com.weike.data.util.BitmapUtil.PIC_PATH;

/**
 * Created by LeoLu on 2018/6/1.
 * 我的二维码Activity
 */
public class MyQRCodeActivity extends BaseActivity {
    MyQRCodeActVM vm;

    ActivityMyQrcodeBinding binding;

    LQRPhotoSelectUtils utils;

    private Uri customUrl;

    private int pagePosition;

    private List<View> shareViews = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_qrcode);

        vm = new MyQRCodeActVM(this);
        binding.setMyqrcodeVM(vm);


        setRightText("分享");
        setCenterText("");
        setLeftText("我的二维码");



        init();

        utils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                ImageView imageView = shareViews.get(1).findViewById(R.id.image_custom);
                customUrl = outputUri;
                Glide.with(getApplicationContext()).load(outputUri).into(imageView);

            }
        },true);


        binding.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                pagePosition = position;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        utils.attachToActivityForResult(requestCode,resultCode,data);
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        utils.takePhoto();
    }


    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        utils.selectPhoto();
    }

    public void init() {
        GetSharePicReq req = new GetSharePicReq();
        req.sign = SignUtil.signData(req);

        RetrofitFactory.getInstance().getService().postAnything(req, Config.GET_SHARED_PIC)
                .compose(TransformerUtils.jsonCompass(new TypeToken<BaseResp<GetSharePicResp>>() {

                })).subscribe(new BaseObserver<BaseResp<GetSharePicResp>>() {
            @Override
            protected void onSuccess(BaseResp<GetSharePicResp> getSharePicRespBaseResp) throws Exception {
                vm.iconUrl.set(getSharePicRespBaseResp.getDatas().sharePicturesUrl);

                resetViewPager(customUrl,0);


            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        });
    }

    public void shareLayout(View view){
        binding.llShareLayout.setVisibility(View.GONE);
    }

    @Override
    public void onRightClick(boolean isModify) {
        super.onRightClick(isModify);

        setRightText("分享");
        if (isModify) {

            binding.llShareLayout.setVisibility(View.VISIBLE);
        } else {
            binding.llShareLayout.setVisibility(View.GONE);
        }
    }

    public void shareToMoment(View view){
        shared(true,pagePosition);
    }

    public void shareToFriend(View view){
        shared(false,pagePosition);
    }



    public void savePic(View view){
        if (pagePosition == 1) {
            View v = shareViews.get(1);
            Bitmap myBitmap = BitmapUtil.convertViewToBitmap(v);
            String path = BitmapUtil.savePicture(myBitmap,System.currentTimeMillis() + ".png");
            ToastUtil.showToast("保存至:" + path);
            resetViewPager(customUrl,pagePosition);
            return;
        }

        new Thread(){
            public void run(){
                if (pagePosition == 0) {
                    String url = vm.iconUrl.get();

                    File crsh = new File(PIC_PATH + PIC_NAME);
                    if (crsh.exists()) {
                        runOnUiThread(()->{
                            ToastUtil.showToast("图片已存在");
                        });

                        return;
                    }

                    Bitmap bitmap = BitmapUtil.GetImageInputStream(url);
                   String path =  BitmapUtil.savePicture(bitmap, PIC_NAME);
                    runOnUiThread(()->{
                        ToastUtil.showToast("保存至:" + path);
                    });
                }
            }
        }.start();
    }

    private Bitmap myBitmap;

    public void shared(boolean isTimelineCb , int position){


        if (position == 1) {
            View view = shareViews.get(1);
            myBitmap = BitmapUtil.convertViewToBitmap(view);
        }


        new Thread() {
            public void run() {

                if (position == 0) {
                     myBitmap = BitmapUtil.GetImageInputStream(vm.iconUrl.get());
                }

                WXImageObject imageObject = new WXImageObject(myBitmap);
                //这个构造方法中自动把传入的bitmap转化为2进制数据,或者你直接传入byte[]也行
                //注意传入的数据不能大于10M,开发文档上写的

                WXMediaMessage msg = new WXMediaMessage();  //这个对象是用来包裹发送信息的对象
                msg.mediaObject = imageObject;
                //msg.mediaObject实际上是个IMediaObject对象,
                //它有很多实现类,每一种实现类对应一种发送的信息,
                //比如WXTextObject对应发送的信息是文字,想要发送文字直接传入WXTextObject对象就行


                Bitmap thumbBitmap = Bitmap.createScaledBitmap(myBitmap, 150, 150, true);

                msg.setThumbImage(thumbBitmap);
                //在这设置缩略图
                //官方文档介绍这个bitmap不能超过32kb
                //如果一个像素是8bit的话换算成正方形的bitmap则边长不超过181像素,边长设置成150是比较保险的
                //或者使用msg.setThumbImage(thumbBitmap);省去自己转换二进制数据的过程
                //如果超过32kb则抛异常


                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = WXEntryActivity.WX_SHAERD;
                req.message = msg;
                req.scene = isTimelineCb ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;


                WXRegister.getApi().sendReq(req);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resetViewPager(customUrl,pagePosition);
                    }
                });

            }
        }.start();


    }

    private void resetViewPager(Uri customUrl,int position){
        shareViews.clear();
        for(int i = 0 ; i < 2;i++){
            View view = LayoutInflater.from(MyQRCodeActivity.this).inflate(R.layout.widget_share_item,null);
            ImageView outSide = view.findViewById(R.id.image_outSide);
            outSide.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    savePic(null);
                    return false;
                }
            });
            ImageView custom = view.findViewById(R.id.image_custom);
            if (i == 0) {

                custom.setVisibility(View.GONE);
            }

            custom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoClick();

                }
            });

            if(customUrl != null) {
                Glide.with(getApplicationContext()).load(customUrl).into(custom);
            }

            Glide.with(getApplicationContext()).load(vm.iconUrl.get()).into(outSide);


            shareViews.add(view);
        }
        binding.viewPager.setAdapter(new ViewPagerGuideAdapter(shareViews));
        binding.viewPager.setCurrentItem(position);

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
                            PermissionGen.with(MyQRCodeActivity.this)
                                    .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.CAMERA
                                    ).request();
                        } else {
                            PermissionGen.needPermission(MyQRCodeActivity.this,
                                    LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE}
                            );
                        }
                    }
                })
                .setNegative("取消", null)
                .show(((FragmentActivity)MyQRCodeActivity.this).getSupportFragmentManager());
    }

}
