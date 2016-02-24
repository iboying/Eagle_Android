package com.buoyantec.eagle_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.API.MyService;
import com.buoyantec.eagle_android.adapter.PrecisionAirListAdapter;
import com.buoyantec.eagle_android.adapter.SystemStatusListAdapter;
import com.buoyantec.eagle_android.model.Device;
import com.buoyantec.eagle_android.model.Devices;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

public class PrecisionAir extends AppCompatActivity {
    private SharedPreferences sp;
    private Integer room_id;
    private String sub_sys_name;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        //加载字体图标
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_precision_air);
        //初始化toolbar
        initToolbar();
        //初始化list
        initListView();
    }

    private void init() {
        sp = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        // TODO: 16/2/7 默认值的问题
        room_id = sp.getInt("current_room_id", 1);

        Intent i = getIntent();
        sub_sys_name = i.getStringExtra("sub_sys_name");

        context = getApplicationContext();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sub_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView subToolbarTitle = (TextView) findViewById(R.id.sub_toolbar_title);
        subToolbarTitle.setText(sub_sys_name);
    }

    private void initListView() {
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
                    ArrayList<Integer> device_id = new ArrayList<>();
                    ArrayList<String[]> device_datas = new ArrayList<>();
                    ArrayList<Integer> device_status = new ArrayList<>();

                    List<Device> devices = response.body().getDevices();
                    for (Device device : devices) {
                        device_name.add(device.getName());
                        device_id.add(device.getId());
                        String[] value = {
                                device.getTemperature()[1],
                                device.geHumidity()[1]
                        };
                        device_datas.add(value);
                        String alarm = device.getAlarm();
                        if (alarm == null) {
                            device_status.add(2);
                        } else {
                            if (alarm.equals("false")) {
                                device_status.add(1);
                            } else {
                                device_status.add(0);
                            }
                        }
                    }
                    // references to our images
                    Integer image = R.drawable.air;
                    // 设备id
                    final Integer[] ids = device_id.toArray(new Integer[device_id.size()]);

                    ListView listView = (ListView) findViewById(R.id.precision_air_listView);
                    listView.setAdapter(new PrecisionAirListAdapter(
                            listView, context, image, device_name, device_datas, device_status));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            TextView title = (TextView) v.findViewById(R.id.list_item_precision_air_text);
                            if (title == null) {
                                title = (TextView) v.findViewById(R.id.list_item_device_status_text);
                            }
                            Intent i = new Intent(PrecisionAir.this, PrecisionAirDetail.class);
                            i.putExtra("title", title.getText());
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
        System.out.println(">>>>>>>>>>Devices接口调用完成>>>>>>>>>>>>");
    }
}
