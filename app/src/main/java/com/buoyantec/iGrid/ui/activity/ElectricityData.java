package com.buoyantec.iGrid.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class ElectricityData extends BaseActivity {
    private Toolbar toolbar;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_electricity_data);
        toolbar = getViewById(R.id.sub_toolbar);

        initToolBar();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    private void initToolBar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        subToolbarTitle.setText("用电数据");
    }

}
