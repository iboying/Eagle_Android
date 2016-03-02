package com.buoyantec.eagle_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.buoyantec.eagle_android.adapter.WarnMessageListAdapter;
import com.buoyantec.eagle_android.model.MySystem;
import com.buoyantec.eagle_android.model.MySystems;
import com.buoyantec.eagle_android.model.SubSystem;
import com.buoyantec.eagle_android.myService.ApiRequest;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarnSystems extends AppCompatActivity{
    private HashMap<String, Integer> systemIcon;
    private Integer statusCode;
    private Context context;
    private CircleProgressBar circleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn_systems);
        // 初始化
        init();
        // sub_toolbar
        initToolbar();
        // ListView
        initListView();
    }

    private void init(){
        systemIcon = new HashMap<>();
        context = this;

        // 进度条
        circleProgressBar = (CircleProgressBar) findViewById(R.id.progressBar);
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.sub_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView subToolbarTitle = (TextView) findViewById(R.id.sub_toolbar_title);
        Intent i = getIntent();
        subToolbarTitle.setText(i.getStringExtra("title"));
    }

    private void initListView() {
        ApiRequest apiRequest = new ApiRequest(this);
        Call<MySystems> call = apiRequest.getService().getSystems();
        // 发送请求
        call.enqueue(new Callback<MySystems>() {
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
                            names.add(subName);
                            device_images.add(systemIcon.get(subName));
                            ids.add(subSystem.getId());
                            if (map != null) {
                                if (map.get(subName) != null) {
                                    alarmCount.add(map.get(subName));
                                } else {
                                    alarmCount.add(0);
                                }
                            } else {
                                alarmCount.add(0);
                            }
                        }
                    }

                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);

                    // 图片
                    Integer[] images = device_images.toArray(new Integer[device_images.size()]);

                    // 加载listView
                    ListView listView = (ListView) findViewById(R.id.warn_systems_listView);
                    listView.setAdapter(new WarnMessageListAdapter(listView, context, images, names, alarmCount));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            Intent i = new Intent(WarnSystems.this, WarnDevices.class);
                            i.putExtra("title", names.get(position));
                            i.putExtra("subSystem_id", ids.get(position));
                            startActivity(i);
                        }
                    });
                    Log.i("系统告警", context.getString(R.string.getSuccess) + statusCode);
                } else {
                    Toast.makeText(context, context.getString(R.string.getDataFailed), Toast.LENGTH_SHORT).show();
                    Log.i("系统告警", context.getString(R.string.getFailed) + statusCode);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                Toast.makeText(context, context.getString(R.string.netWorkFailed), Toast.LENGTH_SHORT).show();
                Log.i("系统告警", context.getString(R.string.linkFailed));
            }
        });
    }
}
