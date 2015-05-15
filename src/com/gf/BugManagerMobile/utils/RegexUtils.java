package com.gf.BugManagerMobile.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则匹配工具类
 * Created by Administrator on 5/12 0012.
 */
public class RegexUtils {
    private static final String TAG = "RegexUtils";

    /**
     * 将传入的字符串和传入的正则表达式比较，验证是否正确
     * @param patternStr
     * @param text
     * @return
     */
    public static boolean isMatch(String patternStr, String text) {
        if (patternStr == null || text == null)
            return false;
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
//        return Pattern.matches(patternStr, text);
    }
}
