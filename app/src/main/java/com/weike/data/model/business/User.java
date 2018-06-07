package com.weike.data.model.business;

import com.weike.data.model.resp.GetClientListResp;

import java.util.List;

/**
 * Created by LeoLu on 2018/6/4.
 * 本地的用户对象
 */
public class User {

    /**
     * 手机号码
     */
    public String phoneNumber;

    /**
     * 头像Url
     */
    public String iconUrl;

    /**
     * 用户名字
     */
    public String userName;

    /**
     * 当前用户的Vip时间
     */
    public  String vipTime;

    /**
     * 当前的客户列表
     */
    public List<GetClientListResp.ClientListSub> clietList;



}
