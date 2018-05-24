package com.weike.data.network.callback;

/**
 * Created by LeoLu on 2018/5/23.
 */

public interface HttpCallBack<H,T> {

    /**
     * 响应
     * @param h
     */
    void onSuccess(H h);

    void onFailed(H h);
}
