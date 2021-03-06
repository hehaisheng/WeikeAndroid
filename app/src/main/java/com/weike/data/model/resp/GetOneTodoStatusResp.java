package com.weike.data.model.resp;

import com.weike.data.base.BaseResp;

public class GetOneTodoStatusResp extends BaseResp {


    public int isRemind;
    /**
     * 待办事时间
     */
    public String birthdaydate;

    /**
     * 是否提前提醒
     */
    public int isAdvance;  // 1是 2 否

    public String advanceDateType; //提前提醒时间类型

    public String advanceInterval; // 提醒时间间隔


    /**
     * id啦
     */
    public String id;

    /**
     * 内容
     */
    public String content;

    /**
     * 优先级 1 == 高 2 == 中 3== 低
     */
    public int priority = 1;

    /*
     * 是否重复提醒
     */
    public int isRepeat = 1; // 是 2 否

    public String  repeatInterval;//重复提醒时间间隔

    public String repeatDateType; //重复提醒时间类型


    public String remindId;




}
