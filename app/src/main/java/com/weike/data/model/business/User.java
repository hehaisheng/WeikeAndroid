package com.weike.data.model.business;

import com.weike.data.model.resp.GetClientListResp;
import com.weike.data.model.resp.GetClientTagListResp;

import java.util.ArrayList;
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

    public List<GetClientTagListResp.TagSub> labelList;

    /**
     * 职业
     */
    public String job;
    /**
     * 邮箱
     */
    public String email;

    /**
     * 地址
     */
    public String address;

    /**
     * 待办事项时间
     * 如果是自定义 那么 就是  天数
     */
    public int handleWorkingType;

    public List<AnotherAttributes> anotherAttributes = new ArrayList<>();
}
