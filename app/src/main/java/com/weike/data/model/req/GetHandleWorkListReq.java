package com.weike.data.model.req;

import com.weike.data.base.BaseReq;

/**
 * Created by LeoLu on 2018/6/15.
 */

public class GetHandleWorkListReq extends BaseReq{

    /**
     * 待办事类型 1 代办事项  2.已办事项  3 过期事项
     */
    public int toDoType;

    /**
     * 加载页码
     */
    public int page;

}
