package com.byl.mvp;

import android.app.Application;

import com.byl.mvp.ui.base.event.EventMsg;

import org.greenrobot.eventbus.EventBus;

public class App extends Application {
    public static boolean DEBUG = false;
    private static App INSTANCE;

    public static App getContext() {
        return INSTANCE;
    }

    public static void post(EventMsg eventMsg) {
        EventBus.getDefault().post(eventMsg);
    }

    public static void postSticky(EventMsg eventMsg) {
        EventBus.getDefault().postSticky(eventMsg);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        setDebug(true);//控制正式环境和测试环境
    }

    public static void setDebug(boolean DEBUG) {
        App.DEBUG = DEBUG;
    }


}
