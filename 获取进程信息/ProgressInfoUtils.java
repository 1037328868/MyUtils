package com.example.hasee.project_01_mobilesafe.activity.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Debug;
import android.text.style.ForegroundColorSpan;

import com.example.hasee.project_01_mobilesafe.R;
import com.example.hasee.project_01_mobilesafe.activity.Bean.ProgressInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by hasee on 2017/5/17.
 */

public class ProgressInfoUtils {
    /**
     * @param context
     * @return  获取正在运行的进程数
     */
    public static int getRuningProgress(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        return runningAppProcesses.size();
    }

    /**
     * @param context
     * @return 所有进程的总数
     */
    public static int getAllProgress(Context context){
        PackageManager packageManager = context.getPackageManager();
        //获取手机所有的已安装的应用(activity,service,provider,receiver)
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES
                | PackageManager.GET_PROVIDERS | PackageManager.GET_RECEIVERS);
        HashSet<String> allProgressName = new HashSet<>();

        for (int i = 0; i < installedPackages.size(); i++) {
            //遍历获取到每一个包的信息
            PackageInfo packageInfo = installedPackages.get(i);
            //从packageinfo中获取到每个app的application节点下声明的进程名(有的进程没有声明)
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            String processName = applicationInfo.processName;
            allProgressName.add(processName);

            //从包信息中获取activity进程
            ActivityInfo[] activities = packageInfo.activities;
            //有可能获取不到
            if (activities!=null){
                for (int j = 0; j < activities.length; j++) {
                    ActivityInfo activity = activities[j];
                    String activityProcessName = activity.processName;
                    allProgressName.add(activityProcessName);
                }
            }
            //从包信息中获取服务(service)进程
            ServiceInfo[] services = packageInfo.services;
            if (services!=null){
                for (int j = 0; j < services.length; j++) {
                    ServiceInfo service = services[j];
                    String serviceProgressName = service.processName;
                    allProgressName.add(serviceProgressName);
                }
            }

            ProviderInfo[] providers = packageInfo.providers;
            if (providers!=null){
                for (int j = 0; j < providers.length; j++) {
                    ProviderInfo provider = providers[j];
                    String providerProgressName = provider.processName;
                    allProgressName.add(providerProgressName);
                }
            }

            ActivityInfo[] receivers = packageInfo.receivers;
            if (receivers!=null){
                for (int j = 0; j < receivers.length; j++) {
                    ActivityInfo receiver = receivers[j];
                    String receiverProgressName = receiver.processName;
                    allProgressName.add(receiverProgressName);
                }
            }
        }
        return allProgressName.size();
    }

    /**
     * @param context
     * @return 已用内存大小byte
     */
    public static long getAvailableMemory(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //getMemoryInfo调用就给availMem赋值
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    /**
     * @param context
     * @return 全部可用内存大小byte
     */
    public static long getTotalMemory(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //getMemoryInfo调用就给availMem赋值
        activityManager.getMemoryInfo(memoryInfo);
        //如果版本号>16才可用这个方法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return memoryInfo.totalMem;
        }else {
            //版本<16
            return getLowVersionMemory();
        }
    }

    /**
     * @return 16版本以下的手机内存大小byte
     */
    private static long getLowVersionMemory() {
        //安卓手机中proc/meminfo文件记录了手机全部可用内存的数据,低版本可已读取文件第一行获取
        File file = new File("proc/meminfo");
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(file));
            String lineNumber = bfr.readLine();
            String stringNum = lineNumber.replace("MemTotal:","").replace("kB","").trim();
            long totalMem = Long.parseLong(stringNum)*1024;
            return totalMem;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * @param context
     * @return 所要展示正在运行进程信息的bean集合
     */
    public static ArrayList<ProgressInfo> getRuningProgressInfo(Context context){
        ArrayList<ProgressInfo> progressInfos = new ArrayList<>();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager packageManager = context.getPackageManager();
        //根据activityManager获取到正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (int i = 0; i < runningAppProcesses.size(); i++) {
            //根据每一个正在运行的集合获取包名
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = runningAppProcesses.get(i);
            String processName = runningAppProcessInfo.processName;
            int pid = runningAppProcessInfo.pid;//pid:进程的标示id
            String name = "";
            Drawable drawable = null;
            boolean isSys = true;

            try {
                //用packageManager根据包名获取到Application配置文件的属性
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(processName, 0);
                //获取应用名
                name = applicationInfo.loadLabel(packageManager).toString();
                //获取应用图标
                drawable = applicationInfo.loadIcon(packageManager);
                //获取是否是系统进程
                if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)
                        == ApplicationInfo.FLAG_SYSTEM){
                    //application系统应用
                    isSys =  true;
                }else{
                    //用户应用
                    isSys = false;
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                //如果出异常获取不到
                name = processName;
                drawable = context.getResources().getDrawable(R.mipmap.heima);
            }
            //获取到每个应用所占内存
            int[] pids = {pid};
            Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(pids);
            Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
            long progressMemory = memoryInfo.getTotalPss() * 1024;
            ProgressInfo progressInfo = new ProgressInfo(drawable, name, progressMemory, isSys,false,processName);
            progressInfos.add(progressInfo);
        }
        return progressInfos;
    }

    /**
     * @param context
     * @param progressInfo 需要被杀死的进程
     */
    public static void killProgress(Context context,ProgressInfo progressInfo){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(progressInfo.getPackageName());
    }

    /**
     * @param context 杀死非本应用非系统应用的所有进程
     */
    public static void killAllProgress(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processeInfo:runningAppProcesses) {
            if (!processeInfo.processName.equals(context.getPackageName())){
                //不是本应用
                try {
                    ApplicationInfo applicationInfo = pm.getApplicationInfo(processeInfo.processName, 0);
                    if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)== ApplicationInfo.FLAG_SYSTEM){
                    }else {
                        //application不是系统应用
                        am.killBackgroundProcesses(processeInfo.processName);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
