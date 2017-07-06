package com.example.hasee.xmlapp;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateXML extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_xml);
    }

    /**
     * 普通io流生成xml文件
     */
    public void click1(View v){
        FileOutputStream fos=null;
        try {
            //关联文件
            File file = new File(Environment.getExternalStorageDirectory().getPath(),"testxml1.xml");
            //1创建一个输出流把结果输出到xml文件
            fos = new FileOutputStream(file);
            //创建一个Stringbuff拼接文本
            StringBuffer sb = new StringBuffer();
            //3一步一步进行拼接
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            sb.append("<smss>");
            sb.append("<sms>");
            sb.append("<address>110</address>");
            sb.append("<body>nihao</body>");
            sb.append("<date>2017</date> ");
            sb.append("</sms> ");
            sb.append("<sms> ");
            sb.append("<address>110</address>");
            sb.append("<body>nihao</body>");
            sb.append("<date>2017</date> ");
            sb.append("</sms> ");
            sb.append("</smss>");
            //4把拼接完成的输出
            fos.write(sb.toString().getBytes());
            System.out.println(sb.toString());
            Toast.makeText(CreateXML.this,"XML1生成成功",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(CreateXML.this,"XML1生成失败",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            //5关闭输出流
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 用android提供的类生成xml文档
     */
    public void click2(View v){
        FileOutputStream fos = null;
        try {
            //1获取官方提供的xml工具序列化实例
            XmlSerializer xmlSerializer = Xml.newSerializer();
            //2关联位置
            File file = new File(Environment.getExternalStorageDirectory().getPath(),"testxml2.xml");
            fos = new FileOutputStream(file);
            //3用工具提供的方法生成xml文件
            //告诉序列化工具生成一个xml文件,位置
            xmlSerializer.setOutput(fos,"utf-8");
            //3.1生成开头结尾
            xmlSerializer.startDocument("utf-8",true);
            //3.2生成根标签开头结尾
            xmlSerializer.startTag(null,"smss");
            //3.3生成各个标签开头结尾
            for (int i=0;i<2;i++) {
                //头
                xmlSerializer.startTag(null,"sms");
                xmlSerializer.startTag(null,"address");
                xmlSerializer.text("110");
                xmlSerializer.endTag(null,"address");
                xmlSerializer.startTag(null,"body");
                xmlSerializer.text("nihao");
                xmlSerializer.endTag(null,"body");
                xmlSerializer.startTag(null,"date");
                xmlSerializer.text("2017");
                xmlSerializer.endTag(null,"date");
                //尾
                xmlSerializer.endTag(null,"sms");
            }
            xmlSerializer.endTag(null,"smss");
            xmlSerializer.endDocument();
            Toast.makeText(CreateXML.this,"XML2生成成功",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(CreateXML.this,"XML2生成失败",Toast.LENGTH_LONG).show();
            e.printStackTrace();
       } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
