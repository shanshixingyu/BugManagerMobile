package com.gf.BugManagerMobile.utils;

import android.graphics.Color;

/**
 * Created by Administrator on 5/16 0016.
 */
public class BugUtils {
    private static final String TAG = "BugUtils";

    public static String getPriorityStr(int priority) {
        String str = "";
        switch (priority) {
            case 0:
                str = "低";
                break;
            case 1:
                str = "中";
                break;
            case 2:
                str = "高";
                break;
            case 3:
                str = "紧急";
                break;
        }
        return str;
    }

    public static int getPriorityColor(int priority) {
        int color;
        switch (priority) {
            case 0:
                color = Color.parseColor("#ff494949");
                break;
            case 1:
                color = Color.parseColor("#00FFFF");
                break;
            case 2:
                color = Color.parseColor("#FF00FF");
                break;
            case 3:
                color = Color.parseColor("#FF0000");
                break;
            default:
                color = Color.parseColor("#ff494949");
        }
        return color;
    }

    public static String getStatusStr(int status) {
        String str = "";
        switch (status) {
            case 0:
                str = "关闭";
                break;
            case 1:
                str = "未解决";
                break;
            case 2:
                str = "解决";
                break;
            case 3:
                str = "重新激活";
                break;
            case 4:
                str = "其它";
                break;
        }
        return str;
    }

    public static int getStatusColor(int status) {
        int color;
        switch (status) {
            case 0:
                color = Color.GREEN;
                break;
            case 1:
                color = Color.RED;
                break;
            case 2:
                color = Color.GREEN;
                break;
            case 3:
                color = Color.RED;
                break;
            case 4:
                color = Color.parseColor("#00FFFF");
                break;
            default:
                color = Color.GREEN;
        }
        return color;
    }

    public static String getSeriousStr(int serious) {
        String str = "";
        switch (serious) {
            case 0:
                str = "不影响";
                break;
            case 1:
                str = "轻度影响";
                break;
            case 2:
                str = "影响";
                break;
            case 3:
                str = "影响较大";
                break;
            case 4:
                str = "严重影响";
                break;
        }
        return str;
    }

}
