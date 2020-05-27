package cn.com.ava.whiteboard.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import cn.com.ava.whiteboard.R;

public class PenStrokeWidthSelectAdapter extends RecyclerView.Adapter<PenStrokeWidthSelectAdapter.StrokeWidthSelectViewHolder>{

    ArrayList<PenStrokeWidthBean> mPenStrokeWidthBeanList;
    private int mCurrentStrokeWidth;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public StrokeWidthSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View root = LayoutInflater.from(mContext).inflate(R.layout.layout_size_item, parent, false);
        return new StrokeWidthSelectViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull StrokeWidthSelectViewHolder holder, final int position) {
        if (mPenStrokeWidthBeanList != null && mPenStrokeWidthBeanList.size() > position) {
            PenStrokeWidthBean penStrokeWidthBean = mPenStrokeWidthBeanList.get(position);

            if (mCurrentStrokeWidth == penStrokeWidthBean.strokeWidth) {
                holder.mStrokeWidthIv.setImageResource(penStrokeWidthBean.pressedDrawableId);
            } else {
                holder.mStrokeWidthIv.setImageResource(penStrokeWidthBean.unPressedDrawableId);
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
        return mPenStrokeWidthBeanList == null ? 0 : mPenStrokeWidthBeanList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setCurrentStrokeWidth(int size) {
        mCurrentStrokeWidth = size;
        notifyDataSetChanged();
    }

    public int getCurrentStrokeWidth() {
        return mCurrentStrokeWidth;
    }

    public void setPenStrokeWidthBeanList(ArrayList<PenStrokeWidthBean> penStrokeWidthBeanList) {
        mPenStrokeWidthBeanList = penStrokeWidthBeanList;
    }


    class StrokeWidthSelectViewHolder extends RecyclerView.ViewHolder {

        ImageView mStrokeWidthIv;

        public StrokeWidthSelectViewHolder(@NonNull View itemView) {
            super(itemView);
            mStrokeWidthIv = itemView.findViewById(R.id.iv_size);
        }

    }
}

