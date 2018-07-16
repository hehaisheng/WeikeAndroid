package com.weike.data.model.resp;

import com.weike.data.base.BaseResp;

import java.util.List;

/**
 * Created by LeoLu on 2018/6/25.
 */

public class GetClientMsgDetailListResp extends BaseResp {


    /**
     * result : 0
     * datas : {"messageList":[{"id":5,"content":"测试5","createDate":"2018-04-18 11:52","type":1,"status":1,"is_remind":1},{"id":4,"content":"测试4","createDate":"2018-04-18 11:52","type":1,"status":2,"is_remind":1},{"id":3,"content":"测试3","createDate":"2018-04-18 11:52","type":1,"status":1,"is_remind":1}]}
     */


    private List<MessageListBean> messageList;

    public List<MessageListBean> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageListBean> messageList) {
        this.messageList = messageList;
    }

    public static class MessageListBean {
        /**
         * id : 5
         * content : 测试5
         * createDate : 2018-04-18 11:52
         * type : 1
         * status : 1
         * is_remind : 1
         */

        public int id;
        public String content;
        public String createDate;
        public int type;  //0-系统消息、1-生日提醒、2-产品提醒、3-日志提醒、4-纪念日提醒
        public int status;
        public int remindType;
        public int handleType;


    }

}
