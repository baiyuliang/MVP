package com.byl.mvp.api.presenter;

/**
 * @Title :
 * @Author : BaiYuliang
 * @Date :
 * @Desc :
 */
public interface Presenter<V extends BaseMvpView> {

    void attachView(V mvpView);

    void detachView();
}
