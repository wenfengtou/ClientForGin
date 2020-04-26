package com.wenfengtou.clientforgin.moreitem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wenfengtou.clientforgin.R;

import java.util.ArrayList;
import java.util.List;

public class SingleListViewAdapter extends BaseAdapter {

    private List<Data> mData;//数据源
    private Context mContext;

    public SingleListViewAdapter(List<Data> data, Context context) {
        mData = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItemTwo holderItemTwo = null;
        if (convertView == null) {
            holderItemTwo = new ViewHolderItemTwo();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.itemtwo, null);
            holderItemTwo.ivIcon = convertView.findViewById(R.id.iv_icon);
            holderItemTwo.mRvPhoto = convertView.findViewById(R.id.rv_photo);
            convertView.setTag(holderItemTwo);
        } else {
            holderItemTwo = (ViewHolderItemTwo) convertView.getTag();
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        PhotoAdapter photoAdapter = new PhotoAdapter();
        holderItemTwo.mRvPhoto.setLayoutManager(layoutManager);
        holderItemTwo.mRvPhoto.setAdapter(photoAdapter);
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

        return convertView;
    }

    public  class ViewHolderItemTwo {
        public ImageView ivIcon;
        public RecyclerView mRvPhoto;
    }
}
