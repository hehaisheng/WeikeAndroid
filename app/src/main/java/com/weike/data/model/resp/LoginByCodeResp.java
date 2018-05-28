package com.weike.data.model.resp;

import com.weike.data.base.BaseResp;

/**
 * Created by LeoLu on 2018/5/23.
 */

public class LoginByCodeResp extends BaseResp {
    private String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
