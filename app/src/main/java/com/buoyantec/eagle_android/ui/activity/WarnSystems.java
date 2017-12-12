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
import android.widget.Toast;

import com.buoyantec.eagle_android.adapter.WarnMessageListAdapter;
import com.buoyantec.eagle_android.model.RoomAlarm;
import com.buoyantec.eagle_android.model.SubSystemAlarm;
import com.buoyantec.eagle_android.ui.base.BaseActivity;
import com.buoyantec.eagle_android.ui.base.BaseTimerActivity;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarnSystems extends BaseTimerActivity {
    private HashMap<String, Integer> systemIcon;
    private Context context;
    private CircleProgressBar circleProgressBar;
    private Toolbar toolbar;
    private TextView subToolbarTitle;

    private Integer current_room_id;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_warn_systems);
        // 组件
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        circleProgressBar = getViewById(R.id.progressBar);
        // 变量
        init();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        // sub_toolbar
        initToolbar();
        // ListView
        initListView();
    }

    @Override
    protected void beginTimerTask() {
        initListView();
    }

    private void init(){
        systemIcon = new HashMap<>();
        context = this;
        current_room_id = getIntent().getIntExtra("room_id", 0);

        // 动力
        systemIcon.put("UPS系统", R.drawable.system_status_ups);
        systemIcon.put("电量仪系统", R.drawable.system_status_box);
        systemIcon.put("配电系统", R.drawable.system_status_power);
        systemIcon.put("电池系统", R.drawable.system_status_battery);
        systemIcon.put("发电机系统", R.drawable.system_status_engine);
        // 环境
        systemIcon.put("温湿度系统", R.drawable.system_status_temperature);
        systemIcon.put("机柜环境", R.drawable.system_status_cabinet);
        systemIcon.put("空调系统", R.drawable.system_status_air);
        systemIcon.put("漏水系统", R.drawable.system_status_water);
        // 联动
        // 安全
        systemIcon.put("消防系统", R.drawable.system_status_video);
        systemIcon.put("氢气检测", R.drawable.system_status_smoke);
        // 远程
        // 能效
        // 部署
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        subToolbarTitle.setText(getIntent().getStringExtra("title"));
    }

    private void initListView() {
        setEngine(sp);

        circleProgressBar.setVisibility(View.VISIBLE);
        // 请求服务
        mEngine.getSubSystemAlarmCount(current_room_id).enqueue(new Callback<RoomAlarm>() {
            @Override
            public void onResponse(Call<RoomAlarm> call, Response<RoomAlarm> response) {
                setNetworkState(true);
                int code = response.code();
                if (code == 200) {
                    List<SubSystemAlarm> subSystemAlarms = response.body().getSubSystemAlarms();
                    // 列表数据
                    List<Integer> images = new ArrayList<>();
                    final List<Integer> subSystemId = new ArrayList<>();
                    final List<String> subSystemNames = new ArrayList<>();
                    List<Integer> subSystemAlarmCount = new ArrayList<>();
                    // 获取数据
                    for (SubSystemAlarm subSystemAlarm : subSystemAlarms) {
                        subSystemId.add(subSystemAlarm.getSubSystemId());
                        subSystemNames.add(subSystemAlarm.getSubSystemName());
                        subSystemAlarmCount.add(subSystemAlarm.getSubSystemCount());
                        images.add(systemIcon.get(subSystemAlarm.getSubSystemName()));
                    }
                    // 加载列表
                    ListView listView = getViewById(R.id.warn_systems_listView);
                    listView.setAdapter(new WarnMessageListAdapter(listView, context, images, subSystemNames, subSystemAlarmCount));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            Intent i = new Intent(WarnSystems.this, WarnDetail.class);
                            i.putExtra("title", subSystemNames.get(position));
                            i.putExtra("subSystemId", subSystemId.get(position));
                            startActivity(i);
                        }
                    });
                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);
                    Log.i("获取子系统告警数", context.getString(R.string.getSuccess) + code);
                } else {
                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(context, context.getString(R.string.getDataFailed), Toast.LENGTH_SHORT).show();
                    Log.i("获取子系统告警数", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Call<RoomAlarm> call, Throwable t) {
                circleProgressBar.setVisibility(View.INVISIBLE);
                setNetworkState(false);
                Log.i("获取子系统告警数", context.getString(R.string.linkFailed));
            }
        });
    }
}
