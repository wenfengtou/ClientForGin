package com.wenfengtou.clientforgin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

public class Main2Activity extends Activity implements View.OnClickListener {

    TextView mHeadTv;
    TextView mTailTv;
    NestedScrollView mNestedScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mHeadTv = findViewById(R.id.tv_head);
        mTailTv = findViewById(R.id.tv_tail);
        mNestedScrollView = findViewById(R.id.main_nest_view);
        mTailTv.setOnClickListener(this);
        AndPermission.with(this)
                .runtime()
                .permission(Permission.CAMERA)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> strings) {

                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> strings) {

                    }
                })
                .start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head:
                break;
            case R.id.tv_tail:
                //mNestedScrollView.smoothScrollTo(0, 500);
                //mNestedScrollView.scrollTo(0, 500);
                mNestedScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mNestedScrollView.smoothScrollTo(0, 0);
                    }
                });

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
