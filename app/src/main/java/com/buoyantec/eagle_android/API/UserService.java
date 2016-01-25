package com.buoyantec.eagle_android.API;

import com.buoyantec.eagle_android.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by kang on 16/1/25.
 * api: http://139.196.190.201/users/sign_in
 * params: user[phone],user[password]
 * header: Accept: application/json
 */
public interface UserService {

    //通过注解设置请求头
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("/users/sign_in")
    //@Field提交的域
    Call<User> getUser(@Field("user[phone]") String phone,
                       @Field("user[password]") String password);

}
