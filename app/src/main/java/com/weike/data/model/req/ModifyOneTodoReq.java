package com.weike.data.model.req;

import com.weike.data.base.BaseReq;

public class ModifyOneTodoReq extends BaseReq {

    public int isRemind;
    public String id;
    public String toDoDate;
    public String content;
    public int priority;
    public int isRepeat;
    public int repeatIntervalHour;
    public int beforeRemindDay;


}
