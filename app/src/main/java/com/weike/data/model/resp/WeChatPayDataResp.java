package com.weike.data.model.resp;

import com.weike.data.base.BaseResp;

public class WeChatPayDataResp extends BaseResp{
    public WxPay wxpay;

    public static class WxPay{
        public String appid;

        public String noncestr;

        public String partnerid;

        public String prepayid;

        public String sign;

        public String timestamp;

    }
}
