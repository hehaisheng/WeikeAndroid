package com.weike.data.base;

/**
 * Created by LeoLu on 2018/5/22.
 * 响应基类
 */
public class BaseResp<T> {
    private int result;

    private String message = "";

    private T datas;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }
}
