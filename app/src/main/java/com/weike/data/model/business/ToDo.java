package com.weike.data.model.business;

import android.os.Parcel;
import android.os.Parcelable;

import com.weike.data.util.TimeUtil;

/**
 * Created by LeoLu on 2018/6/6.
 */

public class ToDo implements Parcelable{

    /**
     * 是否提醒
     */
    public int isRemind;

    /**
     * 待办事时间
     */
    public String birthdaydate;

    /**
     * 是否提前提醒
     */
    public int isAdvance;  // 1是 2 否

    public int advanceDateType; //提前提醒时间类型

    public int advanceInterval; // 提醒时间间隔


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

    public int repeatInterval;//重复提醒时间间隔

    public int repeatDateType; //重复提醒时间类型

    public  ToDo(){

    }

    protected ToDo(Parcel in) {
        birthdaydate = in.readString();
        isAdvance = in.readInt();
        advanceDateType = in.readInt();
        advanceInterval = in.readInt();
        id = in.readString();
        content = in.readString();
        priority = in.readInt();
        isRepeat = in.readInt();
        repeatInterval = in.readInt();
        repeatDateType = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(birthdaydate);
        dest.writeInt(isAdvance);
        dest.writeInt(advanceDateType);
        dest.writeInt(advanceInterval);
        dest.writeString(id);
        dest.writeString(content);
        dest.writeInt(priority);
        dest.writeInt(isRepeat);
        dest.writeInt(repeatInterval);
        dest.writeInt(repeatDateType);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
