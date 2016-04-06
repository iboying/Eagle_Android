package com.buoyantec.eagle_android.ui.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pgyersdk.feedback.PgyFeedback;
import com.pgyersdk.update.PgyUpdateManager;

public class AboutActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView subToolbarTitle;
    private LinearLayout update_version;
    private LinearLayout feedback;
    private TextView versionName;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_about);
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        update_version = getViewById(R.id.update_version);
        feedback = getViewById(R.id.feedback);
        versionName = getViewById(R.id.versionNmae);
    }

    @Override
    protected void setListener() {
        // 问题反馈
        feedback.setOnClickListener(this);
        // 检测更新
        update_version.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initToolbar();
        // 显示版本号
        versionName.setText(getAppVersionName(this));
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        subToolbarTitle.setText("关 于");
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        // Integer versioncode;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            // versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_version:
                PgyUpdateManager.register(this);
                break;
            case R.id.feedback:
                PgyFeedback.getInstance().showDialog(AboutActivity.this);
                break;
        }
    }
}
