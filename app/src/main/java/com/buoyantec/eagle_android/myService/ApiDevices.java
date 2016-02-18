package com.buoyantec.eagle_android.myService;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.buoyantec.eagle_android.API.MyService;
import com.buoyantec.eagle_android.R;
import com.buoyantec.eagle_android.model.Device;
import com.buoyantec.eagle_android.model.Devices;

import java.io.IOException;
import java.util.ArrayList;
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
 * Created by kang on 16/2/7.
 * 根据子系统名称获取设备列表
 */
public class ApiDevices {
    private SharedPreferences sp;
    private Integer room_id;
    private String sub_sys_name;
    private Context context;

    private String[] texts;
    private Integer[][] datas;

    public ApiDevices(Context c, Integer room_id, String sub_sys_name) {
        this.room_id = room_id;
        this.sub_sys_name = sub_sys_name;
        this.context = c;
        sp = context.getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        getDevices();
    }

    private void getDevices(){
        //定义拦截器,添加headers
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("X-User-Token", sp.getString("token", ""))
                        .addHeader("X-User-Phone", sp.getString("phone", ""))
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

        // 创建所有链接
        MyService myService = retrofit.create(MyService.class);

        // 获取指定链接数据
        Call<Devices> call = myService.getDevices(room_id, sub_sys_name);
        call.enqueue(new Callback<Devices>() {
            @Override
            public void onResponse(Response<Devices> response) {
                if (response.code() == 200) {
                    ArrayList<String> device_name = new ArrayList<>();
                    // 获取用户
                    List<Device> devices = response.body().getDevices();
                    Iterator<Device> itr = devices.iterator();
                    while (itr.hasNext()) {
                        Device device = itr.next();
                        device_name.add(device.getName());
                    }
                    // texts of images
                    texts = device_name.toArray(new String[device_name.size()]);
                    // UPS数据
                    datas = new Integer[][]{{75, 75, 75, 75}, {60, 40, 80, 50}};
                } else {
                    // 输出非201时的错误信息
                    System.out.println(">>>>>>>>>>Devices接口状态错误>>>>>>>>>>>>");
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("error_status_code", response.code());
                    editor.putString("error_msg", response.errorBody().toString());
                    editor.apply();
                    System.out.println(">>>>>>>>>>Devices接口状态错误>>>>>>>>>>>>");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println(">>>>>>>>>>Devices接口未成功链接>>>>>>>>>>>>");
                //// TODO: 16/1/28  错误处理
            }
        });
        System.out.println(">>>>>>>>>>Devices接口调用完成>>>>>>>>>>>>");
    }

    public String[] getTexts(){
        return texts;
    }

    public Integer[][] getDatas(){
        return datas;
    }
}
