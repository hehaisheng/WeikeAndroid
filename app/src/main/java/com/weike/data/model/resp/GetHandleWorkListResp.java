package com.weike.data.model.resp;

import com.weike.data.base.BaseResp;
import com.weike.data.model.business.ToDoResp;

import java.util.List;

/**
 * Created by LeoLu on 2018/6/15.
 */

public class GetHandleWorkListResp extends BaseResp {
    public List<ToDoResp> toDoList;
}
