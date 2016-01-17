package com.buoyantec.eagle_android;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class PowerDistribution extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载字体图标
        Iconify.with(new FontAwesomeModule());
        //加载布局文件
        setContentView(R.layout.activity_power_distribution);
        //初始化toolbar
        initToolbar();
        //初始化list
        initListView();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sub_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView subToolbarTitle = (TextView) findViewById(R.id.sub_toolbar_title);
        subToolbarTitle.setText("配电系统");
    }

    private void initListView() {
        // references to our images
        Integer image = R.drawable.power_distribution;
        // texts of images
        String[] texts = { "配电柜1", "配电柜2" };
        ListView listView = (ListView) findViewById(R.id.system_status_listView);
        listView.setAdapter(new SystemStatusListAdapter(listView, this, image, texts));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) {

                } else if (position == 1) {

                }
            }
        });
    }
}
