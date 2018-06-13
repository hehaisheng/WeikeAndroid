package com.weike.data.model.business;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by LeoLu on 2018/6/6.
 */

public class ToDo implements Parcelable{
    /**
     * 时间
     */
    public String time;

    /**
     * 内容
     */
    public String content;

    /**
     * 优先级
     */
    public int priority;

    /*
     * 是否重复
     */
    public boolean IsRepeat;

    /**
     * 提醒时间
     */
    public int remindInvTime;

    protected ToDo(Parcel in) {
        time = in.readString();
        content = in.readString();
        priority = in.readInt();
        IsRepeat = in.readByte() != 0;
        remindInvTime = in.readInt();
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
        dest.writeString(time);
        dest.writeString(content);
        dest.writeInt(priority);
        dest.writeByte((byte) (IsRepeat ? 1 : 0));
        dest.writeInt(remindInvTime);
    }
}
