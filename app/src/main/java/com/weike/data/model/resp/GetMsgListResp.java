package com.weike.data.model.resp;

import com.weike.data.base.BaseResp;

import java.util.List;

/**
 * Created by LeoLu on 2018/6/6.
 */

public class GetMsgListResp extends BaseResp {


    public List<MsgData> messageGroupVmList;

     public class MsgData{
        /**
         * 客户ID
         */
        public String clientId;

        /**
         * 用户名字
         */
        public String clientName;

        /**
         * 消息内容
         */
        public String messageContent;

        /**
         * 未读数
         */
        public int unreadNum;

        /**
         * 最新时间
         */
        public String newestDate;
    }
}
