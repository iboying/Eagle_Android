package com.buoyantec.eagle_android.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReceiverPush extends BaseActivity {
    private Toolbar toolbar;
    private TextView subToolbarTitle;

    private TextView deviceName;
    private TextView info;
    private TextView status;
    private TextView level;
    private TextView alarmTime;
    private TextView finishTime;
    private TextView user;
    private TextView confirmTime;
    private Button confirmButton;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_receiver_push);
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        deviceName = getViewById(R.id.push_device_name);
        info = getViewById(R.id.push_alarm_info);
        status = getViewById(R.id.push_alarm_status);
        level = getViewById(R.id.push_alarm_level);
        alarmTime = getViewById(R.id.push_alarm_time);
        finishTime = getViewById(R.id.push_alarm_finish_time);
        user = getViewById(R.id.push_alarm_user);
        confirmTime = getViewById(R.id.push_alarm_confirm_time);
        confirmButton = getViewById(R.id.confirm_button);
    }

    @Override
    protected void setListener() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("已确认");
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initToolBar();
    }

    private void initToolBar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        subToolbarTitle.setText("推送告警信息");
    }
}
