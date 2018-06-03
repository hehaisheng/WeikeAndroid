package com.weike.data.model.req;

import com.weike.data.base.BaseReq;

public class GetVerificationCodeReq extends BaseReq {
    public String phoneNumber;

    public int type = 1;
}
