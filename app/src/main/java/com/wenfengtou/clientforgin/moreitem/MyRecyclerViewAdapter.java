package com.wenfengtou.clientforgin.moreitem;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wenfengtou.clientforgin.R;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Data> list;//数据源
    private Context context;//上下文

    public MyRecyclerViewAdapter(List<Data> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case Data.TYPE_ONE:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemone,parent,false);
                return new OneViewHolder(view);
            case Data.TYPE_TWO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemtwo,parent,false);
                return new TwoViewHolder(view);
        }
        return null;
    }

    //绑定
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    //有多少个item？
    @Override
    public int getItemCount() {
        return list.size();
    }

    //item类型
    @Override
    public int getItemViewType(int position) {
        return list.get(position).type;
    }

    //=======================以下为item中的button控件点击事件处理===================================

    //第一步：自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener  {
        void onItemClick(View v, int position);
        void onItemLongClick(View v);
    }

    public OnItemClickListener mOnItemClickListener;//第二步：声明自定义的接口

    //第三步：定义方法并暴露给外面的调用者
    public void setOnItemClickListener(OnItemClickListener  listener) {
        this.mOnItemClickListener  = listener;
    }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    class OneViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivIcon;
        private TextView tvUsername, tvMessage;
        private Button btnAgree, btnRefuse;
        //private List<Data> list;
        public OneViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvMessage = itemView.findViewById(R.id.tv_message);
            btnAgree = itemView.findViewById(R.id.btn_agree);
            btnRefuse = itemView.findViewById(R.id.btn_refuse);
            // 为item及item内部控件添加点击事件
            itemView.setOnClickListener(this);
            btnAgree.setOnClickListener(this);
            btnRefuse.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    class TwoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivIcon;
        private RecyclerView mRvPhoto;
        public TwoViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            mRvPhoto = itemView.findViewById(R.id.rv_photo);
            // 为item添加点击事件
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }


}