package com.example.hasee.project_01_mobilesafe.activity.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.example.hasee.project_01_mobilesafe.activity.service.PhoneAdressService;

import java.util.List;

/**
 * Created by hasee on 2017/5/14.
 */

public class ServiceUtils {

    /**
     *
     * @param context 上下文
     * @param seviceName 服务的class字节码文件
     * @return 是否在运行
     */
    public static boolean isRunningService(Context context,Class seviceName){
        //获取服务运行状态
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        //获取全部服务
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(1000);
        String name = seviceName.getName();
        for (ActivityManager.RunningServiceInfo services:runningServices) {
            String className = services.service.getClassName();
            if (name.equals(className)){
                //如果全部服务中有被查找的服务,返回true
                return true;
            }
        }
        return false;
    }
}
