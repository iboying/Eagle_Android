package com.buoyantec.eagle_android.myService;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.buoyantec.eagle_android.API.MyService;
import com.buoyantec.eagle_android.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.Response;

/**
 * Created by kang on 16/1/28.
 * 获取用户信息
 * 参数: phone, password
 * 返回值: void
 */
public class ApiUser {
    private String phone;
    private String password;
    private SharedPreferences sp;


    public ApiUser(Context context, String phone, String password) {
        this.phone = phone;
        this.password = password;
        sp = context.getSharedPreferences("foobar", Activity.MODE_PRIVATE);
    }

    public void getUser(){
        // 创建Retrofit实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://139.196.190.201/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 创建所有链接
        MyService myService = retrofit.create(MyService.class);

        // 获取指定链接数据
        Call<User> call = myService.getUser(phone, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response) {
                if (response.code() == 201) {
                    // 获取用户
                    User user = response.body();
                    // 存入SharedPreferences
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("id", user.getId());
                    editor.putString("name", user.getName());
                    editor.putString("email", user.getEmail());
                    editor.putString("token", user.getAuthenticationToken());
                    editor.putString("phone", user.getPhone());
                    editor.putString("password", password);
                    editor.putInt("user_status_code", response.code());
                    editor.apply();
                } else {
                    // 输出非201时的错误信息
                    System.out.println(">>>>>>>>>>User接口状态错误>>>>>>>>>>>>");
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("user_status_code", response.code());
                    editor.apply();
                    System.out.println(">>>>>>>>>>User接口状态错误>>>>>>>>>>>>");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println(">>>>>>>>>>User接口未成功链接>>>>>>>>>>>>");
                //// TODO: 16/1/28  错误处理
            }
        });
        System.out.println(">>>>>>>>>>User接口调用完成>>>>>>>>>>>>");
    }
}
