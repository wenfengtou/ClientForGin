package com.wenfengtou.clientforgin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private Handler mHandler;
    private TextView b1tv;
    private TextView b2tv;
    class OkHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            sendEmptyMessageDelayed(1,1000);
            Log.i("wenfengtou", "handleMessage");
        }
    }

    private boolean toggle = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b2tv = findViewById(R.id.b2);
        b1tv = findViewById(R.id.b1);
        b2tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggle) {
                    toggle = false;
                    b1tv.setVisibility(View.VISIBLE);
                } else {
                    toggle = true;
                    b1tv.setVisibility(View.GONE);
                }

            }
        });


        try {
            //serializeStudent();
            deserializeStudent();
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*
        OkGo.<String>get("http://192.168.43.125:8000")                            // 请求方式和请求url
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey("cacheKey")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.NO_CACHE)    // 缓存模式，详细请看缓存介绍
                //  .cacheTime(3000)//缓存时间
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("fengtou",response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Log.i("fengtou","error" + response.body());
                        super.onError(response);
                    }
                });
         */
    }


    private static void serializeStudent() throws IOException {
        Student student = new Student("ze",15 ,"zq");
        // ObjectOutputStream 对象输出流，将 CarBean 对象存储到E盘的 CarBean.txt 文件中，完成对 CarBean 对象的序列化操作
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("/sdcard/Student.txt")));
        oos.writeObject(student);
        Log.i("wenfengtou","Student 对象序列化成功");
        oos.close();
    }

    /**
     * 反序列化
     */
    private static Student deserializeStudent() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("/sdcard/Student.txt")));
        Student student = (Student) ois.readObject();
        Log.i("wenfengtou", "Student 对象反序列化成功 " + student );
        return student;
    }


        @Override
    protected void onDestroy() {
        super.onDestroy();
       // mHandler.removeCallbacksAndMessages(null);
    }
}
