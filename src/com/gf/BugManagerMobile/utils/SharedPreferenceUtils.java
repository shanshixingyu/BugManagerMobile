package com.gf.BugManagerMobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * 操作SharedPreferences的工具类
 * Created by GuLang on 2014/12/28.
 */
public class SharedPreferenceUtils {
    public static final String SAVE_FILE_NAME = MyConstant.SHARED_PREFERENCE_NAME;

    public static void save(Context context, String key, Object value) {
        if (context == null || key == null || value == null || key.trim().length() == 0)
            return;
        SharedPreferences sharedPreferences = context.getSharedPreferences(SAVE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else {
            // 剩下部分全部当成是字符串保存
            editor.putString(key, value.toString());
        }
        editor.apply();
    }

    public static void delete(Context context, String key) {
        if (context == null || key == null || key.trim().length() == 0)
            return;
        SharedPreferences sharedPreferences = context.getSharedPreferences(SAVE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 返回指定键的内容
     *
     * @param context
     * @param key
     * @return
     */
    public static String queryString(Context context, String key) {
        if (context == null || key == null || key.trim().length() == 0)
            return "";
        SharedPreferences sharedPreferences = context.getSharedPreferences(SAVE_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static int queryInt(Context context, String key) {
        if (context == null || key == null || key.trim().length() == 0)
            return 0;
        SharedPreferences sharedPreferences = context.getSharedPreferences(SAVE_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    public static long queryLong(Context context, String key) {
        if (context == null || key == null || key.trim().length() == 0)
            return 0;
        SharedPreferences sharedPreferences = context.getSharedPreferences(SAVE_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0);
    }

    public static boolean queryBoolean(Context context, String key) {
        if (context == null || key == null || key.trim().length() == 0)
            return false;
        SharedPreferences sharedPreferences = context.getSharedPreferences(SAVE_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }
}
