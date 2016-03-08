package com.buoyantec.eagle_android.myService;

import android.content.Context;
import android.content.SharedPreferences;

import com.buoyantec.eagle_android.engine.Engine;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by kang on 16/2/25.
 * 统一请求类
 * 添加 token,phone 到header
 * 返回: Engine 实例
 */
public class ApiRequest {
    private Context context;
    private String token;
    private String phone;

    /**
     * 构造函数,传入context
     * @param c
     */
    public ApiRequest(Context c) {
        this.context = c;
    }

    /**
     * 构造函数
     * 无参数
     */
    public ApiRequest() {}

    /**
     * 应用中的服务
     * header: token, phone
     * @return myService
     */
    public Engine getService() {
        // 获取token和phone
        SharedPreferences sp = context.getSharedPreferences("foobar", Context.MODE_PRIVATE);
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://139.196.190.201/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(Engine.class);
    }

    /**
     * 登录时的服务
     * header: null
     */
    public Engine getLoginService() {
        // 创建登录Retrofit实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://139.196.190.201/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(Engine.class);
    }
}
