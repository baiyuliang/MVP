package com.byl.mvp.ui.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public class BaseViewHolder<VB extends ViewBinding> extends RecyclerView.ViewHolder {

    public VB vb;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public BaseViewHolder(VB vb, View itemView) {
        super(itemView);
        this.vb = vb;
    }

}
