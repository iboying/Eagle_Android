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

import com.buoyantec.eagle_android.adapter.WarnMessageListAdapter;
import com.buoyantec.eagle_android.model.MySystem;
import com.buoyantec.eagle_android.model.MySystems;
import com.buoyantec.eagle_android.model.SubSystem;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class WarnSystems extends BaseActivity{
    private HashMap<String, Integer> systemIcon;
    private Integer statusCode;
    private Context context;
    private CircleProgressBar circleProgressBar;
    private Toolbar toolbar;
    private TextView subToolbarTitle;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_warn_systems);
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

    private void init(){
        systemIcon = new HashMap<>();
        context = this;
        // 组件
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        circleProgressBar = getViewById(R.id.progressBar);
        circleProgressBar.setVisibility(View.VISIBLE);

        // 动力
        systemIcon.put("UPS系统", R.drawable.system_status_ups);
        systemIcon.put("电量仪系统", R.drawable.system_status_box);
        systemIcon.put("配电系统", R.drawable.system_status_power);
        systemIcon.put("电池检测", R.drawable.system_status_battery);
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
        Integer room_id = getIntent().getIntExtra("room_id", 0);
        if (room_id == 0)
            room_id = null;
        mEngine.getSystems(room_id).enqueue(new Callback<MySystems>() {
            @Override
            public void onResponse(Response<MySystems> response) {
                statusCode = response.code();
                if (response.body() != null && statusCode == 200) {
                    // 定义动态数组,用于保存子系统及图标
                    final ArrayList<String> names = new ArrayList<>();
                    ArrayList<Integer> device_images = new ArrayList<>();
                    ArrayList<Integer> alarmCount = new ArrayList<>();
                    final ArrayList<Integer> ids = new ArrayList<>();

                    // 读取子系统告警数量
                    Bundle bundle = getIntent().getExtras();
                    HashMap<String, Integer> map = (HashMap<String, Integer>) bundle.getSerializable("systemAlarmCount");

                    //  获取所有的分类系统(比如: 动力,环境..)
                    List<MySystem> mySystems = response.body().getMySystems();
                    for (MySystem mySystem : mySystems) {
                        // 获取所有的子系统( 比如: ups, 配电..)
                        for (SubSystem subSystem : mySystem.getSubSystem()) {
                            String subName = subSystem.getSubSystemName();
                            if (map != null) {
                                if (map.get(subName) != null) {
                                    names.add(subName);
                                    ids.add(subSystem.getId());
                                    device_images.add(systemIcon.get(subName));
                                    alarmCount.add(map.get(subName));
                                }
                            }
                        }
                    }

                    // 图片
                    Integer[] images = device_images.toArray(new Integer[device_images.size()]);

                    // 加载listView
                    ListView listView = getViewById(R.id.warn_systems_listView);
                    listView.setAdapter(new WarnMessageListAdapter(listView, context, images, names, alarmCount));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            Intent i = new Intent(WarnSystems.this, WarnDevices.class);
                            i.putExtra("title", names.get(position));
                            i.putExtra("subSystem_id", ids.get(position));
                            startActivity(i);
                        }
                    });

                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);
                    Log.i("系统告警", context.getString(R.string.getSuccess) + statusCode);
                } else {
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
                showToast(context.getString(R.string.netWorkFailed));
                Log.i("系统告警", context.getString(R.string.linkFailed));
            }
        });
    }
}
