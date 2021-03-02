package com.byl.mvp.api;

import com.byl.mvp.api.retrofit.RxErrorCallAdapterFactory;
import com.byl.mvp.api.retrofit.StringConverterFactory;
import com.byl.mvp.utils.LogUtil;
import com.hjq.gson.factory.GsonFactory;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.byl.mvp.api.URLConstant.BASE_URL;


public class ApiUtil {

    private static final int DEFAULT_CONNECT_TIMEOUT = 10;
    private static final int DEFAULT_WRITE_TIMEOUT = 10;
    private static final int DEFAULT_READ_TIMEOUT = 10;

    private OkHttpClient.Builder okHttpBuilder;
    private ApiService apiService;


    private static SSLSocketFactory createSSLSocketFactory() {

        SSLSocketFactory sSLSocketFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllManager()}, new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {

        }

        return sSLSocketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private ApiUtil() {
        okHttpBuilder = new OkHttpClient.Builder();
        //设置超时和重新连接
        okHttpBuilder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);
        okHttpBuilder.retryOnConnectionFailure(true);//错误重连
        //设置cookie（如果有需要可以添加下面代码）
//        PersistentCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(App.getContext()));
//        okHttpBuilder.cookieJar(cookieJar);
        //设置日志
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(LogUtil::i);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        okHttpBuilder.addInterceptor(interceptor);
        //设置https
        okHttpBuilder.sslSocketFactory(createSSLSocketFactory())
                .hostnameVerifier((s, sslSession) -> true);

        //添加header
        okHttpBuilder.addInterceptor(chain -> {
            Request original = chain.request();
            long timestamp = System.currentTimeMillis();
            Request request = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("appid", "")
                    .header("timestamp", String.valueOf(timestamp))
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        });

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpBuilder.build())
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonFactory.getSingletonGson()))//GsonFactory字段类型容错
                .addCallAdapterFactory(RxErrorCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        apiService = retrofit.create(ApiService.class);

    }

    private static class SingletonHolder {
        private static final ApiUtil INSTANCE = new ApiUtil();
    }

    //获取单例
    public static ApiUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public ApiService getApiService() {
        return apiService;
    }

}
