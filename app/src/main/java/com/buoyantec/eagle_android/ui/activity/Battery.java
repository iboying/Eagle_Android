package com.buoyantec.eagle_android.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.adapter.BatteryListAdapter;
import com.buoyantec.eagle_android.model.Device;
import com.buoyantec.eagle_android.model.Devices;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class Battery extends BaseActivity {
    private Integer room_id;
    private String sub_sys_name;
    private Context context;
    private CircleProgressBar circleProgressBar;
    private Toolbar toolbar;
    private TextView subToolbarTitle;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_battery);
        Iconify.with(new FontAwesomeModule());
        init();
        initToolbar();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mEngine.getDevices(room_id, sub_sys_name).enqueue(new Callback<Devices>() {
            @Override
            public void onResponse(Response<Devices> response) {
                int code = response.code();
                if (code == 200) {
                    final List<Integer> ids = new ArrayList<>();
                    List<String> names = new ArrayList<>();
                    List<List<String>> keys = new ArrayList<>();
                    List<List<String>> values = new ArrayList<>();

                    // 获取UPS系统的设备列表
                    List<Device> devices = response.body().getDevices();
                    for (Device device : devices) {
                        ids.add(device.getId());
                        names.add(device.getName());
                        // 获取point数据
                        List<String> k = new ArrayList<>();
                        List<String> v = new ArrayList<>();
                        List<HashMap<String, String>> points = device.getPoints();
                        for (HashMap<String, String> point : points) {
                            k.add(point.get("name"));
                            v.add(point.get("value"));
                        }
                        keys.add(k);
                        values.add(v);
                    }

                    // 列表图标
                    Integer image = R.drawable.battery;

                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);

                    // 加载设备列表
                    ListView listView = getViewById(R.id.battery_listView);
                    listView.setAdapter(new BatteryListAdapter(listView, context, image, names, keys, values));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView title = (TextView) view.findViewById(R.id.list_item_battery_text);
                            Intent i = new Intent(Battery.this, BatteryDetail.class);
                            i.putExtra("title", title.getText());
                            i.putExtra("device_id", ids.get(position));
                            startActivity(i);
                        }
                    });
                    Log.i(sub_sys_name, context.getString(R.string.getSuccess) + code);
                } else {
                    // 输出非201时的错误信息
                    circleProgressBar.setVisibility(View.GONE);
                    showToast(context.getString(R.string.getDataFailed));
                    Log.i(sub_sys_name, context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                showToast(context.getString(R.string.netWorkFailed));
                Log.i(sub_sys_name, context.getString(R.string.linkFailed));
            }
        });
    }

    // 初始化变量
    private void init() {
        SharedPreferences sp = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        room_id = sp.getInt("current_room_id", 1);
        sub_sys_name = getIntent().getStringExtra("sub_sys_name");
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        circleProgressBar = getViewById(R.id.progressBar);
        circleProgressBar.setVisibility(View.VISIBLE);
        context = this;
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        subToolbarTitle.setText(sub_sys_name);
    }
}