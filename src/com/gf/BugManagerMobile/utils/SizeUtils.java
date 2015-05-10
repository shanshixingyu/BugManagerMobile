package com.gf.BugManagerMobile.utils;

import android.content.Context;

public class SizeUtils {
    public static int dp2px(Context context, float dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f); // 四舍五入
    }

    public static int sp2px(Context context, float sp) {
        return (int) (context.getResources().getDisplayMetrics().scaledDensity
                * sp + 0.5f);
    }

}
