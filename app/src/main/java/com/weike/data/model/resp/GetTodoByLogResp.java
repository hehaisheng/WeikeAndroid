package com.weike.data.model.resp;

import com.weike.data.base.BaseReq;
import com.weike.data.model.business.ProductBean;
import com.weike.data.model.business.ToDo;

/**
 * ${huneng} on 2018/7/17 11:39
 */

public class GetTodoByLogResp extends BaseReq {
    public String logId;

    public String logDate;

    public String logContent;

    public ProductBean.RemindBean remind;
}
