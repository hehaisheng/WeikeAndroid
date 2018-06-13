package com.weike.data.base;

import com.weike.data.util.SpUtil;

/**
 * Created by LeoLu on 2018/5/22.
 * 请求基类
 */
public class BaseReq {
    /**
     * 令牌
     */
    public String token = SpUtil.getInstance().getCurrentToken();
    /**
     * 签名字段
     */
    public String sign;

    /**
     * 平台名字
     */
    public String appKey = "android";
}
