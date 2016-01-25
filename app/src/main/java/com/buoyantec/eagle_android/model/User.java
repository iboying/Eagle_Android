package com.buoyantec.eagle_android.model;

import com.buoyantec.eagle_android.API.UserService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by kang on 16/1/25.
 * 模型: User
 * 用途: 登录,用户信息
 */
public class User {

    private static final String BASE_URL = "http://139.196.190.201";

    // 让 Gson 自动将 API 中的下划线全小写式变量名转换成 Java 的小写开头驼峰式
    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    private static final UserService USER_SERVICE = retrofit.create(UserService.class);

    public static UserService userService() {
        return USER_SERVICE;
    }

}