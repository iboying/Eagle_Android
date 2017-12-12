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

import com.buoyantec.eagle_android.adapter.DeviceStatusListAdapter;
import com.buoyantec.eagle_android.model.Device;
import com.buoyantec.eagle_android.model.Devices;
import com.buoyantec.eagle_android.ui.base.BaseActivity;
import com.buoyantec.eagle_android.ui.base.BaseTimerActivity;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 系统状态 -> 配电系统
 */
public class PowerDistribution extends BaseTimerActivity {
    private Integer room_id;
    private String sub_sys_name;
    private Context context;

    private CircleProgressBar circleProgressBar;
    private Toolbar toolbar;
    private TextView subToolbarTitle;
    private ListView listView;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_power_distribution);
        Iconify.with(new FontAwesomeModule());
        // 初始化变量
        room_id = sp.getInt("current_room_id", 1);
        sub_sys_name = getIntent().getStringExtra("sub_sys_name");
        if (sub_sys_name == null) {
            sub_sys_name = "";
        }
        context = getApplicationContext();
        // 进度条
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        circleProgressBar = getViewById(R.id.progressBar);
        listView = getViewById(R.id.power_distribution_listView);
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
        setEngine(sp);

        circleProgressBar.setVisibility(View.VISIBLE);
        // 获取指定链接数据
        mEngine.getDevices(room_id, sub_sys_name).enqueue(new Callback<Devices>() {
            @Override
            public void onResponse(Call<Devices> call, Response<Devices> response) {
                setNetworkState(true);
                int code = response.code();
                if (code == 200) {
                    List<String> names = new ArrayList<>();
                    final ArrayList<Integer> ids = new ArrayList<>();
                    List<Integer> status = new ArrayList<>();

                    // 获取配电柜数据
                    List<Device> devices = response.body().getDevices();
                    for (Device device : devices) {
                        names.add(device.getName());
                        ids.add(device.getId());
                        String alarm = device.getAlarm();
                        if (alarm.equals("false")) {
                            status.add(0);
                        } else {
                            status.add(1);
                        }
                    }

                    // 图标
                    Integer image = R.drawable.power_distribution;

                    listView.setAdapter(new DeviceStatusListAdapter(listView, context, image, names, status));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            TextView title = (TextView) v.findViewById(R.id.list_item_device_status_text);
                            Intent i = new Intent(PowerDistribution.this, PowerDetail.class);
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
                setNetworkState(false);
                Log.i(sub_sys_name, context.getString(R.string.linkFailed));
            }
        });
    }
}
