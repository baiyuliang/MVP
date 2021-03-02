package com.byl.mvp.api;


import com.byl.mvp.App;

/**
 * API URL相关.
 */
public class URLConstant {

    private static final String BASE_HTTP_DEBUG = "http://192.168.1.24:8080/api/";

    private static final String BASE_HTTP_RELEASE = "https://abc.def.com/api/";

    /**
     * 环境配置
     */
    public static final String BASE_URL = App.DEBUG ? BASE_HTTP_DEBUG : BASE_HTTP_RELEASE;

}
