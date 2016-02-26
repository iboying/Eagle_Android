package com.buoyantec.eagle_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.adapter.BatteryListAdapter;
import com.buoyantec.eagle_android.model.Device;
import com.buoyantec.eagle_android.model.Devices;
import com.buoyantec.eagle_android.myService.ApiRequest;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Battery extends AppCompatActivity {
    private SharedPreferences sp;
    private Integer room_id;
    private String sub_sys_name;
    private Context context;
    private CircleProgressBar circleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载字体图标
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_battery);
        init();
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
        ApiRequest apiRequest = new ApiRequest(this);
        // 获取指定链接数据
        Call<Devices> call = apiRequest.getService().getDevices(room_id, sub_sys_name);
        call.enqueue(new Callback<Devices>() {
            @Override
            public void onResponse(Response<Devices> response) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                int code = response.code();
                if (code == 200) {
                    ArrayList<String> device_name = new ArrayList<>();
                    // 获取数据
                    List<Device> devices = response.body().getDevices();
                    for (Device device : devices) {
                        device_name.add(device.getName());
                    }
                    // 列表图标
                    Integer image = R.drawable.battery;
                    // 设备名称
                    String[] names = device_name.toArray(new String[device_name.size()]);
                    // 设备数据
                    Integer[][] datas = {{75, 75, 75, 75}, {60, 40, 80, 50}};
                    // 加载设备列表
                    ListView listView = (ListView) findViewById(R.id.battery_listView);
                    listView.setAdapter(new BatteryListAdapter(listView, context, image, names, datas));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView title = (TextView) view.findViewById(R.id.list_item_battery_text);
                            Intent i = new Intent(Battery.this, BatteryDetail.class);
                            i.putExtra("title", title.getText());
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
