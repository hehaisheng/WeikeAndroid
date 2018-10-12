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
 public static final String GET_SMS_CODE = APP_SERVER + "system/smsCode/";
 /**
  *验证码登录
  */
 public static final String LOGIN_FOR_CODE = APP_SERVER + "userLogin/loginByCode/";

 /**
  * 账号密码登录
  */
//    public static final String LOGIN_FOR_ACCOUNT = APP_SERVER + "userLogin/loginByPassword/";
 public static final String LOGIN_FOR_ACCOUNT = APP_SERVER + "userLogin/loginByPassword/";
 /**
  * 获取首页登录信息(轮播图等)
  */
 public static final String MAIN_PAGE_DATA = APP_SERVER +"index/loginInfo";

 /**
  * 搜索接口
  */
 public static final String SEARCH_CONTENT = APP_SERVER + "index/search";

 /**
  * 用户信息
  */
 public static final String USER_INFO = APP_SERVER + "user/userIndex";

 /**
  * 用户反馈
  */
 public static final String USER_FEEDBACK = APP_SERVER + "index/addUserFeedBack";

 /**
  * 积分详情
  */
 public static final String INTEGRAL_INFO = APP_SERVER + "user/integralInfo";

 /**
  * 获取首页的消息列表
  */
 public static final String GET_MSG_LIST = APP_SERVER + "message/newsList";
 /**
  * 获取客户列表
  */
 public static final String GET_CLIENT_LIST = APP_SERVER + "client/selectClientList";

 /**
  * 获取属性列表
  */
 public static final String GET_ATTR_LIST = APP_SERVER + "client/selectAttributes";

 /**
  * 添加客户
  */
 public static final String ADD_CLIENT = APP_SERVER +"client/addClient";

 /**
  * 获取客户标签列表
  */
 public static final String GET_CLIENT_TAG_LIST = APP_SERVER + "client/selectClientLabel";

 /**
  * 重置密码
  */
 public static final String RESET_PWD = APP_SERVER + "user/password";

 /**
  * 登录之后获取验证码
  */
 public static final String GET_CODE_AFTER_LOGIN = APP_SERVER + "system/loginAfterSmsCode";

 /**
  *  添加属性
  */
 public static final String ADD_ATTR = "client/addAttributes";
 /**
  * 删除属性
  */
 public static final String DEL_ATTR = "client/upAttributes";

 /**
  * 根据客户ID查询
  */
 public static final String GET_CLIENT_BY_LABEL = "client/getClientByLabelAndUser";

 /**
  * 添加标签
  */
 public static final String ADD_LABEL = "user/addOrEditLabel";

 /**
  * 上传文件
  */
 public static final String UPLOAD_FILE = "system/uploadFile";

 /**
  * 修改用户
  */
 public static final String MODIFY_USER = "user/editUser";

 /**
  * 删除标签
  */
 public static final String DELETE_LABEL = "user/dateleClientLabel";

 /**
  * 查询列表集合
  */
 public static final String CHECK_LABEL_TAG_NUM = "user/selectLabelSort";

 /**
  * 标记和删除消息 (用于首页)
  */
 public static final String DELETE_HOME_MSG = "message/deleteRemindByClient";

 /**
  * 获取待办事列表
  */
 public static final String GET_TO_DO_LIST = "log/selectToDoList";

 /**
  * 编辑 和 删除 一个待办事项
  */
 public static final String EDIT_AND_DEL_TODO_STATUS = "log/editToDoType";

 /**
  * 添加日志
  */
 public static final String ADD_LOG = "log/addLog";

 /**
  * 获取客户详情
  */
 public static final String GET_CLIENT_DETAIL = "client/selectClient";

 /**
  * 用户查看单个待办事项
  */
 public static final String GET_ONE_TODO_STATUS = "log/selectToDo";

 /**
  * 修改单个事项
  */
 public static final String MODIFY_ONE_TODO_STATUS = "log/editToDo";

 /**
  * 修改客户
  */
 public static final String MODIFY_CLIENT  = "client/saveClient";

 /**
  * 批量移动客户
  */
 public static final String MOVE_CLIENT_TO_LABEL = "client/ClientMobileLabel";

 /**
  * 获取客户的消息详情列表
  */
 public static final String GET_CLIENT_DETAIL_MSG_LIST = "message/newsListByClient";

 /**
  * 获取支付信息
  */
 public static final String GET_PAY_DATA = "payment/launch";


 /**
  * 获取客户日志
  */
 public static final String GET_LOG_BY_ID = "log/selectLogByClient";

 /**
  * 根据日志ID 获取待办事项
  */
 public static final String GET_TODO_BY_LOG = "log/selectToDoByLog";

 /**
  * 获取分享海报
  */
 public static final String GET_SHARED_PIC = "user/sharePictures";

 /**
  * 根据标签ID获取待办事项
  */
 public static final String GET_TODO_BY_TAG = "log/seleteToDoByLable";


 /**
  * 修改客户消息的状态
  */
 public static final String MODIFY_CLIENT_MSG = "message/newsEditStatus";

 /**
  * 修改提醒或者不在提醒
  */
 public static final String MODIFY_CLIENT_MSG_REMIND ="message/newsEditRemind";

 /**
  * 更换手机号码
  */
 public static final String CHANGE_PHONE_NUM = "user/editPhoneNumber";

 /**
  * 会员权益
  */
 public static final String GET_VIP_LICENCE = "user/rights";


 /**
  * 显示天数
  */
 public static final String GET_HANDLE_DISPLAY = "index/selectUpcomingDate";

 /**
  * 修改待办事项显
  */
 public static final String MODIFY_HANDLE_DISPLAY = "index/editUpcomingDate";

 /**
  * 发送关闭或者打开push
  */
 public static final String UPDATE_PUSH = "user/closePush";


 /**
  * 密码强制检查
  */
 public static final String CHECK_NEED_PWD = "user/forcePassword";

 /**
  * 强制改密码
  */
 public static final String ADD_PWD_FORCE = "user/addforcePassword";
 /**
  * 删除日志
  */
 public static final String DELETE_LOG = "log/deletelog";

 /**
  * 修改日志事项
  */
 public static final String MODIFY_LOG_TODO ="log/editLogToDo";

 /**
  * 删除纪念日
  */
 public static final String DEL_ANIDAY= "client/deleteAnniversary";

 /**
  * 删除产品
  */
 public static final String DEL_PRODUCT = "client/deleteProduct";

 /**
  * 批量添加客户
  */
 public static final String ADD_CLIENT_LIST = "client/addClientList";

 /**
  * 删除客户
  */
 public static final String DELETE_CLIENT = "client/deleteClient";

 /**
  * 判断不同的用户
  */
 public static final String INSPECT_DIFFERENT_CLIENT = "client/inspectClient";


 /**
  * 删除关联的客户
  */
 public static final String  DELETE_RELATED_CLIENT = "client/deleteRelatedClient";



//修改日志内容
 public static final String  CHANGE_LOG= "log/editLogContent";


 //修改协议
 public static final String  CHANGE_AGREE= "userLocal/updateIsOpen.do";

 //获取图片链接

 public static final String  FETCH_IMAGE= "userOss/findFile.do";

 //重命名

 public static final String  RENAME_FILE= "userOss/renameFile.do";

//删除文件

 public static final String  DELETE_FILE= "userOss/delete.do";

 //上传文档

 public static final String UPLOAD_FILE_OSS= "userOss/upload.do";

}
