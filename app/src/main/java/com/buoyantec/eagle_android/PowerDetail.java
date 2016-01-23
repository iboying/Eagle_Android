package com.buoyantec.eagle_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;
import android.widget.TextView;

import com.buoyantec.eagle_android.adapter.PowerUpsGridViewAdapter;

public class PowerDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_detail);
        initToolbar();
        //初始化GridView
        initPowerGridView();
        initVoltageGridView();
        initElectricityGridView();
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


    private void initPowerGridView() {
        int item_layout = R.layout.grid_item_green;
        String[] texts = { "有功功率", "无功功率", "视在功率", "频率" };
        Integer[] data = {556,556,556,556};

        GridView gridview = (GridView) findViewById(R.id.power_detail_power_gridView);
        gridview.setAdapter(new PowerUpsGridViewAdapter(gridview, this, item_layout, texts, data));
    }

    private void initVoltageGridView() {
        int item_layout = R.layout.grid_item_orange;
        String[] texts = { "A相电压", "B相电压", "C相电压", "AB相电压", "BC相电压", "CA相电压" };
        Integer[] data = {556,556,556,556,556,556};

        GridView gridview = (GridView) findViewById(R.id.power_detail_voltage_gridView);
        gridview.setAdapter(new PowerUpsGridViewAdapter(gridview, this, item_layout, texts, data));
    }

    private void initElectricityGridView() {
        int item_layout = R.layout.grid_item_purple;
        String[] texts = { "A相电流", "B相电流", "C相电流" };
        Integer[] data = {556,556,556};

        GridView gridview = (GridView) findViewById(R.id.power_detail_electricity_gridView);
        gridview.setAdapter(new PowerUpsGridViewAdapter(gridview, this, item_layout, texts, data));
    }
}
