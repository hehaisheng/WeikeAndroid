package com.weike.data.model.resp;

import com.weike.data.base.BaseResp;

public class GetUserInfoResp extends BaseResp {
    /**
     * 用户名
     */
    public String userNames;
    /*
     * 手机号码
     */
    public String phoneNumber;
    /**
     * 头像
     */
    public String photoUrl;
    /*
     * 会员时间
     */
    public String timeoutDate;
    /**
     * 当前积分
     */
    public String currentIntegral;
    /**
     * 会员等级
     */
    public int memberLevel;
}
