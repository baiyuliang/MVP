package com.byl.mvp.api.presenter;

/**
 *@Title :
 *@Author : BaiYuliang
 *@Date :
 *@Desc :
 */
public interface ViewRunnable<View extends BaseMvpView> {
    void run(View view);
}