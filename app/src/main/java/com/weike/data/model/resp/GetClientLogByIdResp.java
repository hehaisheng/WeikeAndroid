package com.weike.data.model.resp;

import com.google.gson.annotations.SerializedName;
import com.weike.data.base.BaseResp;

import java.util.List;

/**
 * Created by LeoLu on 2018/6/28.
 */

public class GetClientLogByIdResp extends BaseResp {


    private List<LogListBean> logList;

    public List<LogListBean> getLogList() {
        return logList;
    }

    public void setLogList(List<LogListBean> logList) {
        this.logList = logList;
    }

    public static class LogListBean {
        /**
         * id : 2
         * logDate : 2018-04-28
         * content : 2阿斯达
         */

        private int id;
        private String logDate;
        private String content;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLogDate() {
            return logDate;
        }

        public void setLogDate(String logDate) {
            this.logDate = logDate;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
