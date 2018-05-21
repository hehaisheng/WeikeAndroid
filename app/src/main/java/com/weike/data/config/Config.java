package com.weike.data.config;

import com.weike.data.BuildConfig;

/**
 * Created by LeoLu on 2018/5/21.
 */
public final class Config {
    /**
     * 服务器IP
     */
    public static final String APP_SERVER = BuildConfig.APP_SERVER;
    /**
     * APP的秘钥
     */
    public static final String APP_SECRET = BuildConfig.APP_SECRET;
    /**
     * 这个是登录返回的token , 每次登录都不同
     */
    public static String APP_TOKEN = "";
}
