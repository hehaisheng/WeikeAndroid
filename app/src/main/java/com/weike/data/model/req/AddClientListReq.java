package com.weike.data.model.req;

import com.weike.data.base.BaseReq;

/**
 * ${huneng} on 2018/7/13 14:16
 */

public class AddClientListReq extends BaseReq {


    public String clientArr;


    public static class ClientListAttr{
        public String userName;

        public String phoneNumber;
    }
}
