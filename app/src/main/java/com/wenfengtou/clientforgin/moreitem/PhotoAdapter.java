package com.wenfengtou.clientforgin.moreitem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wenfengtou.clientforgin.R;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoAdapterViewHolder> {

    private  ArrayList<String> mData;

    @NonNull
    @Override
    public PhotoAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent,false);
        return new PhotoAdapterViewHolder(rootView);
    }

    public void setData(ArrayList<String> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoAdapterViewHolder holder, int position) {
        holder.mTvName.setText(" " + mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class PhotoAdapterViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIvPhoto;
        public TextView mTvName;
        public PhotoAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvPhoto = itemView.findViewById(R.id.iv_photo);
            mTvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
