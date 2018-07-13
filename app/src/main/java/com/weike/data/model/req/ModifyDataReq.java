package com.weike.data.model.req;

import com.weike.data.base.BaseReq;

public class ModifyDataReq extends BaseReq {

    /*8
     * 用户名
     */
    public String userName;

    /**
     * 图片URL
     */
    public String photoUrl;

    /**
     * 职位
     */
    public String occupation;

    /**
     * 邮箱
     */
    public String email;

    /**
     * 详细地址
     */
    public String detailAddress;
}
