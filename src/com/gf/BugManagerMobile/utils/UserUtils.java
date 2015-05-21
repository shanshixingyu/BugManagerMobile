package com.gf.BugManagerMobile.utils;

/**
 * 获得用户相关的信息
 * Created by GuLang on 2015-05-20.
 */
public class UserUtils {
    private static final String TAG = "UserUtils";

    /**
     * 将用户角色Id转换成用户角色
     * @param roleId
     * @return
     */
    public static String getUserRoleName(int roleId) {
        String str = "";
        switch (roleId) {
            case 0:
                str = "超级管理员";
                break;
            case 1:
                str = "管理人员";
                break;
            case 2:
                str = "开发人员";
                break;
            case 3:
                str = "测试人员";
                break;
        }
        return str;
    }

}
