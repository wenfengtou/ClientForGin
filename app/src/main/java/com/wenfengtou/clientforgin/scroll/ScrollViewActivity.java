package com.wenfengtou.clientforgin.scroll;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.wenfengtou.clientforgin.R;

public class ScrollViewActivity extends AppCompatActivity {

    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view);
        mWebView = findViewById(R.id.wv_content);
        mWebView.loadUrl("file:////android_asset/Hello.html");
    }
}
