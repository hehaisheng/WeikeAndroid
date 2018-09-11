package com.weike.data.model.business;

public class ProductBean {
    public long id;
    public String productName;
    public RemindBean remind;



    public static class RemindBean {
        /**
         * 是否提醒
         */
        public int isRemind = 1;

        /**
         * 待办事时间
         */
        public String remindDate;

        /**
         * 是否提前提醒
         */
        public int isAdvance;  // 1是 2 否

        public String advanceDateType; //提前提醒时间类型

        public String advanceInterval; // 提醒时间间隔


        /**
         * id啦
         */
        public String id;

        /**
         * 内容
         */
        public String content;

        /**
         * 优先级 1 == 高 2 == 中 3== 低
         */
        public int priority = 1;

        /*
         * 是否重复提醒
         */
        public int isRepeat = 1; // 是 2 否

        public String repeatInterval;//重复提醒时间间隔

        public String repeatDateType; //重复提醒时间类型

    }
}
