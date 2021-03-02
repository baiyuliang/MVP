package com.byl.mvp.api;

import com.byl.mvp.ui.login.model.UserBean;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    //登录
    @FormUrlEncoded
    @POST("Account/Login")
    Observable<HttpResult<UserBean>> login(@FieldMap HashMap<String, String> options);

    //参数如果为json格式，则使用Body
    @FormUrlEncoded
    @POST("Account/Login")
    Observable<HttpResult<UserBean>> login2(@Body HashMap<String, String> options);
}
