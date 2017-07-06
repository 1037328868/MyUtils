package com.example.hasee.project_01_mobilesafe.activity.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.hasee.project_01_mobilesafe.activity.Bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2017/5/21.
 */

public class AppInfoUtils {
    //获取用户所有应用的包名,应用名,图标
    public static  ArrayList<AppInfo> getAllProgramInfo(Context context){
        ArrayList<AppInfo> appInfoList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        //获取所有已安装应用集合
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(0);
        for (int i = 0; i < installedApplications.size(); i++) {
            //单个应用信息
            ApplicationInfo applicationInfo = installedApplications.get(i);
            //获取应用名
            String appName = applicationInfo.loadLabel(packageManager).toString();
            //获取包名
            String packageName = applicationInfo.packageName;
            //获取图标
            Drawable icon = applicationInfo.loadIcon(packageManager);
            appInfoList.add(new AppInfo(packageName,appName,icon));
        }
        return appInfoList;
    }
}
