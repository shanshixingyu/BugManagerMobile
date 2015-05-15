package com.gf.BugManagerMobile.utils;

/**
 * 常量类
 * Created by Administrator on 5/9 0009.
 */
public class MyConstant {
    private static final String TAG = "MyConstant";
    /**
     * 配置文件名称
     */
    public static final String SHARED_PREFERENCE_NAME = "SharedPreferenceName";
    /**
     * 输入用户名匹配正则表达式
     */
    public static final String USER_NAME_PATTERN = "[\\x{4e00}-\\x{9fa5}A-Za-z0-9_]+";
    /**
     * SharedPreferences中的用户名
     */
    public static final String SP_USER_NAME = "user_name";
    /**
     * SharedPreferences中的用户密码
     */
    public static final String SP_PASSWORD = "password";
    /**
     * SharedPreferences中的是否记住用户
     */
    public static final String SP_REMEMBER_ME = "remember_me";
    /**
     * 登录用户名
     */
    public static final String SP_LOGIN_USE_NAME = "login_user_name";
    /**
     * 登录用户角色
     */
    public static final String SP_LOGIN_ROLE_NAME = "login_role_name";
    /**
     * 登录用户密码
     */
    public static final String SP_LOGIN_PASSWORD = "login_password";
    /**
     * 登录用户角色Id
     */
    public static final String SP_LOGIN_ROLE_ID = "login_role_id";
    /**
     * 服务器IP地址
     */
    public static final String SERVER_IP = "server_ip";
    /**
     * 服务器端口号
     */
    public static final String SERVER_PORT = "server_port";
    /**
     * cookie
     */
    public static final String COOKIE = "save_cookie";
    /**
     * Htt网络访问结束消息
     */
    public static final int MSG_HTTP_FINISH = 0x123;

    /**
     * 密码错误
     */
    public static final int VISIT_CODE_WRONG_PASSWORD = 300;
    /**
     * 响应状态码不是200
     */
    public static final int VISIT_CODE_NO_OK = 100;
    /**
     * 访问成功
     */
    public static final int VISIT_CODE_SUCCESS = 0;
    /**
     * 连接超时
     */
    public static final int VISIT_CODE_CONNECT_TIME_OUT = -100;
    /**
     * 连接错误
     */
    public static final int VISIT_CODE_CONNECT_ERROR = -200;
    // /**
    // * 从登录界面
    // */
    // public static final String JUMP_LOGIN2MAIN_INFO = "jump_login2main_info";
    /**
     * 邮箱正则表达式
     */
    public static final String EMAIL_PATTERN = "[A-Za-z0-9]+@[A-Za-z0-9]+.[a-zA-Z]+";
    /**
     * PIM跳转到显示信息界面的信息
     */
    public static final String SHOW_INFO_DATA = "show_info_data";
    /**
     * 从首页到项目Bug查询页面的参数
     */
    public static final String HOME_2_SEARCH_PROJECT_ID = "home_2_search_project_id";
    public static final String HOME_2_SEARCH_PROJECT_NAME = "home_2_search_project_Name";

}
