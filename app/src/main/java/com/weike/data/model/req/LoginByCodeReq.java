package com.weike.data.model.req;

import com.weike.data.base.BaseReq;

/**
 * Created by LeoLu on 2018/5/23.
 * 用验证码登录的请求
 */
public class LoginByCodeReq{
    /**
     * 手机号码
     */
    public String phoneNumber;
    /**
     * 验证码
     */
    public String code;

    /**
     * 邀请人ID
     */
    public String parentId = "-1";
    /**
     * 极光io
     */
    public String igNo = "-1";

    /**
     * 平台名字
     */
    public String appKey = "android";

    /**
     * 签名字段
     */
    public String sign;
}
