package com.buoyantec.eagle_android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.BoolRes;
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
import com.buoyantec.eagle_android.ui.base.BaseActivity;
import com.buoyantec.eagle_android.ui.base.BaseTimerActivity;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 电池系统 , 发电机系统
 */

public class Battery extends BaseTimerActivity {
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
        room_id = sp.getInt("current_room_id", 1);
        sub_sys_name = getIntent().getStringExtra("sub_sys_name");
        if (sub_sys_name == null) {
            sub_sys_name = "";
        }
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        circleProgressBar = getViewById(R.id.progressBar);
        context = this;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initToolbar();
        initListView();
    }

    @Override
    protected void beginTimerTask() {
        initListView();
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        subToolbarTitle.setText(sub_sys_name);
    }

    private void initListView() {
        circleProgressBar.setVisibility(View.VISIBLE);
        setEngine(sp);
        mEngine.getDevices(room_id, sub_sys_name).enqueue(new Callback<Devices>() {
            @Override
            public void onResponse(Call<Devices> call, Response<Devices> response) {
                setNetworkState(true);
                int code = response.code();
                if (code == 200) {
                    final List<Integer> ids = new ArrayList<>();
                    List<String> names = new ArrayList<>();
                    List<List<String>> keys = new ArrayList<>();
                    List<List<String>> values = new ArrayList<>();
                    List<String> alarms = new ArrayList<>();

                    // 获取UPS系统的设备列表
                    List<Device> devices = response.body().getDevices();
                    for (Device device : devices) {
                        ids.add(device.getId());
                        names.add(device.getName());
                        alarms.add(device.getAlarm());
                        // 获取point数据
                        List<String> k = new ArrayList<>();
                        List<String> v = new ArrayList<>();
                        List<HashMap<String, String>> points = device.getPoints();
                        for (HashMap<String, String> point : points) {
                            if (point.get("name") == null) {
                                k.add("-");
                                v.add("-");
                            }else{
                                k.add(point.get("name"));
                                v.add(point.get("value"));
                            }
                        }
                        if (k.size() > 0 && v.size() > 0) {
                            keys.add(k);
                            values.add(v);
                        }
                    }

                    // 列表图标
                    Integer image = R.drawable.battery;

                    // 加载设备列表
                    ListView listView = getViewById(R.id.battery_listView);
                    listView.setAdapter(new BatteryListAdapter(listView, context, image, names, alarms, keys, values));
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

                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);
                    Log.i(sub_sys_name, context.getString(R.string.getSuccess) + code);
                } else {
                    // 输出非201时的错误信息
                    circleProgressBar.setVisibility(View.GONE);
                    showToast(context.getString(R.string.getDataFailed));
                    Log.i(sub_sys_name, context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Call<Devices> call, Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                setNetworkState(false);
                Log.i(sub_sys_name, context.getString(R.string.linkFailed));
            }
        });
    }
}
