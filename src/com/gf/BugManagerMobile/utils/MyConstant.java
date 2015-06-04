package com.gf.BugManagerMobile.utils;

import android.graphics.Color;

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
    public static final String NAME_PATTERN = "[\\x{4e00}-\\x{9fa5}A-Za-z0-9_]+";
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
     * 登录用户ID
     */
    public static final String SP_LOGIN_USE_ID = "login_user_id";
    /**
     * 登录用户名
     */
    public static final String SP_LOGIN_USE_NAME = "login_user_name";
    /**
     * 登录用户密码
     */
    public static final String SP_LOGIN_PASSWORD = "login_password";
    /**
     * 登录用户角色Id
     */
    public static final String SP_LOGIN_ROLE_ID = "login_role_id";
    /**
     * 登录用户角色名
     */
    public static final String SP_LOGIN_ROLE_NAME = "login_role_name";
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
     * 没有登录
     */
    public static final int VISIT_CODE_NO_LOGIN = 1100;
    /**
     * 不是post请求
     */
    public static final int VISIT_CODE_NO_POST = 1000;
    /**
     * 用户不存在
     */
    public static final int VISIT_CODE_NO_USER = 900;
    /**
     * 没有缺陷
     */
    public static final int VISIT_CODE_NOT_PERMISSION = 800;
    /**
     * 不在日期范围内
     */
    public static final int VISIT_CODE_OUT_DATE = 700;
    /**
     * 用户还没有登录
     */
    public static final int VISIT_CODE_NOT_LOGIN = 600;
    /**
     * 已经存在
     */
    public static final int VISIT_CODE_HAS_EXIST = 500;
    /**
     * 不存在
     */
    public static final int VISIT_CODE_NOT_EXIST = 400;

    /**
     * 密码错误
     */
    public static final int VISIT_CODE_WRONG_PASSWORD = 300;
    /**
     * 失败
     */
    public static final int VISIT_CODE_FAILURE = 200;
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
    public static final String HOME_2_SEARCH_BUG_PROJECT_ID = "home_2_search_bug_project_id";
    public static final String HOME_2_SEARCH_BUG_PROJECT_NAME = "home_2_search_bug_project_Name";
    public static final String SEARCH_BUG_2_BUG_LIST_CONDITION = "search_bug_2_bug_list_condition";
    public static final String BUG_LIST_2_BUG_DETAIL_BUG_ID = "bug_list_2_bug_detail_bug_id";
    public static final String BUG_DETAIL_2_BUG_INTRODUCE = "bug_detail_2_bug_introduce";
    public static final String USER_LIST_2_USER_MODIFY = "user_list_2_user_modify";
    public static final String GROUP_LIST_2_GROUP_MODIFY = "group_list_2_group_modify";
    public static final String PROJECT_LIST_2_PROJECT_MODIFY = "project_list_2_project_modify";
    public static final String PROJECT_LIST_2_MODULE_LIST = "project_list_2_module_list";
    public static final String MODULE_LIST_2_MODULE_ADD = "module_list_2_module_add";
    public static final String MODULE_LIST_2_MODULE_MODIFY = "module_list_2_module_modify";

    /**
     * 当不使能的的时候的字体颜色
     */
    public static final int NOT_ENABLE_TEXT_COLOR = Color.parseColor("#ffbcbcbe");

}
