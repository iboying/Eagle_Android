package com.buoyantec.eagle_android.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.buoyantec.eagle_android.ui.base.BaseActivity;
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

        String push = sp.getString("push", "");
        if (push.equals("")) {
            System.out.println(sp.getString("push", null)+"----");
            receiveMsg.setChecked(true);
        } else{
            System.out.println(sp.getString("push", null)+"=====");
            receiveMsg.setChecked(false);
        }
    }

    @Override
    protected void setListener() {
        final SharedPreferences.Editor editor = sp.edit();

        receiveMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                if (receiveMsg.isChecked()) {
                    // 注册
                    XGPushManager.registerPush(context);
                    // 修改配置文件
                    editor.putString("push", "");
                    editor.apply();
                    showToast("接收推送设置成功");
                } else {
                    // 反注册
                    XGPushManager.unregisterPush(context);
                    // 修改配置文件
                    editor.putString("push", "no");
                    editor.apply();
                    showToast("拒绝推送设置成功");
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
