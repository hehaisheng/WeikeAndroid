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

    /**
     * 用户反馈
     */
    public static final String USER_FEEDBACK = APP_SERVER + "wkzs-restful/index/addUserFeedBack";

    /**
     * 积分详情
     */
    public static final String INTEGRAL_INFO = APP_SERVER + "wkzs-restful/user/integralInfo";

    /**
     * 获取首页的消息列表
     */
    public static final String GET_MSG_LIST = APP_SERVER + "wkzs-restful/message/newsList";
    /**
     * 获取客户列表
     */
    public static final String GET_CLIENT_LIST = APP_SERVER + "wkzs-restful/client/selectClientList";

    /**
     * 获取属性列表
     */
    public static final String GET_ATTR_LIST = APP_SERVER + "wkzs-restful/client/selectAttributes";

    /**
     * 添加客户
     */
    public static final String ADD_CLIENT = APP_SERVER +"wkzs-restful/client/addClient";

    /**
     * 获取客户标签列表
     */
    public static final String GET_CLIENT_TAG_LIST = APP_SERVER + "wkzs-restful/client/selectClientLabel";

    /**
     * 重置密码
     */
    public static final String RESET_PWD = APP_SERVER + "wkzs-restful/user/password";

    /**
     * 登录之后获取验证码
     */
    public static final String GET_CODE_AFTER_LOGIN = APP_SERVER + "wkzs-restful/system/loginAfterSmsCode";

    /**
     *  添加属性
     */
    public static final String ADD_ATTR = "wkzs-restful/client/addAttributes";
    /**
     * 删除属性
     */
    public static final String DEL_ATTR = "wkzs-restful/client/upAttributes";

    /**
     * 根据客户ID查询
     */
    public static final String GET_CLIENT_BY_LABEL = "wkzs-restful/client/getClientByLabelAndUser";

    /**
     * 添加标签
     */
    public static final String ADD_LABEL = "wkzs-restful/user/addOrEditLabel";

    /**
     * 上传文件
     */
    public static final String UPLOAD_FILE = "wkzs-restful/system/uploadFile";

    /**
     * 修改用户
     */
    public static final String MODIFY_USER = "wkzs-restful/user/editUser";

    /**
     * 删除标签
     */
    public static final String DELETE_LABEL = "wkzs-restful/user/dateleClientLabel";

    /**
     * 查询列表集合
     */
    public static final String CHECK_LABEL_TAG_NUM = "wkzs-restful/user/selectLabelSort";

    /**
     * 标记和删除消息 (用于首页)
     */
    public static final String DELETE_HOME_MSG = "wkzs-restful/message/deleteRemindByClient";

    /**
     * 获取待办事列表
     */
    public static final String GET_TO_DO_LIST = "wkzs-restful/log/selectToDoList";

    /**
     * 编辑 和 删除 一个待办事项
     */
    public static final String EDIT_AND_DEL_TODO_STATUS = "wkzs-restful/log/editToDoType";

    /**
     * 添加日志
     */
    public static final String ADD_LOG = "wkzs-restful/log/addLog";

    /**
     * 获取客户详情
     */
    public static final String GET_CLIENT_DETAIL = "wkzs-restful/client/selectClient";

    /**
     * 用户查看单个待办事项
     */
    public static final String GET_ONE_TODO_STATUS = "wkzs-restful/log/selectToDo";

    /**
     * 修改单个事项
     */
    public static final String MODIFY_ONE_TODO_STATUS = "wkzs-restful/log/editToDo";

    /**
     * 修改客户
     */
    public static final String MODIFY_CLIENT  = "wkzs-restful/client/saveClient";

    /**
     * 批量移动客户
     */
    public static final String MOVE_CLIENT_TO_LABEL = "wkzs-restful/client/ClientMobileLabel";

}
