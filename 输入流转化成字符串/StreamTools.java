package com.example.urltest3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2017/4/22 0022.
 * 流的工具类
 */
public class StreamTools {
    /**
     * 把流转化为字符串
     * @param is
     * @return
     */
    public static String getStringByInputStream(InputStream is) throws IOException {
        if(is!=null){
            //读输入流
            BufferedReader reader=new BufferedReader(new InputStreamReader(is));
            StringBuilder sb=new StringBuilder("");
            //临时字符串
            String tempStr=null;
            while((tempStr=reader.readLine())!=null){
                sb.append(tempStr);
            }
            reader.close();
            return  sb.toString();
        }
        return  null;
    }
}
