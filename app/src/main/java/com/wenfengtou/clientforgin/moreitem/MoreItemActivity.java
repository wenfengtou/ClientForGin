package com.wenfengtou.clientforgin.moreitem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wenfengtou.clientforgin.R;

import java.util.ArrayList;
import java.util.List;

public class MoreItemActivity extends AppCompatActivity {


    private RecyclerView rvRecyclerView;
    private MyRecyclerViewAdapter adapter;
    private List<Data> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_item);
        initView();
        initData();

        rvRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));//控制布局为LinearLayout或者是GridView或者是瀑布流布局
        adapter = new MyRecyclerViewAdapter(list,this);
        rvRecyclerView.setAdapter(adapter);
        // 设置item及item中控件的点击事件
        adapter.setOnItemClickListener(MyItemClickListener);
    }

    private void initView() {
        rvRecyclerView = (RecyclerView) findViewById(R.id.rv_recyclerView);
    }
    private void initData() {
        list = new ArrayList<>();
        list.add(new Data(1,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(2,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(1,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(2,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(1,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(2,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(1,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(2,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(1,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(2,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(1,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(2,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(1,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(2,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(1,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(2,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(1,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(2,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(1,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(2,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
        list.add(new Data(1,R.mipmap.ic_launcher, "username", "让我们成为好友吧！"));
    }

    /**
     * item＋item里的控件点击监听事件
     */
    private MyRecyclerViewAdapter.OnItemClickListener MyItemClickListener = new MyRecyclerViewAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View v, int position) {
            switch (v.getId()){
                case R.id.btn_agree:
                    Toast.makeText(MoreItemActivity.this,"你点击了同意按钮"+(position+1),Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_refuse:
                    Toast.makeText(MoreItemActivity.this,"你点击了拒绝按钮"+(position+1),Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(MoreItemActivity.this,"你点击了item按钮"+(position+1),Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onItemLongClick(View v) {

        }
    };

}
