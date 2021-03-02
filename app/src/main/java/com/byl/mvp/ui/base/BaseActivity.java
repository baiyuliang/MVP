package com.byl.mvp.ui.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
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

public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity implements BaseMvpView {
    public VB vb = null;
    private List<BasePresenter> mInjectPresenters;
    public FragmentActivity mContext;
    private ProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        mInjectPresenters = new ArrayList<>();

        try {
            //获取ViewBinding
            ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
            Class clazz = (Class) type.getActualTypeArguments()[0];
            Method method = clazz.getMethod("inflate", LayoutInflater.class);
            vb = (VB) method.invoke(null, getLayoutInflater());
            setContentView(vb.getRoot());
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
        } catch (Throwable e) {
            finish();
            return;
        }

        mContext = this;

        initView();
        initClick();
        initData();

        LogUtil.e(getClassName());
    }

    public abstract void initView();

    public abstract void initClick();

    public abstract void initData();

    public String getClassName() {
        String className = "BaseActivity";
        try {
            return getClass().getName();
        } catch (Exception e) {

        }
        return className;
    }

    protected void showLoading() {
        dismissLoading();
        if (isFinishing()) return;
        mLoadingDialog = new ProgressDialog(this);
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
    protected void onDestroy() {
        super.onDestroy();
        if (mInjectPresenters != null) {
            for (BasePresenter presenter : mInjectPresenters) {
                presenter.detachView();
            }
            mInjectPresenters.clear();
            mInjectPresenters = null;
        }
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        EventBus.getDefault().unregister(this);
    }

}
