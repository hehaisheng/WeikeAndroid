package com.weike.data.model.viewmodel;

import com.tencent.mm.opensdk.modelmsg.GetMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.weike.data.payment.wechat.WXRegister;

/**
 * Created by LeoLu on 2018/6/1.
 */

public class MyQRCodeActVM {

    /**
     * 图片的Url
     */
    public String iconUrl;

    /**
     * 用户名字
     */
    public String userName;

    /**
     * 分享Url
     */
    public String shredUrl;


    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;

    public void sharedToWeChat(){

        WXTextObject textObj = new WXTextObject();
        textObj.text = "测试专用的数据";

        // ��WXTextObject�����ʼ��һ��WXMediaMessage����
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        // �����ı����͵���Ϣʱ��title�ֶβ�������
        // msg.title = "Will be ignored";
        msg.description ="测试专用的数据";

        // ����һ��Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "text" + System.currentTimeMillis(); // transaction�ֶ�����Ψһ��ʶһ������
        req.message = msg;
        req.scene = mTargetScene;

        // ����api�ӿڷ�����ݵ�΢��
        WXRegister.getApi().sendReq(req);


    }

    public void sharedToQQ(){

    }

    public void QRCode(){

    }
}
