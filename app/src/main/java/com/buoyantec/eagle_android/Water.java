package com.buoyantec.eagle_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.API.MyService;
import com.buoyantec.eagle_android.adapter.StandardListAdapter;
import com.buoyantec.eagle_android.adapter.TemperatureListAdapter;
import com.buoyantec.eagle_android.adapter.WaterListAdapter;
import com.buoyantec.eagle_android.model.Device;
import com.buoyantec.eagle_android.model.Devices;
import com.buoyantec.eagle_android.myService.ApiRequest;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Water extends AppCompatActivity {
    private SharedPreferences sp;
    private Integer room_id;
    private String sub_sys_name;
    private Context context;
    private CircleProgressBar circleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        // 初始化变量
        init();
        // 加载字体图标
        Iconify.with(new FontAwesomeModule());
        // 初始化toolbar
        initToolbar();
        // 加载list
        initListView();
    }

    private void init() {
        sp = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        // TODO: 16/2/7 默认值的问题
        room_id = sp.getInt("current_room_id", 1);

        Intent i = getIntent();
        sub_sys_name = i.getStringExtra("sub_sys_name");

        context = getApplicationContext();
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
        subToolbarTitle.setText(sub_sys_name);
    }

    private void initListView() {
        // 获取指定链接数据
        ApiRequest apiRequest = new ApiRequest(this);
        Call<Devices> call = apiRequest.getService().getDevices(room_id, sub_sys_name);
        call.enqueue(new Callback<Devices>() {
            @Override
            public void onResponse(Response<Devices> response) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                int code = response.code();
                if (code == 200) {
                    ArrayList<String> device_name = new ArrayList<>();
                    ArrayList<Integer> device_id = new ArrayList<>();
                    // 获取用户
                    List<Device> devices = response.body().getDevices();
                    for (Device device : devices) {
                        device_name.add(device.getName());
                        device_id.add(device.getId());
                    }

                    // 图标
                    Integer image = R.drawable.system_status_water;
                    // 设备名称
                    String[] names = device_name.toArray(new String[device_name.size()]);
                    // 设备id
                    final Integer[] ids = device_id.toArray(new Integer[device_id.size()]);

                    ListView listView = (ListView) findViewById(R.id.water_listView);
                    listView.setAdapter(new StandardListAdapter(listView, context, image, names));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            TextView title = (TextView) v.findViewById(R.id.list_item_standard_list_text);
                            Intent i = new Intent(Water.this, WaterDetail.class);
                            i.putExtra("title", title.getText());
                            i.putExtra("device_id", ids[position]);
                            startActivity(i);
                        }
                    });
                    Log.i(sub_sys_name, context.getString(R.string.getSuccess) + code);
                } else {
                    // 输出非201时的错误信息
                    Log.i(sub_sys_name, context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                Log.i(sub_sys_name, context.getString(R.string.linkFailed));
                //// TODO: 16/1/28  错误处理
            }
        });
    }
}
