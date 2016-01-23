package com.buoyantec.eagle_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.adapter.MainListAdapter;

public class WarnMessages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn_messages);
        //sub_toolbar
        initToolbar();
        //ListView
        initListView();
    }

    private void initListView() {
        // references to our images
        Integer[] images = {
                R.drawable.system_status_air, R.drawable.system_status_air,
                R.drawable.system_status_temperature
        };
        // texts of images
        final String[] texts = { "空调1告警", "空调2告警", "温湿度系统" };

        ListView listView = (ListView) findViewById(R.id.warn_messages_listView);
        listView.setAdapter(new MainListAdapter(listView, this, images, texts));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(WarnMessages.this, WarnDetail.class);
                i.putExtra("title", texts[position]);
                startActivity(i);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sub_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView subToolbarTitle = (TextView) findViewById(R.id.sub_toolbar_title);
        subToolbarTitle.setText("告警信息");
    }

}
