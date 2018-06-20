package com.weike.data.model.resp;

import com.weike.data.base.BaseResp;

public class GetOneTodoStatusResp extends BaseResp {

    /**
     * id : 6
     * toDoDate : 2018-05-03
     * content : 待办事项内容
     * priority : 1
     * isRepeat : 1
     * repeatIntervalHour : 5
     * beforeRemindDay : 5
     */

    public int isRemind;
    public int id;
    public String remindDate;
    public String content;
    public int priority;
    public int isRepeat;
    public int repeatIntervalHour;
    public int beforeRemindDay;




}
