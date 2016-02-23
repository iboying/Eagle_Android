package com.buoyantec.eagle_android.API;

import com.buoyantec.eagle_android.model.Alarm;
import com.buoyantec.eagle_android.model.Devices;
import com.buoyantec.eagle_android.model.Results;
import com.buoyantec.eagle_android.model.Rooms;
import com.buoyantec.eagle_android.model.MySystems;
import com.buoyantec.eagle_android.model.User;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    // 根据子系统获取设备
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("rooms/{id}/devices/search")
    Call<Devices> getDevices(@Path("id") Integer room_id,
                             @Field("sub_sys_name") String sub_sys_name);


    // 根据设备id获取设备信息 status: 200, 返回hashMap
    @Headers("Accept: application/json")
    @GET("rooms/{room_id}/devices/{id}")
    Call<HashMap<String, String>> getDeviceDataHash(@Path("room_id") Integer room_id,
                                                    @Path("id") Integer id);


    // 根据设备id获取设备告警列表 status: 200
    @Headers("Accept: application/json")
    @GET("devices/{device_id}/point_alarms")
    Call<Alarm> getWarnMessages(@Path("device_id") Integer device_id,
                                   @Query("checked") Integer checked);

    // 获取机房下子系统的告警数 status: 200
    @Headers("Accept: application/json")
    @POST("rooms/{room_id}/point_alarms/count")
    Call<Results> getSystemAlarmCount(@Path("room_id") Integer room_id);

    // 获取子系统下设备的告警数 status: 200
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("rooms/{room_id}/point_alarms/count")
    Call<Results> getDeviceAlarmCount(@Path("room_id") Integer room_id,
                                      @Field("sub_system_id") Integer sub_system_id);

}
