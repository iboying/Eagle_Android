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
    private Engine engine;
    private Engine loginEngine;
    private String token;
    private String phone;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        setLoginEngine();
        setEngine();
    }

    /**
     * 应用
     * @return
     */
    public static App getInstance() {
        return sInstance;
    }

    /**
     * 返回带header的链接
     * @return
     */
    public Engine getEngine() {
        return engine;
    }

    /**
     * 返回登录链接
     * @return
     */
    public Engine getLoginEngine() {
        return loginEngine;
    }

    //
    private void setLoginEngine() {
        loginEngine = new Retrofit.Builder()
                .baseUrl("http://139.196.190.201/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Engine.class);
    }

    private void setEngine() {
        // 获取token和phone
        SharedPreferences sp = sInstance.getApplicationContext()
                .getSharedPreferences("foobar", Context.MODE_PRIVATE);
        token = sp.getString("token", null);
        phone = sp.getString("phone", null);

        // 定义拦截器,添加headers
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("X-User-Token", token)
                        .addHeader("X-User-Phone", phone)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        // 创建Retrofit实例
        engine = new Retrofit.Builder()
                .baseUrl("http://139.196.190.201/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build().create(Engine.class);
    }
}
