package com.weike.data.model.req;

import com.weike.data.base.BaseReq;

/**
 * Created by LeoLu on 2018/6/11.
 */

public class GetVCodeAfterLoginReq  extends BaseReq {
    /**
     * 短信类型
     */
    public int type;

    public String phoneNumber;
}
