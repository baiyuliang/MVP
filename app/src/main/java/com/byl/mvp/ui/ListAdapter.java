package com.byl.mvp.ui;

import android.app.Activity;

import com.byl.mvp.databinding.ItemListBinding;
import com.byl.mvp.ui.base.BaseAdapter;
import com.byl.mvp.ui.base.BaseViewHolder;

import java.util.List;

public class ListAdapter extends BaseAdapter<ItemListBinding, String> {

    public ListAdapter(Activity context, List<String> listData) {
        super(context, listData);
    }

    @Override
    public void convert(ItemListBinding vb, String t, int position) {
        vb.tvContent.setText(t);
    }

}
