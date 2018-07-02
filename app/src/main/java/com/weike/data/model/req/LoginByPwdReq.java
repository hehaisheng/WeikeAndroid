package com.weike.data.model.req;

import com.weike.data.base.BaseReq;

public class LoginByPwdReq {

    public String phoneNumber;


    public String password;

    /**
     * 平台名字
     */
    public String appKey = "android";

    /**
     * 签名字段
     */
    public String sign;

    /**
     * 极光推送ID
     */
    public String igNo;

}
