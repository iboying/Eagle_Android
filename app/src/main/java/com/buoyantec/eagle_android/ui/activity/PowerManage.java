package com.buoyantec.eagle_android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.adapter.StandardListAdapter;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.List;

public class PowerManage extends BaseActivity {
    private Toolbar toolbar;
    private TextView subToolbarTitle;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_power_manage);
        Iconify.with(new FontAwesomeModule());
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        // 初始化toolbar
        initToolbar();
        // 初始化list
        initListView();
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        subToolbarTitle.setText(getIntent().getStringExtra("title"));
    }

    /**
     * Home -> 能效管理
     * 暂无数据
     */
    private void initListView() {
        // references to our images
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.power_manage_pue);
        images.add(R.drawable.power_manage_analyse);
        images.add(R.drawable.power_manage_data);

        // texts of images
        List<String> names = new ArrayList<>();
        names.add("P U E");
        names.add("能 效 分 析");
        names.add("用 电 数 据");

        ListView listView = getViewById(R.id.power_manage_listView);
        listView.setAdapter(new StandardListAdapter(listView, this, images, names));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) {
                    Intent i = new Intent(PowerManage.this, Pue.class);
                    startActivity(i);
                } else if (position == 1) {
                    Intent i = new Intent(PowerManage.this, PowerAnalyse.class);
                    startActivity(i);
                } else if (position == 2) {
                    Intent i = new Intent(PowerManage.this, ElectricityData.class);
                    startActivity(i);
                }
            }
        });
    }
}
