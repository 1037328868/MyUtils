package com.example.hasee.zhihuibeijing.utils;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by hasee on 2017/6/1.
 */

public class WindowUtils {
    public static int getWindowWidht(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        return defaultDisplay.getWidth();
    }
    public static int getWindowHeight(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        return defaultDisplay.getHeight();
    }
}
