package com.buoyantec.eagle_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class UpsDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ups_detail);
        initToolbar();
        //初始化GridView
        initInputGridView();
        initOutputGridView();
        initBatteryGridView();
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


    private void initInputGridView() {
        int item_layout = R.layout.grid_item_orange;
        String[] texts = { "A相电压", "B相电压", "C相电压", "A相电流", "B相电流", "C相电流" };
        Integer[] data = {556,556,556,556,556,556};

        GridView gridview = (GridView) findViewById(R.id.ups_input_gridView);
        gridview.setAdapter(new PowerUpsGridViewAdapter(gridview, this, item_layout, texts, data));
    }

    private void initOutputGridView() {
        int item_layout = R.layout.grid_item_purple;
        String[] texts = { "A相电压", "B相电压", "C相电压", "A相电流", "B相电流", "C相电流" };
        Integer[] data = {556,556,556,556,556,556};

        GridView gridview = (GridView) findViewById(R.id.ups_output_gridView);
        gridview.setAdapter(new PowerUpsGridViewAdapter(gridview, this, item_layout, texts, data));
    }

    private void initBatteryGridView() {
        int item_layout = R.layout.grid_item_green;
        String[] texts = { "容量", "温度", "电压", "电流" };
        Integer[] data = {556,556,556,556};

        GridView gridview = (GridView) findViewById(R.id.ups_battery_gridView);
        gridview.setAdapter(new PowerUpsGridViewAdapter(gridview, this, item_layout, texts, data));
    }

}
