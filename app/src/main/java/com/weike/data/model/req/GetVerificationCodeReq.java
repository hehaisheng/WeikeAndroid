package com.weike.data.model.req;

import com.weike.data.base.BaseReq;

/**
 * Created by LeoLu on 2018/5/28.
 */

public class GetVerificationCodeReq extends BaseReq {

    /**
     * 手机号码
     */
    public String phoneNumber;

    public int type = 1;
}
