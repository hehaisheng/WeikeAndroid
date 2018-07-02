package com.weike.data.model.business;

import com.weike.data.model.resp.GetClientDetailMsgResp;

public class ProductBean {
    public int id;
    public String productName;
    public RemindBean remind;



    public static class RemindBean {
        /**
         * id : 65
         * isRemind : 1
         * content : 测试2
         * remindDate : 1999-05-02 20:20
         * beforeRemindDay : 3
         * repeatIntervalHour : 1
         * priority : 1
         * isRepeat : 1
         * dateType : 2
         */

        public int id;
        public String isRemind = "";
        public String content = "";
        public String remindDate = "";
        public String beforeRemindDay = "";
        public String repeatIntervalHour = "";
        public String priority = "";
        public String isRepeat = "";
        public String dateType = "";

        /*public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }*/

    }
}
