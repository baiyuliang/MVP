package com.byl.mvp.ui.base;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.byl.mvp.weight.RxView;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 注意：RecyclerView和item的最外层布局高度要设为wrap_content
 */
public abstract class BaseAdapter<VB extends ViewBinding, T> extends RecyclerView.Adapter<BaseViewHolder> {

    Activity mContext;
    List<T> listData;

    public BaseAdapter(Activity context, List<T> listData) {
        this.mContext = context;
        this.listData = listData;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class clazz = (Class) type.getActualTypeArguments()[0];
        try {
            Method method = clazz.getMethod("inflate", LayoutInflater.class);
            VB vb = (VB) method.invoke(null, mContext.getLayoutInflater());
            vb.getRoot().setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new BaseViewHolder(vb, vb.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        RxView.clicks(holder.vb.getRoot(), v -> {
            if (onItemClickListener != null) onItemClickListener.onItemClick(position);
        });
        RxView.longClicks(holder.vb.getRoot(), v -> {
            if (onItemLongClickListener != null) onItemLongClickListener.onItemLongClick(position);
            return true;
        });
        convert((VB) holder.vb, listData.get(position), position);
    }

    public abstract void convert(VB vb, T t, int position);

    @Override
    public int getItemCount() {
        return listData != null ? listData.size() : 0;
    }

    public OnItemClickListener onItemClickListener;//item点击事件
    public OnItemLongClickListener onItemLongClickListener;//item长按事件

    /**
     * item点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /**
     * item长按事件
     */
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    /**
     * 设置item点击事件
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置item长按事件
     *
     * @param onItemLongClickListener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
}
