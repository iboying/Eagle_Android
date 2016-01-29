package com.buoyantec.eagle_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buoyantec.eagle_android.API.MyService;
import com.buoyantec.eagle_android.adapter.SystemStatusGridAdapter;
import com.buoyantec.eagle_android.model.MySystem;
import com.buoyantec.eagle_android.model.MySystems;
import com.buoyantec.eagle_android.model.SubSystem;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SystemStatus extends AppCompatActivity {
    private HashMap<String, Integer> systemIcon;
    private HashMap<String, ArrayList<String>> kindSystems;
    private Integer statusCode;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_status);
        // 初始化变量
        init();
        // sub_toolbar
        initToolbar();
        //GridView
        initPowerSystemGrid();
        initEnvSystemGrid();
        initSafeSystemGrid();
    }

    //-------------------私有方法--------------------
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sub_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView subToolbarTitle = (TextView) findViewById(R.id.sub_toolbar_title);
        subToolbarTitle.setText("系统状态");
    }

    private void init(){
        systemIcon = new HashMap<>();
        kindSystems = new HashMap<>();
        context = this;
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

    //初始化动力系统GridView
    private void initPowerSystemGrid(){
        // references to our images
        Integer[] images = {
                R.drawable.system_status_power, R.drawable.system_status_ups,
                R.drawable.system_status_box, R.drawable.system_status_ats,
                R.drawable.system_status_battery, R.drawable.system_status_engine,
        };
        // texts of images
        String[] texts = { "配电", "UPS", "列头柜", "ATS", "蓄电池", "柴油机" };

        GridView gridView = (GridView) findViewById(R.id.grid_power_system);
        gridView.setAdapter(new SystemStatusGridAdapter(gridView, this, images, texts));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            if (position == 0) {
                Intent i = new Intent(context, PowerDistribution.class);
                startActivity(i);
            } else if (position == 1) {
                Intent i = new Intent(context, UpsSystem.class);
                startActivity(i);
            } else if (position == 2) {
                Intent i = new Intent(context, Box.class);
                startActivity(i);
            } else if (position == 3) {

            } else if (position == 4) {
                Intent i = new Intent(context, Battery.class);
                startActivity(i);
            } else if (position == 5) {

            }
            }
        });
    }
    //初始化环境系统GridView
    private void initEnvSystemGrid(){
        // references to our images
        Integer[] images = {
                R.drawable.system_status_temperature, R.drawable.system_status_water,
                R.drawable.system_status_air, R.drawable.system_status_cabinet,
                R.drawable.system_status_empty, R.drawable.system_status_empty
        };
        // texts of images
        String[] texts = { "温湿度", "漏水", "精密空调", "机柜温度", "", "" };

        GridView gridView = (GridView) findViewById(R.id.grid_env_system);
        gridView.setAdapter(new SystemStatusGridAdapter(gridView, this, images, texts));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) {
                    Intent i = new Intent(context, Temperature.class);
                    startActivity(i);
                } else if (position == 1) {
                    Intent i = new Intent(context, Water.class);
                    startActivity(i);
                } else if (position == 2) {
                    Intent i = new Intent(context, PrecisionAir.class);
                    startActivity(i);
                } else if (position == 3) {
                    Intent i = new Intent(context, Cabinet.class);
                    startActivity(i);
                } else if (position == 4) {

                } else if (position == 5) {

                }
            }
        });
    }
    //初始化安防系统GridView
    private void initSafeSystemGrid(){
        // references to our images
        Integer[] images = {
                R.drawable.system_status_video, R.drawable.system_status_door,
                R.drawable.system_status_smoke
        };
        // texts of images
        String[] texts = { "视频系统", "门禁系统", "烟感" };

        GridView gridView = (GridView) findViewById(R.id.grid_safe_system);
        gridView.setAdapter(new SystemStatusGridAdapter(gridView, this, images, texts));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) {
                    Intent i = new Intent(context, VideoSystem.class);
                    startActivity(i);
                } else if (position == 1) {

                } else if (position == 2) {
                    Intent i = new Intent(context, Smoke.class);
                    startActivity(i);
                }
            }
        });
    }
}
