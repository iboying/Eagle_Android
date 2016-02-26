package com.buoyantec.eagle_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.API.MyService;
import com.buoyantec.eagle_android.adapter.SystemStatusListAdapter;
import com.buoyantec.eagle_android.adapter.WarnMessageListAdapter;
import com.buoyantec.eagle_android.model.Device;
import com.buoyantec.eagle_android.model.Devices;
import com.buoyantec.eagle_android.model.MySystem;
import com.buoyantec.eagle_android.model.MySystems;
import com.buoyantec.eagle_android.model.Result;
import com.buoyantec.eagle_android.model.Results;
import com.buoyantec.eagle_android.model.SubSystem;
import com.buoyantec.eagle_android.myService.ApiRequest;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

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
    private Integer room_id;
    private String subSystemName;
    private Context context;
    private CircleProgressBar circleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn_devices);
        // 初始化
        init();
        // sub_toolbar
        initToolbar();
        // 获得数据
        getDeviceAlarmCount();
    }

    private void init(){
        sp = getSharedPreferences("foobar", MODE_PRIVATE);
        room_id = sp.getInt("current_room_id", 1);
        context = this;
        // 进度条
        circleProgressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        circleProgressBar.setVisibility(View.VISIBLE);
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

    // 获取设备告警数量
    private void getDeviceAlarmCount() {
        // 初始化
        Intent i = getIntent();
        Integer sub_system_id = i.getIntExtra("subSystem_id", 1);
        final HashMap<String, Integer> deviceCount = new HashMap<>();

        final ApiRequest apiRequest = new ApiRequest(this);
        Call<Results> call = apiRequest.getService().getDeviceAlarmCount(room_id, sub_system_id);
        // 获取数据
        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Response<Results> response) {
                if (response.code() == 200) {
                    // 计数
                    List<Result> results = response.body().getResults();
                    for (Result result : results) {
                        deviceCount.put(result.getName(), result.getSize());
                    }

                    // 获得告警数,加载listView
                    initListView(apiRequest.getService(), deviceCount);
                } else {
                    // 输出非201时的错误信息
                    System.out.println(">>>>>>>>>>设备告警数量接口状态错误>>>>>>>>>>>>");
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("error_status_code", response.code());
                    editor.putString("error_msg", response.errorBody().toString());
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                System.out.println("设备告警数量接口,链接错误");
                // TODO: 16/2/19 错误处理
            }
        });
    }

    private void initListView(MyService myService, final HashMap<String, Integer> countMap) {
        Call<Devices> call = myService.getDevices(room_id, subSystemName);
        // 发送请求
        call.enqueue(new Callback<Devices>() {
            @Override
            public void onResponse(Response<Devices> response) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                int code = response.code();
                if (code == 200) {
                    // 载入设备列表
                    ArrayList<String> device_name = new ArrayList<>();
                    ArrayList<Integer> device_id = new ArrayList<>();
                    ArrayList<Integer> deviceAlarmCount = new ArrayList<>();
                    // 读取数据
                    List<Device> devices = response.body().getDevices();
                    for (Device device : devices) {
                        String deviceName = device.getName();
                        Integer id = device.getId();

                        device_name.add(deviceName);
                        device_id.add(id);
                        if (countMap.get(deviceName) != null) {
                            deviceAlarmCount.add(countMap.get(deviceName));
                        } else {
                            deviceAlarmCount.add(0);
                        }
                    }
                    // references to our images
                    Integer[] images = new Integer[device_name.size()];
                    final String[] names = device_name.toArray(new String[device_name.size()]);
                    final Integer[] ids = device_id.toArray(new Integer[device_id.size()]);
                    Integer[] count = deviceAlarmCount.toArray(new Integer[deviceAlarmCount.size()]);

                    ListView listView = (ListView) findViewById(R.id.warn_devices_listView);
                    listView.setAdapter(new WarnMessageListAdapter(listView, context, images, names, count));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            Intent i = new Intent(WarnDevices.this, WarnDetail.class);
                            i.putExtra("title", names[position]);
                            i.putExtra("device_id", ids[position]);
                            startActivity(i);
                        }
                    });
                    Log.i("设备告警", context.getString(R.string.getSuccess) + code);
                } else {
                    // 输出非201时的错误信息
                    Log.i("设备告警", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                Log.i("设备告警", context.getString(R.string.linkFailed));
                //// TODO: 16/1/28  错误处理
            }
        });
    }
}
