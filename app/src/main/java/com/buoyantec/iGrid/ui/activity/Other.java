package com.buoyantec.iGrid.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class Other extends BaseActivity {
    private Toolbar toolbar;
    private TextView subToolbarTitle;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_other);
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        //sub_toolbar
        initToolbar();
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
