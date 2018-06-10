package com.weike.data.model.business;

public class Product {
    public String productName;

    public int orRemind;

    public int beforeRemindDate; //提醒天数

    public String content ; //内容

    public String productDate; //产品时间

    public int isRepeat ; // 是否重复提醒

    public int dateType; //重复提醒类型

    public int repeatRemindDate; //重复提醒间隔
}
