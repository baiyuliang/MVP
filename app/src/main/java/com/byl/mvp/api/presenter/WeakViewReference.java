package com.byl.mvp.api.presenter;

import java.lang.ref.WeakReference;

/**
 * @Title :
 * @Author : BaiYuliang
 * @Date :
 * @Desc :
 */
public class WeakViewReference<View extends BaseMvpView> extends WeakReference<View> {
    public WeakViewReference(View r) {
        super(r);
    }

    public void safeExecute(ViewRunnable<View> runnable) {
        View view = get();
        if (view != null) {
            runnable.run(view);
        }
    }
}