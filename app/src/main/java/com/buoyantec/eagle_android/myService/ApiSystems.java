package com.buoyantec.eagle_android.myService;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buoyantec.eagle_android.API.MyService;
import com.buoyantec.eagle_android.R;
import com.buoyantec.eagle_android.model.MySystem;
import com.buoyantec.eagle_android.model.MySystems;
import com.buoyantec.eagle_android.model.SubSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
 * Created by kang on 16/2/18.
 */
public class ApiSystems {
    private String phone;
    private String token;
    private Integer statusCode;
    private SharedPreferences sp;
    ArrayList<String> subSystemList = new ArrayList<>();
    private HashMap<String, String[]> kindSystems;
    //初始化
    public ApiSystems(Context context, String phone, String token){
        this.phone = phone;
        this.token = token;
        sp = context.getSharedPreferences("foobar", Activity.MODE_PRIVATE);
    }

    public void getSystems() {
        // 获取数据
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

        // 建立http请求
        MyService myService = retrofit.create(MyService.class);
        Call<MySystems> call = myService.getSystems();
        // 发送请求
        call.enqueue(new Callback<MySystems>() {
            @Override
            public void onResponse(Response<MySystems> response) {
                statusCode = response.code();
                if (response.body() != null && statusCode == 200) {
                    //  获取所有的分类系统(比如: 动力,环境..)
                    List<MySystem> mySystems = response.body().getMySystems();
                    Iterator<MySystem> itr = mySystems.iterator();
                    while (itr.hasNext()) {
                        MySystem mySystem = itr.next();
                        String systemName = mySystem.getName();
                        System.out.println(systemName + "---------------------------");
                        subSystemList.clear();
                        // 获取所有的子系统( 比如: ups, 配电..)
                        Iterator<SubSystem> subItr = mySystem.getSubSystem().iterator();
                        while (subItr.hasNext()) {
                            SubSystem subSystem = subItr.next();
                            String subName = subSystem.getSubSystemName();
                            System.out.println("-->"+subName);
                            subSystemList.add(subName);
                        }
                        // 把分类名和对应的子系统列表写入HashMap
                        if (subSystemList.size() > 0){
                            kindSystems.put(systemName,
                                    subSystemList.toArray(new String[subSystemList.size()]));
                        }
                    }
                    System.out.println(kindSystems.toString());

                } else {
                    try {
                        String error = response.errorBody().string();
                        System.out.println(error+"8888888888888888888888888888888");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("system_status_status_code", statusCode);
                    editor.apply();
                    System.out.println(">>>>>>>>>>获取系统状态列表失败>>>>>>>>>>>>");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println(">>>>>>>>>>系统状态接口链接失败>>>>>>>>>>>>");
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("system_status_status_code", 2222);
                editor.apply();
            }
        });
        System.out.println(">>>>>>>>>>获取系统状态列表执行完成>>>>>>>>>>>>");
    }
}
