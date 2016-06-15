package com.buoyantec.eagle_android.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.adapter.WarnDetailListAdapter;
import com.buoyantec.eagle_android.model.Alarm;
import com.buoyantec.eagle_android.model.PointAlarm;
import com.joanzapata.iconify.widget.IconTextView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;
import retrofit2.Callback;
import retrofit2.Response;

public class WarnDetail extends BaseActivity {
    private String title;
    private Integer subSystemId;
    private Context context;
    // 组件
    private CircleProgressBar circleProgressBar;
    private ListView listView;
    private Button addMore;
    private Toolbar toolbar;
    private TextView subToolbarTitle;
    // 列表数据
    private List<Integer> ids = new ArrayList<>();
    private List<String> deviceNames = new ArrayList<>();
    private List<String> pointNames = new ArrayList<>();
    private List<String> meanings = new ArrayList<>();
    private List<String> types = new ArrayList<>();
    private List<String> reportedAt = new ArrayList<>();
    private List<String> clearedAt = new ArrayList<>();
    private List<String> checkedUser = new ArrayList<>();
    private List<String> checkedAt = new ArrayList<>();
    private List<Boolean> isChecked = new ArrayList<>();
    private List<Boolean> isCleared = new ArrayList<>();
    // 模态框
    private MaterialDialog materialDialog;
    private View dialogDetail;
    private TextView alarmDeviceName;
    private TextView alarmPointName;
    private TextView alarmMeaning;
    private TextView alarmType;
    private TextView alarmReportedAt;
    private TextView alarmClearedAt;
    private TextView alarmCheckedUser;
    private TextView alarmCheckedAt;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_warn_detail);
        init();
    }

    @Override
    protected void setListener() {}

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        // 加载工具条
        initToolbar();
        //加载告警信息列表
        initListView(1);
    }

    private void init() {
        Intent i = getIntent();
        subSystemId = i.getIntExtra("subSystemId", 1);

        title = i.getStringExtra("title");
        context = this;
        // 组件
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        circleProgressBar = getViewById(R.id.progressBar);
        circleProgressBar.setVisibility(View.VISIBLE);
        // 列表
        listView = getViewById(R.id.warn_detail_listView);
        View footer = getLayoutInflater().inflate(R.layout.list_view_footer, null);
        listView.addFooterView(footer);
        addMore = getViewById(R.id.list_more);
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        subToolbarTitle.setText(title);
    }

    private void initListView(Integer page) {
        Integer room_id = sp.getInt("current_room_id", 1);
        setEngine(sp);
        mEngine.getWarnMessages(room_id, subSystemId, 0, page).enqueue(new Callback<Alarm>() {
            @Override
            public void onResponse(Response<Alarm> response) {
                // 初始化变量
                Alarm alarm = response.body();
                final Integer total_pages = alarm.getTotalPages();
                final Integer current_page = alarm.getCurrentPage();

                if (response.code() == 200) {
                    // 获取数据
                    final List<PointAlarm> pointAlarms = alarm.getPointAlarms();
                    for (PointAlarm pointAlarm : pointAlarms) {
                        // 告警记录id(id)
                        ids.add(pointAlarm.getId());
                        // 设备名称
                        deviceNames.add(pointAlarm.getDeviceName());
                        // 告警点名称(point_name)
                        pointNames.add(pointAlarm.getPointName());
                        // 描述(meaning)
                        meanings.add(pointAlarm.getMeaning());
                        // 类型(Type)
                        types.add(pointAlarm.getType());
                        // 告警时间(updated_at)
                        reportedAt.add(pointAlarm.getReportedAt());
                        // 解除时间(cleared_at)
                        clearedAt.add(pointAlarm.getClearedAt());
                        // 操作员(checked_user)
                        checkedUser.add(pointAlarm.getCheckedUser());
                        // 确认时间(确认后修改确认时间)
                        checkedAt.add(pointAlarm.getCheckedAt());

                        // 是否已确认(is_checked)
                        isChecked.add(pointAlarm.getChecked());
                        // 是否已清除(is_cleared)
                        isCleared.add(pointAlarm.getCleared());
                    }

                    // ListView填装数据
                    BaseAdapter adapter = new WarnDetailListAdapter(context, pointNames, meanings, reportedAt, isChecked);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                            // 模态框
                            materialDialog = new MaterialDialog(context);
                            dialogDetail = View.inflate(context, R.layout.alarm_detail, null);
                            // 获取组件
                            alarmDeviceName = (TextView) dialogDetail.findViewById(R.id.alarm_device_name);
                            alarmPointName = (TextView) dialogDetail.findViewById(R.id.alarm_point_name);
                            alarmMeaning = (TextView) dialogDetail.findViewById(R.id.alarm_meaning);
                            alarmType = (TextView) dialogDetail.findViewById(R.id.alarm_type);
                            alarmReportedAt = (TextView) dialogDetail.findViewById(R.id.alarm_reported_at);
                            alarmClearedAt = (TextView) dialogDetail.findViewById(R.id.alarm_cleared_at);
                            alarmCheckedUser = (TextView) dialogDetail.findViewById(R.id.alarm_checked_user);
                            alarmCheckedAt = (TextView) dialogDetail.findViewById(R.id.alarm_checked_at);
                            // modal: 取消
                            materialDialog.setContentView(dialogDetail).setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    materialDialog.dismiss();
                                }
                            });

                            // 向modal传入数据
                            alarmDeviceName.setText(deviceNames.get(position));
                            alarmPointName.setText(pointNames.get(position));
                            alarmMeaning.setText(meanings.get(position));
                            alarmType.setText(types.get(position));
                            alarmReportedAt.setText(reportedAt.get(position));
                            if (isCleared.get(position)) {
                                alarmClearedAt.setText(clearedAt.get(position));
                            } else {
                                alarmClearedAt.setText("未解除");
                            }
                            alarmCheckedUser.setText(checkedUser.get(position));
                            alarmCheckedAt.setText(checkedAt.get(position));

                            // 确认告警事件
                            if (!isChecked.get(position)) {
                                materialDialog.setPositiveButton("确认告警", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // 确认告警
                                        checkAlarm(view, ids.get(position), position);
                                    }
                                });
                            } else {
                                materialDialog.setPositiveButton("", null);
                            }
                            materialDialog.show();
                        }
                    });

                    // 判断是否有更多数据
                    if (current_page < total_pages) {
                        addMore.setClickable(true);
                        addMore.setText("加载更多");
                        addMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addMore.setText("正在加载...");
                                addMore.setClickable(false);
                                initListView(current_page + 1);
                            }
                        });
                    } else {
                        addMore.setVisibility(View.GONE);
                    }

                    circleProgressBar.setVisibility(View.GONE);
                    Log.i("系统告警->详情", context.getString(R.string.getSuccess));
                } else {
                    // 输出非201时的错误信息
                    circleProgressBar.setVisibility(View.GONE);
                    addMore.setClickable(true);
                    addMore.setText("点击重新加载");
                    Log.i("系统告警->详情", context.getString(R.string.getFailed));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                circleProgressBar.setVisibility(View.GONE);
                addMore.setClickable(true);
                addMore.setText("点击重新加载");
                Log.i("系统告警->详情", context.getString(R.string.linkFailed));
            }
        });
    }

    // 确认告警
    private void checkAlarm(View view, Integer id, final int position) {
        materialDialog.dismiss();
        showLoadingDialog("正在确认...");
        final IconTextView icon = (IconTextView) view.findViewById(R.id.device_detail_checked);
        setEngine(sp);
        mEngine.checkAlarm(id).enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Response<HashMap<String, String>> response) {
                HashMap<String, String> data = response.body();
                if (data.get("result").equals("处理成功")) {
                    dismissLoadingDialog();
                    // 改变item操作员
                    checkedUser.set(position, getSharedPreferences("foobar", Activity.MODE_PRIVATE).getString("name", null));
                    icon.setTextColor(context.getResources().getColor(R.color.gray));
                    // 改变确认时间和确认状态
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date currentDate = new Date(System.currentTimeMillis());
                    String str = simpleDateFormat.format(currentDate);
                    checkedAt.set(position, str);
                    isChecked.set(position, true);
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
}
