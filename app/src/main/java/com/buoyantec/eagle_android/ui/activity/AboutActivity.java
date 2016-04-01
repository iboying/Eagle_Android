package com.buoyantec.eagle_android.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pgyersdk.feedback.PgyFeedback;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
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
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PgyFeedback.getInstance().showDialog(AboutActivity.this);
            }
        });
        update_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PgyUpdateManager.register(AboutActivity.this, new UpdateManagerListener() {
                    @Override
                    public void onNoUpdateAvailable() {
                        showToast("有新版本");
                    }

                    @Override
                    public void onUpdateAvailable(String s) {
                        showToast("已是最新版本");
                    }
                });
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
        subToolbarTitle.setText("关 于");
    }

}
