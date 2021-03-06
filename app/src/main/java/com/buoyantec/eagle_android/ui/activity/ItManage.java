package com.buoyantec.eagle_android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.support.v7.app.ActionBar;

import com.buoyantec.eagle_android.ui.base.BaseActivity;

public class ItManage extends BaseActivity {
    private Toolbar toolbar;
    private TextView subToolbarTitle;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_it_manage);
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initToolbar();
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
