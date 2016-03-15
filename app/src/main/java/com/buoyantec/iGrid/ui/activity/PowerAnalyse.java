package com.buoyantec.iGrid.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class PowerAnalyse extends BaseActivity {
    private Toolbar toolbar;
    private TextView subToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_analyse);
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        initToolbar();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        subToolbarTitle.setText(getIntent().getStringExtra("title"));
    }

}
