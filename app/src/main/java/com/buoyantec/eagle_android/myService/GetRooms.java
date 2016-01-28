package com.buoyantec.eagle_android.myService;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.buoyantec.eagle_android.API.MyService;
import com.buoyantec.eagle_android.model.Room;
import com.buoyantec.eagle_android.model.Rooms;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by kang on 16/1/27.
 * 获取所有机房
 * 参数: token, phone
 * 用户身份验证成功后调用
 * 返回值: 机房名称
 */
public class GetRooms {
    private String phone;
    private String token;
    private Context context;

    //初始化
    public GetRooms(Context c, String phone, String token){
        this.phone = phone;
        this.token = token;
        this.context = c;
    }

    /**
     * 如果用户身份合法,获取用户机房列表
     */
    public void getUserRooms() {
        //定义拦截器,添加headers
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

        // 建立http请求
        MyService myService = retrofit.create(MyService.class);
        Call<Rooms> call = myService.getRooms();
        // 发送请求(默认为异步加载)
        call.enqueue(new Callback<Rooms>() {
            @Override
            public void onResponse(Response<Rooms> response) {
                if (response.body() != null && response.code() == 200){
                    String result = "";
                    // 获得机房List
                    List<Room> roomList = response.body().getRooms();
                    // 遍历机房
                    Iterator<Room> rooms = roomList.iterator();
                    while(rooms.hasNext()) {
                        Room room = rooms.next();
                        result += (room.getId()+"");
                        result += '#';
                        result += room.getName();
                    }
                    SharedPreferences sp = context
                            .getSharedPreferences("foobar", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("room", result);
                    editor.apply();
                    System.out.println(">>>>>>>>>>获取机房成功>>>>>>>>>>>>");
                } else {
                    try {
                        String error = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(">>>>>>>>>>获取机房失败>>>>>>>>>>>>");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println(">>>>>>>>>>机房接口链接失败>>>>>>>>>>>>");
            }
        });
        System.out.println(">>>>>>>>>>获取机房执行完成>>>>>>>>>>>>");
    }
}
