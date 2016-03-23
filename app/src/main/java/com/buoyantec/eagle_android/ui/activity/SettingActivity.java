package com.buoyantec.eagle_android.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.tencent.android.tpush.XGPushManager;

public class SettingActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView subToolbarTitle;
    private Switch receiveMsg;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        receiveMsg = getViewById(R.id.settingSwitch);
    }

    @Override
    protected void setListener() {
        receiveMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                if (receiveMsg.isChecked()) {
                    XGPushManager.registerPush(context);
                    showToast("设置成功");
                } else {
                    XGPushManager.unregisterPush(context);
                    showToast("设置成功");
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initToolbar();
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        subToolbarTitle.setText("设 置");
    }

}
