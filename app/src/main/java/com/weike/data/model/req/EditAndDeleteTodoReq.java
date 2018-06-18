package com.weike.data.model.req;

import com.weike.data.base.BaseReq;

/**
 * 删除一个待办事项 或者 修改
 */
public class EditAndDeleteTodoReq extends BaseReq {
    public String toDoId;

    public String toDoType; // 1为代办 2为已办 3 过期  4 删除

}
