package com.buoyantec.eagle_android.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.buoyantec.eagle_android.model.PointAlarm;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Callback;
import retrofit2.Response;

public class ReceiverPush extends BaseActivity {
    private Toolbar toolbar;
    private TextView subToolbarTitle;
    private TextView deviceName;
    private TextView info;
    private TextView status;
    private TextView type;
    private TextView alarmTime;
    private TextView finishTime;
    private TextView user;
    private TextView confirmTime;
    private Button confirmButton;

    private Integer id;
    private SharedPreferences sp;
    private Context context;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_receiver_push);
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        // 告警设备名称(device_name)
        deviceName = getViewById(R.id.push_device_name);
        // 告警信息(comment)
        info = getViewById(R.id.push_alarm_info);
        // 状态(meaning)
        status = getViewById(R.id.push_alarm_status);
        // 类型(type)
        type = getViewById(R.id.push_alarm_type);
        // 告警时间(updated_at)
        alarmTime = getViewById(R.id.push_alarm_time);
        // 解除时间(checked_at)
        finishTime = getViewById(R.id.push_alarm_finish_time);
        // 操作员(checked_user)
        user = getViewById(R.id.push_alarm_user);
        // 确认时间
        confirmTime = getViewById(R.id.push_alarm_confirm_time);
        // 确认按钮
        confirmButton = getViewById(R.id.push_alarm_confirm_button);
        sp  = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        context = getApplicationContext();
    }

    @Override
    protected void setListener() {}

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initToolBar();
        // 接收推送消息,更新UI
        setData();
        // 确认时间
        confirmAlarm();
    }



    private void initToolBar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        subToolbarTitle.setText("推送告警信息");
    }

    private void setData() {
        // custom_content = {
        // "id":3210406,
        // "is_checked":true,
        // "updated_at":"2016-03-29T14:50:42.000Z",
        // "device_name":"APC 空调",
        // "alarm_value":"",
        // "state":0,
        // "created_at":"2016-03-02T08:39:57.000Z",
        // "pid":null,"meaning":"分",
        // "type":"digital",
        // "comment":"APC空调-开机状态",
        // "checked_at":null,
        // "point_id":4570
        // }

        // 显示载入
        showLoadingDialog("正在加载...");

        String customContent = sp.getString("custom_content", null);
        Gson gson = new Gson();
        id = gson.fromJson(customContent, PointAlarm.class).getId();
        // 从推送中获取告警id,调用接口判断告警是否已确认
        // 已确认: disabled掉按钮
        // 未确认: 正常显示
        if (customContent != null) {
            if (!String.valueOf(id).isEmpty()) {
                mEngine.getAlarm(id).enqueue(new Callback<PointAlarm>() {
                    @Override
                    public void onResponse(Response<PointAlarm> response) {
                        if (response.code() == 200) {
                            PointAlarm pointAlarm = response.body();
                            // 给UI设置数据
                            deviceName.setText(pointAlarm.getDeviceName());
                            info.setText(pointAlarm.getComment());
                            status.setText(pointAlarm.getMeaning());
                            type.setText(pointAlarm.getType());
                            alarmTime.setText(pointAlarm.getUpdatedAt());
                            if (!String.valueOf(pointAlarm.getState()).isEmpty() && pointAlarm.getUpdatedAt() != null) {
                                if (pointAlarm.getState() == 0) {
                                    finishTime.setText(pointAlarm.getUpdatedAt());
                                } else {
                                    finishTime.setText("");
                                }
                            }
                            user.setText(pointAlarm.getCheckedUser());
                            confirmTime.setText(pointAlarm.getCheckedAt());
                            // 判断是否已被确认
                            if (pointAlarm.getCheckedUser().length() > 0) {
                                confirmButton.setText("已被确认");
                                confirmButton.setClickable(false);
                            }
                            // 隐藏dialog
                            dismissLoadingDialog();
                            Log.i("推送->告警详情", context.getString(R.string.getSuccess) + response.code());
                        } else {
                            // 隐藏dialog
                            dismissLoadingDialog();
                            showToast(context.getString(R.string.getDataFailed));
                            Log.i("推送->告警详情", context.getString(R.string.getFailed) + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // 隐藏dialog
                        dismissLoadingDialog();
                        showToast(context.getString(R.string.netWorkFailed));
                        Log.i("推送->告警详情", context.getString(R.string.linkFailed));
                    }
                });
            }
        }
    }

    private void confirmAlarm() {
        if (confirmButton.isClickable()) {
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoadingDialog("正在确认...");
                    mEngine.checkAlarm(id).enqueue(new Callback<HashMap<String, String>>() {
                        @Override
                        public void onResponse(Response<HashMap<String, String>> response) {
                            HashMap<String, String> data = response.body();
                            if (data.get("result").equals("处理成功")) {
                                dismissLoadingDialog();
                                // 改变item操作员
                                user.setText(sp.getString("name", ""));
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                Date currentDate = new Date(System.currentTimeMillis());
                                String str = simpleDateFormat.format(currentDate);
                                confirmTime.setText(str);
                                showToast("确认成功");
                            } else {
                                dismissLoadingDialog();
                                showToast("确认失败");
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            dismissLoadingDialog();
                            showToast("网络连接失败");
                        }
                    });
                }
            });
        }
    }

}
