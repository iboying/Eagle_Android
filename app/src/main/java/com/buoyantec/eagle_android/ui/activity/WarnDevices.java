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

import com.buoyantec.eagle_android.adapter.WarnMessageListAdapter;
import com.buoyantec.eagle_android.model.Device;
import com.buoyantec.eagle_android.model.Devices;
import com.buoyantec.eagle_android.model.Result;
import com.buoyantec.eagle_android.model.Results;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class WarnDevices extends BaseActivity {
    private SharedPreferences sp;
    private Integer room_id;
    private String subSystemName;
    private Context context;
    private Toolbar toolbar;
    private TextView subToolbarTitle;
    private CircleProgressBar circleProgressBar;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_warn_devices);
        init();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        // sub_toolbar
        initToolbar();
        // 获得数据
        getDeviceAlarmCount();
    }

    private void init(){
        sp = getSharedPreferences("foobar", MODE_PRIVATE);
        room_id = sp.getInt("current_room_id", 1);
        context = this;
        // 组件
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        circleProgressBar = getViewById(R.id.progressBar);
        circleProgressBar.setVisibility(View.VISIBLE);
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        subSystemName = getIntent().getStringExtra("title");
        if (subSystemName == null) {
            subSystemName = "";
        }
        subToolbarTitle.setText(subSystemName);
    }

    // 获取设备告警数量
    private void getDeviceAlarmCount() {
        // 初始化
        SharedPreferences mPreferences = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        Integer sub_system_id = getIntent().getIntExtra("subSystem_id", 1);
        final HashMap<String, Integer> deviceCount = new HashMap<>();

        setEngine(mPreferences);
        mEngine.getDeviceAlarmCount(room_id, sub_system_id).enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Response<Results> response) {
                Integer statusCode = response.code();
                if (statusCode == 200) {
                    // 计数
                    List<Result> results = response.body().getResults();
                    for (Result result : results) {
                        deviceCount.put(result.getName(), result.getSize());
                    }

                    // 获得告警数,加载listView
                    initListView(deviceCount);
                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);
                } else {
                    // 输出非201时的错误信息
                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);
                    showToast(context.getString(R.string.getDataFailed));
                    Log.i("系统告警", context.getString(R.string.getFailed) + statusCode);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                System.out.println("设备告警数量接口,链接错误");
            }
        });
    }

    private void initListView(final HashMap<String, Integer> countMap) {
        mEngine.getDevices(room_id, subSystemName).enqueue(new Callback<Devices>() {
            @Override
            public void onResponse(Response<Devices> response) {
                int code = response.code();
                if (code == 200) {
                    // 载入设备列表
                    final ArrayList<String> names = new ArrayList<>();
                    final ArrayList<Integer> ids = new ArrayList<>();
                    ArrayList<Integer> alarmCount = new ArrayList<>();
                    // 读取数据
                    List<Device> devices = response.body().getDevices();
                    for (Device device : devices) {
                        String deviceName = device.getName();
                        Integer id = device.getId();

                        if (countMap.get(deviceName) != null) {
                            alarmCount.add(countMap.get(deviceName));
                            names.add(deviceName);
                            ids.add(id);
                        }
                    }

                    // images
                    Integer[] images = new Integer[names.size()];

                    ListView listView = getViewById(R.id.warn_devices_listView);
                    listView.setAdapter(new WarnMessageListAdapter(listView, context, images, names, alarmCount));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            Intent i = new Intent(WarnDevices.this, WarnDetail.class);
                            i.putExtra("title", names.get(position));
                            i.putExtra("device_id", ids.get(position));
                            startActivity(i);
                        }
                    });

                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);
                    Log.i("设备告警", context.getString(R.string.getSuccess) + code);
                } else {
                    // 输出非201时的错误信息
                    showToast(context.getString(R.string.getDataFailed));
                    Log.i("设备告警", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                showToast(context.getString(R.string.netWorkFailed));
                Log.i("设备告警", context.getString(R.string.linkFailed));
            }
        });
    }
}
