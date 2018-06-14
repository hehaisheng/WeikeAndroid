package com.weike.data.model.req;

import com.weike.data.base.BaseReq;

/**
 * Created by LeoLu on 2018/6/14.
 */

public class EditAndDelMsgReq extends BaseReq {
    public String id;

    public int status ; //2 已读 3 删除
}
