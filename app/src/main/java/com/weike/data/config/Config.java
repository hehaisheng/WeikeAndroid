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

    /**
     * 获取验证码
     */
    public static final String GET_SMS_CODE = APP_SERVER + "wkzs-restful/system/smsCode/";
    /**
     *验证码登录
     */
    public static final String LOGIN_FOR_CODE = APP_SERVER + "wkzs-restful/userLogin/loginByCode/";

    /**
     * 账号密码登录
     */
    public static final String LOGIN_FOR_ACCOUNT = APP_SERVER + "wkzs-restful/userLogin/loginByPassword/";

    /**
     * 获取首页登录信息(轮播图等)
     */
    public static final String MAIN_PAGE_DATA = APP_SERVER +"wkzs-restful/index/loginInfo";

    /**
     * 搜索接口
     */
    public static final String SEARCH_CONTENT = APP_SERVER + "wkzs-restful/index/search";

    /**
     * 用户信息
     */
    public static final String USER_INFO = APP_SERVER + "wkzs-restful/user/userIndex";

}
