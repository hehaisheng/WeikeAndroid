package com.weike.data.network;

/**
 * Created by LeoLu on 2018/5/22.
 * retrofit 的中间管理类 用于释放资源等..
 */
public class RetrofitManager {

    public volatile static RetrofitManager instance;

    public static final Object lock = new Object();

    public static RetrofitManager getInstance() {

        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    synchronized (lock){
                        instance = new RetrofitManager();
                    }
                }
            }
        }
        return instance;
    }

    public  RetrofitManager url(String url) {

        return this;
    }

    


}
