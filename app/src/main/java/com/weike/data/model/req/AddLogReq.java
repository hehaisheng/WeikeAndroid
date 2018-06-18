package com.weike.data.model.req;

import com.weike.data.base.BaseReq;
import com.weike.data.model.business.Client;
import com.weike.data.model.business.ClientRelated;
import com.weike.data.model.business.ToDo;
import com.weike.data.model.business.ToDoResp;

import java.util.List;

/**
 * Created by LeoLu on 2018/6/6.
 */

public class AddLogReq extends BaseReq {
    /**
     * 日志内容
     */
    public String content;

    /**
     * 日志时间
     */
    public String logDate;

    /**
     * 客户数组
     */
    public String clientArr;

    /**
     * 设置提醒
     */
    public String toDo;
    
}
