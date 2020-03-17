package com.wenfengtou.clientforgin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        List<File> mFileList = new ArrayList<>();
        mFileList.add(new File("/sdcard/haha3.txt"));
        mFileList.add(new File("/sdcard/haha2.txt"));
        mFileList.add(new File("/sdcard/haha1.txt"));
        OkGo.<String>post("http://192.168.43.125:8000/upload")
                .tag(this)
                .params("hehe", "nimeide")
                .addFileParams("upload-key", mFileList)
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
    }
}
