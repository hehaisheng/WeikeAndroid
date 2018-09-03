package com.weike.data.model.business;

/**
 * Created by LeoLu on 2018/6/15.
 *
 * 用于响应作用的toDo类
 */
public class ToDoResp {

    public int priority;

    /**
     * 重复提醒时间类型 1 天 2周 3月 4年
     */
    public int dateType;

    /**
     * 事项时间
     */
    public String toDoDate;

    /**
     * 事项ID 用于删除
     */
    public String id;

    /**
     * 事项内容
     */
    public String content;

    /**
     * 事项内容
     */
    public String clientName;

    /**
     * 提前提醒时间
     */
    public int beforeRemindDay;


    //客户id
    public long clientId;



}
