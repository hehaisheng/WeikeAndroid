package com.weike.data.model.req;

import com.weike.data.base.BaseReq;

public class GetVerificationCodeReq {

    /**
     * 签名字段
     */
    public String sign;

    /**
     * 平台名字
     */
    public String appKey = "android";
    /**
     * 手机号码
     */
    public String phoneNumber;
    /**
     * 666
     */
    public int type = 1;
}
