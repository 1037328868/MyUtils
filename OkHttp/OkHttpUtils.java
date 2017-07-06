package com.example.hasee.zhihuibeijing.utils;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by hasee on 2017/6/2.
 */

public class OkHttpUtils {
    public static Call getCall(String uriString){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(uriString).build();
        return okHttpClient.newCall(request);
    }
}
