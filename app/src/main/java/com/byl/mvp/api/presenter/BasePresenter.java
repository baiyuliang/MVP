package com.byl.mvp.api.presenter;


/**
 *@Title :
 *@Author : BaiYuliang
 *@Date :
 *@Desc :
 */
public class BasePresenter<T extends BaseMvpView> implements Presenter<T> {

    public Disposables disposables;
    private WeakViewReference<T> mWeakViewReference;


    @Override
    public void attachView(T mvpView) {
        disposables = new Disposables();
        mWeakViewReference = new WeakViewReference<>(mvpView);
    }

    @Override
    public void detachView() {
        disposables.dispose();
        mWeakViewReference = null;
    }

    public WeakViewReference<T> getMvpView() {
        return mWeakViewReference;
    }

}

