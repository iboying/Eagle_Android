package com.buoyantec.iGrid;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.buoyantec.iGrid.engine.Engine;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by kang on 16/3/3.
 * 自定义application
 */
public class App extends Application {
    private static App sInstance;
//    private Engine engine;
    private Engine loginEngine;
//    private String token;
//    private String phone;

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
