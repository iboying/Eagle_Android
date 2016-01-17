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

public class PowerManage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_manage);
        //sub_toolbar
        initToolbar();
        //ListView
        initListView();
        Iconify.with(new FontAwesomeModule());
    }

    private void initListView() {
        // references to our images
        Integer[] images = {
            R.drawable.power_manage_pue, R.drawable.power_manage_analyse,
            R.drawable.power_manage_data
        };
        // texts of images
        String[] texts = { "P U E", "能 效 分 析", "用 电 数 据" };

        ListView listView = (ListView) findViewById(R.id.power_manage_listView);
        listView.setAdapter(new PowerManageListAdapter(listView, this, images, texts));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) {

                } else if (position == 1) {

                } else if (position == 2) {

                }
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
        subToolbarTitle.setText("能效管理");
    }

}
