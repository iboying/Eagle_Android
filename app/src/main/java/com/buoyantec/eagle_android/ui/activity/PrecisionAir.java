package com.buoyantec.eagle_android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.adapter.PrecisionAirListAdapter;
import com.buoyantec.eagle_android.model.Device;
import com.buoyantec.eagle_android.model.Devices;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrecisionAir extends BaseActivity {
    private Integer room_id;
    private String sub_sys_name;
    private Context context;
    private CircleProgressBar circleProgressBar;
    private Toolbar toolbar;
    private TextView subToolbarTitle;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_precision_air);
        Iconify.with(new FontAwesomeModule());
        init();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        //初始化toolbar
        initToolbar();
        //初始化list
        initListView();
    }

    private void init() {
        room_id = sp.getInt("current_room_id", 1);
        sub_sys_name = getIntent().getStringExtra("sub_sys_name");
        if (sub_sys_name == null) {
            sub_sys_name = "";
        }
        context = this;

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
        subToolbarTitle.setText(sub_sys_name);
    }

    private void initListView() {
        setEngine(sp);
        // 获取指定链接数据
        mEngine.getDevices(room_id, sub_sys_name).enqueue(new Callback<Devices>() {
            @Override
            public void onResponse(Call<Devices> call, Response<Devices> response) {
                int code = response.code();
                if (code == 200) {
                    ArrayList<String> names = new ArrayList<>();
                    final ArrayList<Integer> ids = new ArrayList<>();
                    List<List<String>> labels = new ArrayList<>();
                    List<List<String>> datas = new ArrayList<>();
                    List<Integer> status = new ArrayList<>();

                    List<Device> devices = response.body().getDevices();
                    for (Device device : devices) {
                        ids.add(device.getId());
                        names.add(device.getName());
                        // 获取存在温湿度的设备数据
                        List<String> data = new ArrayList<>();
                        List<String> label = new ArrayList<>();

                        List<HashMap<String, String>> points = device.getPoints();
                        for (HashMap<String, String> point : points) {
                            label.add(point.get("name"));
                            data.add(point.get("value"));
                        }
                        labels.add(label);
                        datas.add(data);

                        // 获取存在状态的设备数据
                        String alarm = device.getAlarm();
                        if (alarm == null) {
                            status.add(2);
                        } else {
                            if (alarm.equals("false")) {
                                status.add(0);
                            } else {
                                status.add(1);
                            }
                        }
                    }

                    // 图标
                    Integer image = R.drawable.air;

                    ListView listView = getViewById(R.id.precision_air_listView);
                    listView.setAdapter(new PrecisionAirListAdapter(listView, context, image, names, labels, datas, status));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            TextView title = (TextView) v.findViewById(R.id.list_item_precision_air_text);
                            if (title == null) {
                                title = (TextView) v.findViewById(R.id.list_item_device_status_text);
                            }
                            Intent i = new Intent(PrecisionAir.this, PrecisionAirDetail.class);
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
                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);
                    showToast(context.getString(R.string.getDataFailed));
                    Log.i(sub_sys_name, context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Call<Devices> call, Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                showToast(context.getString(R.string.netWorkFailed));
                Log.i(sub_sys_name, context.getString(R.string.linkFailed));
            }
        });
    }
}
