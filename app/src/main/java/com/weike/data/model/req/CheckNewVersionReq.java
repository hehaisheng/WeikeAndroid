package com.weike.data.model.req;

import com.weike.data.base.BaseReq;
import com.weike.data.base.BaseResp;

public class CheckNewVersionReq extends BaseReq {
    /**
     * 版本号
     */
    public String apkCode;

    /**
     * 当前包名
     */
    public String apkPackage;
}
