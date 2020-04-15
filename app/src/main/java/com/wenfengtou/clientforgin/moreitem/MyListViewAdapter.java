package com.wenfengtou.clientforgin.moreitem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wenfengtou.clientforgin.R;

import java.util.ArrayList;
import java.util.List;

public class MyListViewAdapter extends BaseAdapter {

    private List<Data> mData;//数据源
    private Context mContext;

    public MyListViewAdapter(List<Data> data, Context context) {
        mData = data;
        mContext = context;
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).type;
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
        int type = getItemViewType(position);
        MyListViewAdapter.ViewHolderItemOne holderItemOne = null;
        MyListViewAdapter.ViewHolderItemTwo holderItemTwo = null;
        switch (type) {
            case Data.TYPE_ONE:
                if (convertView == null) {
                    holderItemOne = new ViewHolderItemOne();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.itemone, null);
                    holderItemOne.ivIcon = convertView.findViewById(R.id.iv_icon);
                    holderItemOne.tvUsername = convertView.findViewById(R.id.tv_username);
                    holderItemOne.tvMessage = convertView.findViewById(R.id.tv_message);
                    holderItemOne.btnAgree = convertView.findViewById(R.id.btn_agree);
                    holderItemOne.btnRefuse =convertView.findViewById(R.id.btn_refuse);
                    convertView.setTag(holderItemOne);
                } else {
                    holderItemOne = (ViewHolderItemOne) convertView.getTag();
                }
                break;

            case Data.TYPE_TWO:
                if (convertView == null) {
                    holderItemTwo = new ViewHolderItemTwo();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.itemtwo, null);
                    holderItemTwo.ivIcon = convertView.findViewById(R.id.iv_icon);
                    holderItemTwo.mRvPhoto = convertView.findViewById(R.id.rv_photo);
                    convertView.setTag(holderItemTwo);
                } else {
                    holderItemTwo = (ViewHolderItemTwo) convertView.getTag();
                }
            break;
        }


        switch (type) {
            case Data.TYPE_ONE:
                break;

            case Data.TYPE_TWO:
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
                break;
        }

        return convertView;
    }

    class ViewHolderItemOne {
        private ImageView ivIcon;
        private TextView tvUsername, tvMessage;
        private Button btnAgree, btnRefuse;
    }

    class ViewHolderItemTwo {
        public ImageView ivIcon;
        public RecyclerView mRvPhoto;
    }


}
