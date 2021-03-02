package com.byl.mvp.ui.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;


import com.byl.mvp.api.presenter.BaseMvpView;
import com.byl.mvp.api.presenter.BasePresenter;
import com.byl.mvp.ui.base.event.EventMsg;
import com.byl.mvp.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseFragment<VB extends ViewBinding> extends Fragment implements BaseMvpView {
    public VB vb = null;
    private List<BasePresenter> mInjectPresenters;
    protected Activity mContext;
    public View contentView;
    private ProgressDialog mLoadingDialog;
    private boolean isViewCreated;
    private boolean isUIVisible;
    public boolean isVisibleToUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        EventBus.getDefault().register(this);
        mInjectPresenters = new ArrayList<>();
        try {
            //获取Presenter
            Field[] fields = getClass().getDeclaredFields();
            for (Field field : fields) {
                //获取注解的Presenter
                InjectPresenter injectPresenter = field.getAnnotation(InjectPresenter.class);
                if (injectPresenter != null) {
                    //得到注解的Presenter，并执行attachView方法，并添加进mInjectPresenters
                    Class<? extends BasePresenter> fieldType = (Class<? extends BasePresenter>) field.getType();
                    BasePresenter mInjectPresenter = fieldType.newInstance();
                    mInjectPresenter.attachView(this);
                    field.setAccessible(true);
                    field.set(this, mInjectPresenter);
                    mInjectPresenters.add(mInjectPresenter);
                }
            }
        } catch (Exception e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
            Class clazz = (Class) type.getActualTypeArguments()[0];
            try {
                Method method = clazz.getMethod("inflate", LayoutInflater.class);
                vb = (VB) method.invoke(null, getLayoutInflater());
                contentView = vb.getRoot();
            } catch (Exception e) {
                e.printStackTrace();
            }
            initRefresh();
            initRecyclerView();
            initView();
            initClick();
            initData();
            LogUtil.e(getClass().getName());
        }
        return contentView;
    }

    public abstract void initView();

    public abstract void initClick();

    public abstract void initData();

    public abstract void lazyLoadData(); //需要懒加载的数据，重写此方法


    public String getClassName() {
        String className = "BaseFragment";
        try {
            return getClass().getName();
        } catch (Exception e) {

        }
        return className;
    }

    public void initRefresh() {

    }

    public void initRecyclerView() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    private void lazyLoad() {
        if (isViewCreated && isUIVisible) {
            lazyLoadData();
            isViewCreated = false;
            isUIVisible = false;
        }
    }

    protected void showLoading() {
        dismissLoading();
        mLoadingDialog = new ProgressDialog(mContext);
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.show();
    }

    public void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = null;
    }

    /**
     * 统一消息处理
     *
     * @param eventMsg
     */
    @Subscribe
    public void onEventMainThread(EventMsg eventMsg) {
        handleEventMsg(eventMsg);
    }

    public void handleEventMsg(EventMsg eventMsg) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        contentView = null;
        if (mInjectPresenters != null) {
            for (BasePresenter presenter : mInjectPresenters) {
                presenter.detachView();
            }
            mInjectPresenters.clear();
            mInjectPresenters = null;
        }
        EventBus.getDefault().unregister(this);
    }
}
