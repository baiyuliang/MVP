package com.byl.mvp.api.subscribe;

import com.byl.mvp.api.android.AndroidSchedulers;

import java.util.LinkedHashMap;

import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;


/**
 * @Title :
 * @Author : BaiYuliang
 * @Date :
 * @Desc :
 */
public class BaseSubscribe {
    /**
     * @param params
     * @return
     */
    public LinkedHashMap<String, String> appendParam(LinkedHashMap<String, String> params) {
        if (params == null) params = new LinkedHashMap<>();
        params.put("token", "");
        params.put("version", "");
        //...添加公共参数
        return params;
    }


    public static <T> ObservableTransformer<T, T> compose() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
