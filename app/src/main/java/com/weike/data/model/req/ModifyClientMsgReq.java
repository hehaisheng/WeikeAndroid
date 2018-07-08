package com.weike.data.model.req;

import com.weike.data.base.BaseReq;

public class ModifyClientMsgReq extends BaseReq {
    public String id;

    public int status; //2-已读、3-删除
}
