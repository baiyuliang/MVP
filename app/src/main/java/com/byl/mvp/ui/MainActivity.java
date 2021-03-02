package com.byl.mvp.ui;


import androidx.recyclerview.widget.LinearLayoutManager;

import com.byl.mvp.App;
import com.byl.mvp.R;
import com.byl.mvp.databinding.ActivityMainBinding;
import com.byl.mvp.ui.base.BaseActivity;
import com.byl.mvp.ui.base.event.EventMsg;
import com.byl.mvp.ui.base.event.MsgCode;
import com.byl.mvp.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> {


    @Override
    public void initView() {
        StatusBarUtil.darkMode(this,false);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void initClick() {

    }

    @Override
    public void initData() {
        App.post(new EventMsg(MsgCode.LOGIN_SUCCESS));//发送消息

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("Item" + i);
        }
        ListAdapter listAdapter = new ListAdapter(mContext, list);
        vb.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        vb.mRecyclerView.setAdapter(listAdapter);
        
    }


}