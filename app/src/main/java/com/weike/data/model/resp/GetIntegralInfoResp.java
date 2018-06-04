package com.weike.data.model.resp;

import com.weike.data.base.BaseResp;

import java.util.List;

/**
 * Created by LeoLu on 2018/6/4.
 */

public class GetIntegralInfoResp extends BaseResp {
    /**
     * 当前积分
     */
    public String currentIntegral;

    public List<IntegralInfo> integralInfoList;

    public class IntegralInfo{

        /**
         * 积分数量
         */
       public String integralNum;

        /**
         * 积分类型
         */
       public int type;

        /**
         * 积分状态
         */
       public int status;

        /**
         * 创建时间
         */
       public String createDate;

    }
}
