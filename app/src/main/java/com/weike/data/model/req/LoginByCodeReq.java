package com.weike.data.model.req;

import com.weike.data.base.BaseReq;

/**
 * Created by LeoLu on 2018/5/23.
 * 用验证码登录的请求
 */
public class LoginByCodeReq extends BaseReq {
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
    public String parentId;
    /**
     * 极光io
     */
    public String igNo;
}
