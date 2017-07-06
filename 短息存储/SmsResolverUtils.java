package com.example.hasee.project_01_mobilesafe.activity.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.cardemulation.OffHostApduService;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.example.hasee.project_01_mobilesafe.activity.Bean.SmsInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by hasee on 2017/5/21.
 */

public class SmsResolverUtils {

    private static ArrayList<SmsInfo> smsInfoList;

    public static void saveSmsInfo(final Context context, final SmsCallBack smsCallBack){
        smsInfoList = new ArrayList<>();
        final ContentResolver contentResolver = context.getContentResolver();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//sd卡可用
            new Thread(){
                @Override
                public void run() {
                    Cursor cursor = contentResolver.query(Uri.parse("content://sms"), new String[]{"address", "read", "type", "body", "date"}, null, null, null);
                    int max = cursor.getCount();
                    //此时设置进度条最大值
                    smsCallBack.setProgressMax(max);
                    int count = 0;
                    Log.e("获取到:", max +"组数据");
                    while (cursor.moveToNext()){
                        String strAddress = cursor.getString(0);
                        String strBody = cursor.getString(3);
                        int intRead = cursor.getInt(1);
                        int intType = cursor.getInt(2);
                        long longDate = cursor.getLong(4);
                        smsInfoList.add(new SmsInfo(intType,intRead,strAddress,strBody,longDate));
                        count++;
                        //此时设置进度条当前进度
                        SystemClock.sleep(500);
                        smsCallBack.setProgressNow(count);
                    }
                    cursor.close();
                    //解析json
                    Gson gson = new Gson();
                    String json = gson.toJson(smsInfoList);
                    Log.e("getSmsInfo: ",json );
                    //保存json到本地
                    String path = Environment.getExternalStorageDirectory()+File.separator+"sms.json";
                    File file = new File(path);
                    FileWriter fw = null;
                    try {
                        fw = new FileWriter(file);
                        fw.write(json);
                        fw.flush();
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //存储成功展示
                                smsCallBack.showSaveSuccess();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("json存储失败 ","" );
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //存储失败展示
                                smsCallBack.showSaveFailure();
                            }
                        });
                    }finally {
                        if (fw!=null){
                            try {
                                fw.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }.start();
        }else{
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //sd卡不可用
                    Toast.makeText(context, "sd卡不可用", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    /**
     * 还原短信
     * @param context
     * @param smsCallBack
     */
    public static void getSmsInfo(final Context context, final SmsCallBack smsCallBack){
        smsInfoList = new ArrayList<>();
        final ContentResolver contentResolver = context.getContentResolver();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //sd卡可用
            String path = Environment.getExternalStorageDirectory()+File.separator+"sms.json";
            final File file =new File(path);
            if (file.length()>0){
                new Thread(){
                    @Override
                    public void run() {
                        //file文件有内容
                        BufferedReader bfr = null;
                        try {
                            bfr =  new BufferedReader(new FileReader(file));
                            String readLine = bfr.readLine();
                            //把strJson从新封装成数组
                            Gson gson = new Gson();
                            ArrayList<SmsInfo> smsInfoList = gson.fromJson(readLine, new TypeToken<ArrayList<SmsInfo>>() {}.getType());
                            Log.e("smsInfoList.size== ", smsInfoList.size()+"");
                            smsCallBack.setProgressMax(smsInfoList.size());
                            //遍历数组把集合中的数据用内容解析者从新插入到数据库中
                            ContentResolver resolver = context.getContentResolver();
                            ContentValues values = new ContentValues();
                            int count = 0;
                            for (SmsInfo smsInfo:smsInfoList) {
                                values.put("address",smsInfo.getAddress());
                                values.put("body",smsInfo.getBody());
                                values.put("date",smsInfo.getDate());
                                values.put("read",smsInfo.getRead());
                                values.put("type",smsInfo.getType());
                                resolver.insert(Uri.parse("content://sms"),values);
                                count++;
                                smsCallBack.setProgressNow(count);
                            }
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //插入成功展示
                                    smsCallBack.showSaveSuccess();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //插入失败展示
                                    smsCallBack.showSaveFailure();
                                }
                            });
                        }finally {
                            if (bfr!=null){
                                try {
                                    bfr.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }.start();

            }else {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //文件大小为0
                        Toast.makeText(context, "请先备份", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else{
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //sd卡不可用
                    Toast.makeText(context, "sd卡不可用", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public interface SmsCallBack{
        public void setProgressMax(int max);
        public void setProgressNow(int count);
        public void showSaveSuccess();
        public void showSaveFailure();

    }
}
