package com.buoyantec.eagle_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.adapter.CabinetListAdapter;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class Cabinet extends AppCompatActivity {
    private SharedPreferences sp;
    private Integer room_id;
    private String sub_sys_name;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        //加载字体图标
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_cabinet);
        //初始化toolbar
        initToolbar();
        //初始化list
        initListView();
    }

    private void init() {
        Intent i = getIntent();
        // TODO: 16/2/7 默认值的问题
        room_id = i.getIntExtra("room_id", 1);
        sub_sys_name = i.getStringExtra("sub_sys_name");
        sp = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        context = getApplicationContext();
    }

    private void initListView() {
        // item数据
        String[] names = {"d2", "d3", "d4"};
        Integer[][] data = {{22, 40}, {22, 50}, {24, 30}};
        ListView listView = (ListView) findViewById(R.id.cabinet_listView);
        listView.setAdapter(new CabinetListAdapter(listView, this, names, data));
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sub_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView subToolbarTitle = (TextView) findViewById(R.id.sub_toolbar_title);
        subToolbarTitle.setText(sub_sys_name);
    }
}
