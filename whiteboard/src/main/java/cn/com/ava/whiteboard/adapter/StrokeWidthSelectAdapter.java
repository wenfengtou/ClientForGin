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

public class StrokeWidthSelectAdapter extends RecyclerView.Adapter<StrokeWidthSelectAdapter.StrokeWidthSelectViewHolder>{

    ArrayList<StrokeWidthBean> mStrokeWidthBeanList;
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
        View root = LayoutInflater.from(mContext).inflate(R.layout.whiteboard_recycle_item_size, parent, false);
        return new StrokeWidthSelectViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull StrokeWidthSelectViewHolder holder, final int position) {
        if (mStrokeWidthBeanList != null && mStrokeWidthBeanList.size() > position) {
            StrokeWidthBean strokeWidthBean = mStrokeWidthBeanList.get(position);

            if (mCurrentStrokeWidth == strokeWidthBean.strokeWidth) {
                holder.mStrokeWidthIv.setImageResource(strokeWidthBean.pressedDrawableId);
            } else {
                holder.mStrokeWidthIv.setImageResource(strokeWidthBean.unPressedDrawableId);
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
        return mStrokeWidthBeanList == null ? 0 : mStrokeWidthBeanList.size();
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

    public void setPenStrokeWidthBeanList(ArrayList<StrokeWidthBean> strokeWidthBeanList) {
        mStrokeWidthBeanList = strokeWidthBeanList;
    }


    class StrokeWidthSelectViewHolder extends RecyclerView.ViewHolder {

        ImageView mStrokeWidthIv;

        public StrokeWidthSelectViewHolder(@NonNull View itemView) {
            super(itemView);
            mStrokeWidthIv = itemView.findViewById(R.id.iv_size);
        }

    }
}

