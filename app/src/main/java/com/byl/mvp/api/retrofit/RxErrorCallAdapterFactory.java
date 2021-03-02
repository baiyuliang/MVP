package com.byl.mvp.api.retrofit;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RxErrorCallAdapterFactory extends CallAdapter.Factory {

    private final RxJava2CallAdapterFactory original;

    private RxErrorCallAdapterFactory() {
        original = RxJava2CallAdapterFactory.create();
    }

    public static CallAdapter.Factory create() {
        return new RxErrorCallAdapterFactory();
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper(mHandler, retrofit, (CallAdapter<Observable<?>, Object>) original.get(returnType, annotations, retrofit));
    }

    public static class RxCallAdapterWrapper implements CallAdapter<Observable<?>, Object> {
        private final Retrofit retrofit;
        private final CallAdapter<Observable<?>, Object> wrapped;
        Handler handler;

        public RxCallAdapterWrapper(Handler handler, Retrofit retrofit, CallAdapter<Observable<?>, Object> wrapped) {
            this.handler = handler;
            this.retrofit = retrofit;
            this.wrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @Override
        public Object adapt(Call<Observable<?>> call) {
            return ((Observable) wrapped.adapt(call)).onErrorResumeNext(new Function<Throwable, ObservableSource>() {
                @Override
                public ObservableSource apply(@NonNull Throwable throwable) {
                    return Observable.error(asRetrofitException(throwable));
                }
            });
        }

        private RetrofitException asRetrofitException(Throwable throwable) {
            // We had non-200 http error
            if (throwable instanceof retrofit2.HttpException) {
                retrofit2.HttpException httpException = (retrofit2.HttpException) throwable;
                Response response = httpException.response();
                //如果API为Restful，可以这里全局捕获错误码
//                if (response.code() == 401) {  // TOKEN 失效
//                    handler.sendEmptyMessage(0);
//                }
//                if (response.code() == 520 || response.code() == 521) {// session 过期
//                    handler.sendEmptyMessage(0);
//                }
                return RetrofitException.httpError(response.raw().request().url().toString(), response, retrofit);
            }
            // A network error happened
            if (throwable instanceof IOException) {
                IOException ioException = (IOException) throwable;
                return RetrofitException.networkError((IOException) throwable);
            }
            // We don't know what happened. We need to simply convert to an unknown error
            return RetrofitException.unexpectedError(throwable);
        }
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //TODO 发送Event
        }
    };
}