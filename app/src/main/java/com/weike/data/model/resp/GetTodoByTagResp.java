package com.weike.data.model.resp;

import com.weike.data.base.BaseResp;
import com.weike.data.model.business.ToDoResp;

import java.util.List;

public class GetTodoByTagResp extends BaseResp {
    public List<ToDoResp> toDoList;
}
