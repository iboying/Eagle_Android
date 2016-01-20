package com.buoyantec.eagle_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class SystemStatus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_status);
        //sub_toolbar
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

        GridView gridview = (GridView) findViewById(R.id.grid_power_system);
        gridview.setAdapter(new SubGridAdapter(gridview, this, images, texts));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            if (position == 0) {
                Intent i = new Intent(SystemStatus.this, PowerDistribution.class);
                startActivity(i);
            } else if (position == 1) {
                Intent i = new Intent(SystemStatus.this, UpsSystem.class);
                startActivity(i);
            } else if (position == 2) {
                Intent i = new Intent(SystemStatus.this, Box.class);
                startActivity(i);
            } else if (position == 3) {

            } else if (position == 4) {
                Intent i = new Intent(SystemStatus.this, Battery.class);
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

        GridView gridview = (GridView) findViewById(R.id.grid_env_system);
        gridview.setAdapter(new SubGridAdapter(gridview, this, images, texts));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) {
                    Intent i = new Intent(SystemStatus.this, Temperature.class);
                    startActivity(i);
                } else if (position == 1) {
                    Intent i = new Intent(SystemStatus.this, Water.class);
                    startActivity(i);
                } else if (position == 2) {
                    Intent i = new Intent(SystemStatus.this, PrecisionAir.class);
                    startActivity(i);
                } else if (position == 3) {
                    Intent i = new Intent(SystemStatus.this, Cabinet.class);
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

        GridView gridview = (GridView) findViewById(R.id.grid_safe_system);
        gridview.setAdapter(new SubGridAdapter(gridview, this, images, texts));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) {

                } else if (position == 1) {

                } else if (position == 2) {

                } else if (position == 3) {

                } else if (position == 4) {

                } else if (position == 5) {

                }
            }
        });
    }
}
