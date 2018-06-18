package com.weike.data.model.business;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by LeoLu on 2018/6/6.
 */

public class ToDo implements Parcelable{

    /**
     * 待办事时间
     */
    public String toDoDate = null ;
    /**
     * 内容
     */
    public String content = null;

    /**
     * 优先级 1 == 高 2 == 中 3== 低
     */
    public int priority = 1;

    /*
     * 是否重复
     */
    public int isRepeat = 1; // 重复 2 重复


    /**
     * 是否提醒
     */
    public int isRemind = 1; // 1提醒 2不提醒

    /**
     * 重复提醒日期类型
     */
    public int dateType = 1;

    /**
     * 重复提醒数量
     */
    public int repeatIntervalHour = 1;

    /**
     * 提前提醒几天
     */
    public int beforeRemindDay = 1;

    public ToDo(){

    }


    protected ToDo(Parcel in) {
        toDoDate = in.readString();
        content = in.readString();
        priority = in.readInt();
        isRepeat = in.readInt();
        isRemind = in.readInt();
        dateType = in.readInt();
        repeatIntervalHour = in.readInt();
        beforeRemindDay = in.readInt();
    }

    public static final Creator<ToDo> CREATOR = new Creator<ToDo>() {
        @Override
        public ToDo createFromParcel(Parcel in) {
            return new ToDo(in);
        }

        @Override
        public ToDo[] newArray(int size) {
            return new ToDo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(toDoDate);
        dest.writeString(content);
        dest.writeInt(priority);
        dest.writeInt(isRepeat);
        dest.writeInt(isRemind);
        dest.writeInt(dateType);
        dest.writeInt(repeatIntervalHour);
        dest.writeInt(beforeRemindDay);
    }
}
