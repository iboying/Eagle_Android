package com.buoyantec.eagle_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.adapter.WarnDetailListAdapter;

public class WarnDetail extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn_detail);
        initToolbar();
        //加载告警信息列表
        initListView();
    }

    private void initListView() {
        String[] texts = {"低温告警状态","冷凝水泵输出","冷冻水进水故障状态",
                "机架进风温度1故障状态","气流丢失告警状态"};
        Integer[] data = {40, 50, 60, 70, 80};

        ListView listView = (ListView) findViewById(R.id.warn_detail_listView);
        listView.setAdapter(new WarnDetailListAdapter(listView, this, texts, data));
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
