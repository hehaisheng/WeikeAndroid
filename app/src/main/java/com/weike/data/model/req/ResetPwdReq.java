package com.weike.data.model.req;

import com.weike.data.base.BaseReq;

/**
 * Created by LeoLu on 2018/6/11.
 */

public class ResetPwdReq extends BaseReq {
    /**
     * code
     */
    public String code;

    /**
     * 密码
     */
    public String password;
}
