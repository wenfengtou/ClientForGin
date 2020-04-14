package com.wenfengtou.clientforgin.moreitem;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoAdapterViewHolder> {


    @NonNull
    @Override
    public PhotoAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class PhotoAdapterViewHolder extends RecyclerView.ViewHolder {

        public PhotoAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }
}
