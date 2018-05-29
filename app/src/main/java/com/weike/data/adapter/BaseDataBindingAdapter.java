package com.weike.data.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weike.data.WKBaseApplication;

import java.util.List;

/**
 * Created by LeoLu on 2018/5/29.
 */

public class BaseDataBindingAdapter<T> extends RecyclerView.Adapter<BaseDataBindingAdapter.BindingHolder> {
    protected Context context;
    protected LayoutInflater inflater;
    protected int layoutId;
    protected int variableId;
    protected List<T> list;


    protected OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    protected OnRecyclerViewItemLongClickListener onRecyclerViewItemLongClickListener;

    public BaseDataBindingAdapter(Context context) {
        this.context = context;
    }

    public BaseDataBindingAdapter(Context context, int layoutId, List<T> list, int resId) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
        this.variableId = resId;
        inflater = (LayoutInflater) WKBaseApplication.getInstance().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                layoutId,
                parent,
                false);

        BindingHolder holder = new BindingHolder(binding.getRoot(), onRecyclerViewItemClickListener, onRecyclerViewItemLongClickListener);
        holder.setBinding(binding);

        return holder;
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        holder.getBinding().setVariable(variableId, list.get(position));
        holder.getBinding().executePendingBindings();

    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.onRecyclerViewItemClickListener = listener;
    }

    public void setOnRecyclerViewItemLongClickListener(OnRecyclerViewItemLongClickListener listener) {
        this.onRecyclerViewItemLongClickListener = listener;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        protected ViewDataBinding binding;

        protected OnRecyclerViewItemClickListener itemClick;
        protected OnRecyclerViewItemLongClickListener itemLongClick;

        public BindingHolder(View itemView, OnRecyclerViewItemClickListener itemClick, OnRecyclerViewItemLongClickListener itemLongClick) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            this.itemClick = itemClick;
            this.itemLongClick = itemLongClick;
        }

        public ViewDataBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }

        @Override
        public void onClick(View v) {
            if (itemClick != null) {
                itemClick.onItemClick(getAdapterPosition(), v);

            }
        }


        @Override
        public boolean onLongClick(View v) {
            if (itemLongClick != null) {
                return itemLongClick.onItemLongClick(getAdapterPosition(), v);
            }

            return false;
        }
    }


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int position, View view);
    }

    public interface OnRecyclerViewItemLongClickListener {
        boolean onItemLongClick(int position, View view);
    }

}
