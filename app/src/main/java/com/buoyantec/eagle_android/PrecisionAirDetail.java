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

public class PrecisionAirDetail extends AppCompatActivity {
    private SharedPreferences sp;
    private Integer room_id;
    private Integer id;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        //加载字体图标
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_precision_air_detail);
        //初始化toolbar
        initToolbar();
        //初始化list
        initListView();
    }

    private void init() {
        Intent i = getIntent();
        // TODO: 16/2/7 默认值的问题
        room_id = i.getIntExtra("room_id", 1);
        id = i.getIntExtra("id", 1);
        sp = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        context = getApplicationContext();
    }

    private void initListView() {
        // item数据
        String[] names = {"内风机1输出", "内风机2输出", "内风机3输出", "内风机4输出",
                        "内风机5输出", "内风机6输出", "内风机7输出", "内风机8输出"};
        String[] status = {"关闭","关闭","关闭","关闭","关闭","关闭","关闭","关闭"};
        ListView listView = (ListView) findViewById(R.id.precision_air_detail_listView);
        listView.setAdapter(new CabinetListAdapter(listView, this, names, status));
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
        String title = i.getStringExtra("title");
        subToolbarTitle.setText(title);
    }
}
