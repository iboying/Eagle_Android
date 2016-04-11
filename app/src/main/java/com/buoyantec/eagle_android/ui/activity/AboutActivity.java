package com.buoyantec.eagle_android.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pgyersdk.feedback.PgyFeedback;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

public class AboutActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView subToolbarTitle;
    private LinearLayout update_version;
    private LinearLayout feedback;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_about);
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        update_version = getViewById(R.id.update_version);
        feedback = getViewById(R.id.feedback);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_version:
                checkVersion();
                break;
            case R.id.feedback:
                PgyFeedback.getInstance().showDialog(AboutActivity.this);
                break;
        }
    }

    // 检测更新
    public void checkVersion() {
        PgyUpdateManager.register(this, new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {
                showToast("已经是最新版本");
            }

            @Override
            public void onUpdateAvailable(String result) {
                // 将新版本信息封装到AppBean中
                final AppBean appBean = getAppBeanFromString(result);
                new AlertDialog.Builder(AboutActivity.this)
                    .setTitle("更新提醒")
                    .setMessage(appBean.getReleaseNote())
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startDownloadTask(AboutActivity.this, appBean.getDownloadURL());
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
            }
        });
    }
}
