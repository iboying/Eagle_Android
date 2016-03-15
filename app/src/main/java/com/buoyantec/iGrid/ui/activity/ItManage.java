package com.buoyantec.iGrid.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.support.v7.app.ActionBar;

public class ItManage extends BaseActivity {
    private Toolbar toolbar;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_it_manage);
        toolbar = getViewById(R.id.sub_toolbar);
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

        TextView subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        subToolbarTitle.setText(getIntent().getStringExtra("title"));
    }
}
