package com.buoyantec.eagle_android.API;

import com.buoyantec.eagle_android.model.Rooms;
import com.buoyantec.eagle_android.model.MySystems;
import com.buoyantec.eagle_android.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by kang on 16/1/25.
 * service: http://139.196.190.201
 */
public interface MyService {
    // 获取用户信息 status: 201
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("users/sign_in")
    //@Field提交的域
    Call<User> getUser(@Field("user[phone]") String phone,
                       @Field("user[password]") String password);

    // 获取机房列表 status: 200
    @Headers("Accept: application/json")
    @GET("rooms")
    Call<Rooms> getRooms();

    // 获取系统列表 status: 200
    @Headers("Accept: application/json")
    @GET("systems")
    Call<MySystems> getSystems();

}
