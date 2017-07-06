package com.example.hasee.project_01_mobilesafe.activity.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hasee on 2017/5/12.
 */

public class SharePreUtils {

    private static SharedPreferences sp;

    /**
     * 储存boolean
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveBooleanInfo(Context context, String key, boolean value) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 读取boolean
     *
     * @param context
     * @param key
     * @param defValue 如果没找到,返回defValue
     * @return
     */
    public static boolean getBooleanInfo(Context context, String key, boolean defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }

    /**
     * 储存int
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveIntInfo(Context context, String key, int value) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key, value).commit();
    }

    /**
     * @param context
     * @param key
     * @param defValue
     * @return 读取int
     */
    public static int getIntInfo(Context context, String key, int defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getInt(key, defValue);
    }
}
