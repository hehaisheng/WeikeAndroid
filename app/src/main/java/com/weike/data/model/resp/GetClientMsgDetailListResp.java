package com.weike.data.model.resp;

import com.google.gson.annotations.SerializedName;
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

        private int id;
        private String content;
        private String createDate;
        private int type;
        private int status;
        private int is_remind;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIs_remind() {
            return is_remind;
        }

        public void setIs_remind(int is_remind) {
            this.is_remind = is_remind;
        }
    }

}
