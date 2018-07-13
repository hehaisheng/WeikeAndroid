package com.weike.data.model.req;

import com.weike.data.base.BaseReq;

import java.util.List;

/**
 * ${huneng} on 2018/7/13 14:16
 */

public class AddClientListReq extends BaseReq {

    public List<ClientListAttr> clientAttr;


    public static class ClientListAttr{
        public String userName;

        public String phoneNumber;
    }
}
