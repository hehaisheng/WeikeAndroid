package com.weike.data.model.business;

/**
 * 纪念日类
 */
public class AnniversaryDay {

    public String anniversaryName;

    public String anniversaryDate;

    public int orRemind; //是否提醒

    public String beforeRemindDate; //提前提醒时间 天数

    public String content; //提醒内容

    public int isRepeat; //是否重复提醒
}
