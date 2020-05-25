package com.wenfengtou.whiteboard.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.wenfengtou.whiteboard.R;

import java.util.ArrayList;

public class PenColorSelectAdapter extends RecyclerView.Adapter<PenColorSelectAdapter.ColorSelectViewHolder>{

    ArrayList<PenColorBean> mPenColorBeanList;
    private int mCurrentColor;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public ColorSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View root = LayoutInflater.from(mContext).inflate(R.layout.layout_pen_color_item, parent, false);
        return new ColorSelectViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorSelectViewHolder holder, final int position) {
        if (mPenColorBeanList != null && mPenColorBeanList.size() > position) {
            PenColorBean penColorBean = mPenColorBeanList.get(position);
            holder.mColorIv.setImageResource(penColorBean.drawableId);
            if (mCurrentColor == penColorBean.color) {
                holder.mColorIv.setBackground(mContext.getDrawable(R.drawable.rectangle_frame_blue));
            } else {
                holder.mColorIv.setBackground(null);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mPenColorBeanList == null ? 0 : mPenColorBeanList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setCurrentColor(int color) {
        mCurrentColor = color;
        notifyDataSetChanged();
    }

    public int getCurrentColor() {
        return mCurrentColor;
    }

    public void setPenColorBeanList(ArrayList<PenColorBean> penColorBeanList) {
        mPenColorBeanList = penColorBeanList;
    }


    class ColorSelectViewHolder extends ViewHolder {

        ImageView mColorIv;

        public ColorSelectViewHolder(@NonNull View itemView) {
            super(itemView);
            mColorIv = itemView.findViewById(R.id.iv_color);
        }

    }
}
