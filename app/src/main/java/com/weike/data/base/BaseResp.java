package com.weike.data.base;

/**
 * Created by LeoLu on 2018/5/22.
 * 响应基类
 */
public class BaseResp<T> {
    public int result = 1;

    public String message = "";

    public T datas;
}
