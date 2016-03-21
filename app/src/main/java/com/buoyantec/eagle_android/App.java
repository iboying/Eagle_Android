package com.buoyantec.eagle_android;

import android.app.Application;

import com.buoyantec.eagle_android.engine.Engine;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by kang on 16/3/3.
 * 自定义application
 */
public class App extends Application {
    private static App sInstance;
    private Engine loginEngine;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        setLoginEngine();
    }

    /**
     * 应用
     */
    public static App getInstance() {
        return sInstance;
    }

    /**
     * 返回登录链接
     */
    public Engine getLoginEngine() {
        return loginEngine;
    }

    // 设置登录链接
    private void setLoginEngine() {
        loginEngine = new Retrofit.Builder()
                .baseUrl("http://139.196.190.201/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Engine.class);
    }
}
