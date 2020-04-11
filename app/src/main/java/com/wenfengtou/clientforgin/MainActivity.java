package com.wenfengtou.clientforgin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
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
import java.util.Random;

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

        b2tv = findViewById(R.id.textView2);
        b2tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });
        int a = b2tv.getVisibility();

        try {
            //serializeStudent();
            deserializeStudent();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int id = new Random().nextInt(100);
        MyApplication.getInstance().getDaoSession().getNoteDao().insert(new Note(id, "wf-comment" + id));

        List<Note> notes = MyApplication.getInstance().getDaoSession().getNoteDao().loadAll();
        for (Note note : notes) {
            Log.i("wenfengtou", "get note" + note.getComment());
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



    ObjectAnimator mAlphaAnimation;
    private void startAlphaAnimator(View view) { // ImageView的alpha渐变后再变回来
        mAlphaAnimation = ObjectAnimator.ofFloat(view, "alpha", 1f, 1f);
        mAlphaAnimation.setDuration(0);
        mAlphaAnimation.setRepeatCount(0);
        mAlphaAnimation.setRepeatMode(ValueAnimator.RESTART);
        mAlphaAnimation.setStartDelay(0);
        mAlphaAnimation.setInterpolator(new LinearInterpolator());
        mAlphaAnimation.start();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("wenfengtou", "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("wenfengtou", "onRestoreInstanceState");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //startService(new Intent(MainActivity.this, MusicPlayerService.class));
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
        //stopService(new Intent(MainActivity.this, MusicPlayerService.class));
       // mHandler.removeCallbacksAndMessages(null);
    }
}
