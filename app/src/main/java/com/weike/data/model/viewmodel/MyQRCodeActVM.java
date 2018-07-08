package com.weike.data.model.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;

import com.google.gson.reflect.TypeToken;
import com.tencent.mm.opensdk.modelmsg.GetMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.weike.data.base.BaseObserver;
import com.weike.data.base.BaseResp;
import com.weike.data.base.BaseVM;
import com.weike.data.config.Config;
import com.weike.data.model.req.GetSharePicReq;
import com.weike.data.model.resp.GetSharePicResp;
import com.weike.data.network.RetrofitFactory;
import com.weike.data.payment.wechat.WXRegister;
import com.weike.data.util.BitmapUtil;
import com.weike.data.util.SignUtil;
import com.weike.data.util.TransformerUtils;

import java.util.concurrent.ExecutionException;

/**
 * Created by LeoLu on 2018/6/1.
 */

public class MyQRCodeActVM extends BaseVM {

    /**
     * 图片的Url
     */
    public ObservableField<String> iconUrl = new ObservableField<>();

    /**
     * 用户名字
     */
    public String userName;

    /**
     * 分享Url
     */
    public String shredUrl;

    public MyQRCodeActVM(Activity activity) {
        this.activity = activity;

    }

    public void savePic(){

    }






    private void shared(boolean isTimelineCb) {
        new Thread() {
            public void run() {

                Bitmap myBitmap = BitmapUtil.GetImageInputStream(iconUrl.get());


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
                req.transaction = "text" + System.currentTimeMillis();
                req.message = msg;
                req.scene = isTimelineCb ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;


                WXRegister.getApi().sendReq(req);
            }
        }.start();
    }

    public void shareToMoment(){
        shared(true);
    }

    public void sharedToWeChat() {
        shared(false);

    }


}
