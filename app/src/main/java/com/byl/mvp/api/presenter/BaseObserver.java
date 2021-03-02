package com.byl.mvp.api.presenter;

import android.text.TextUtils;

import com.byl.mvp.api.HttpResult;
import com.byl.mvp.utils.LogUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Title :
 * @Author : BaiYuliang
 * @Date :
 * @Desc :
 */
public abstract class BaseObserver<T> implements Observer<T> {

    Disposables disposables;

    public BaseObserver(Disposables disposables) {
        if (disposables == null) {
            disposables = new Disposables();
        }
        this.disposables = disposables;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposables.add(d);
    }

    @Override
    public void onNext(T value) {
        if (value instanceof HttpResult) {
            if (((HttpResult) value).errorCode == 1) {
                onSuccess(value);
            } else {
                onFailure(((HttpResult) value).errorCode, ((HttpResult) value).errorMessage);
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        if (e != null && !TextUtils.isEmpty(e.getMessage())) {
            onFailure(-1, e.getMessage());
            LogUtil.e(e.getMessage());
        } else {
            onFailure(-1, "网络连接失败，请稍后再试");
        }

    }

    @Override
    public void onComplete() {

    }

    protected abstract void onSuccess(T t);

    protected abstract void onFailure(int error_code, String error_msg);

}
