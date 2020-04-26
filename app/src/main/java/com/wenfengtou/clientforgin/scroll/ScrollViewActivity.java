package com.wenfengtou.clientforgin.scroll;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.wenfengtou.clientforgin.R;
import com.wenfengtou.clientforgin.moreitem.PhotoAdapter;

import java.util.ArrayList;

public class ScrollViewActivity extends AppCompatActivity {

    WebView mWebView;
    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view);
        mWebView = findViewById(R.id.wv_content);
        mRecyclerView = findViewById(R.id.rv_scroll);
        mWebView.loadUrl("file:////android_asset/Hello.html");
        PhotoAdapter photoAdapter = new PhotoAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        ArrayList<String> data = new ArrayList<String>();
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("4");
        data.add("5");
        data.add("6");
        data.add("7");
        data.add("8");
        data.add("9");
        data.add("10");
        data.add("11");
        data.add("12");
        data.add("13");
        data.add("14");
        data.add("15");
        data.add("16");
        data.add("17");
        photoAdapter.setData(data);
        mRecyclerView.setAdapter(photoAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);

        //mWebView.setNestedScrollingEnabled(false);
    }
}
