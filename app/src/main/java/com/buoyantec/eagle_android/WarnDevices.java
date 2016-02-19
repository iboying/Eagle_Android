package com.buoyantec.eagle_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.API.MyService;
import com.buoyantec.eagle_android.adapter.SystemStatusListAdapter;
import com.buoyantec.eagle_android.adapter.WarnMessageListAdapter;
import com.buoyantec.eagle_android.model.Device;
import com.buoyantec.eagle_android.model.Devices;
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

public class WarnDevices extends AppCompatActivity {
    private SharedPreferences sp;
    private String token;
    private String phone;
    private Integer room_id;
    private String subSystemName;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn_devices);
        // 初始化
        init();
        // sub_toolbar
        initToolbar();
        // ListView
        initListView();
    }

    private void init(){
        sp = getSharedPreferences("foobar", MODE_PRIVATE);
        token = sp.getString("token", null);
        phone = sp.getString("phone", null);
        room_id = sp.getInt("current_room_id", 1);
        context = this;
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sub_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView subToolbarTitle = (TextView) findViewById(R.id.sub_toolbar_title);
        Intent i = getIntent();
        subSystemName = i.getStringExtra("title");
        subToolbarTitle.setText(subSystemName);
    }

    private void initListView() {
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
        Call<Devices> call = myService.getDevices(room_id, subSystemName);
        // 发送请求
        call.enqueue(new Callback<Devices>() {
            @Override
            public void onResponse(Response<Devices> response) {
                if (response.code() == 200) {
                    ArrayList<String> device_name = new ArrayList<>();
                    ArrayList<Integer> device_id = new ArrayList<>();
                    // 读取数据
                    List<Device> devices = response.body().getDevices();
                    Iterator<Device> itr = devices.iterator();
                    while (itr.hasNext()) {
                        Device device = itr.next();
                        device_name.add(device.getName());
                        device_id.add(device.getId());
                    }
                    // references to our images
                    Integer[] images = new Integer[device_name.size()];
                    final String[] texts = device_name.toArray(new String[device_name.size()]);
                    final Integer[] ids = device_id.toArray(new Integer[device_id.size()]);

                    ListView listView = (ListView) findViewById(R.id.warn_devices_listView);
                    listView.setAdapter(new WarnMessageListAdapter(listView, context, images, texts));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            Intent i = new Intent(WarnDevices.this, WarnDetail.class);
                            i.putExtra("title", texts[position]);
                            i.putExtra("device_id", ids[position]);
                            startActivity(i);
                        }
                    });
                    System.out.println("Devices接口调用完成");
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
    }
}
