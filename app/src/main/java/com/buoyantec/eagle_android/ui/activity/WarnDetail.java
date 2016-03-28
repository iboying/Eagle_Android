package com.buoyantec.eagle_android.ui.activity;

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
import com.buoyantec.eagle_android.model.Results;
import com.joanzapata.iconify.widget.IconTextView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;
import retrofit2.Callback;
import retrofit2.Response;

public class WarnDetail extends BaseActivity {
    private String title;
    private Integer device_id;
    private Context context;
    // 组件
    private CircleProgressBar circleProgressBar;
    private ListView listView;
    private Button addMore;
    private Toolbar toolbar;
    private TextView subToolbarTitle;
    // 列表数据
    private List<Integer> point_ids = new ArrayList<>();
    private List<String> comments = new ArrayList<>();
    private List<String> types = new ArrayList<>();
    private List<String> times = new ArrayList<>();
    private List<String> alarms = new ArrayList<>();
    private List<String> status = new ArrayList<>();
    private List<Boolean> checked = new ArrayList<>();
    private List<String> userNames = new ArrayList<>();
    // 模态框
    private MaterialDialog materialDialog;
    private View dialogDetail;
    private TextView info;
    private TextView alarmStatus;
    private TextView level;
    private TextView alarmTime;
    private TextView finishTime;
    private TextView user;
    private TextView confirmTime;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_warn_detail);
        init();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        // 加载工具条
        initToolbar();
        //加载告警信息列表
        initListView(1);
    }

    private void init() {
        Intent i = getIntent();
        device_id = i.getIntExtra("device_id", 1);
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
        // 模态框
        materialDialog = new MaterialDialog(this);
        dialogDetail = View.inflate(context, R.layout.alarm_detail, null);
        info = (TextView) dialogDetail.findViewById(R.id.push_alarm_info);
        alarmStatus = (TextView) dialogDetail.findViewById(R.id.push_alarm_status);
        level = (TextView) dialogDetail.findViewById(R.id.push_alarm_level);
        alarmTime = (TextView) dialogDetail.findViewById(R.id.push_alarm_time);
        finishTime = (TextView) dialogDetail.findViewById(R.id.push_alarm_finish_time);
        user = (TextView) dialogDetail.findViewById(R.id.push_alarm_user);
        confirmTime = (TextView) dialogDetail.findViewById(R.id.push_alarm_confirm_time);
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
        mEngine.getWarnMessages(device_id, 2, page).enqueue(new Callback<Alarm>() {
            @Override
            public void onResponse(Response<Alarm> response) {
                // 初始化变量
                Alarm alarm = response.body();
                int code = response.code();

                final Integer total_pages = alarm.getTotalPages();
                final Integer current_page = alarm.getCurrentPage();

                if (code == 200) {
                    // 获取数据
                    List<PointAlarm> pointAlarms = alarm.getPointAlarms();
                    for (PointAlarm pointAlarm : pointAlarms) {
                        point_ids.add(pointAlarm.getPointId());
                        comments.add(pointAlarm.getComment());
                        times.add(pointAlarm.getUpdatedAt());
                        alarms.add(pointAlarm.getAlarmValue());
                        status.add(pointAlarm.getMeaning());
                        checked.add(pointAlarm.isChecked());
                        userNames.add(pointAlarm.getCheckedUser());
                        if (pointAlarm.getType() == null) {
                            types.add("开关量告警");
                        } else {
                            types.add(pointAlarm.getType());
                        }

                    }

                    circleProgressBar.setVisibility(View.GONE);

                    // ListView填装数据
                    BaseAdapter adapter = new WarnDetailListAdapter(context, comments, types, times, alarms, status, checked);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                            // 传入数据
                            info.setText("信 息: "+comments.get(position));
                            alarmStatus.setText("状 态: "+status.get(position));
                            level.setText("类 型: "+types.get(position));
                            alarmTime.setText("告警时间: "+times.get(position));
                            finishTime.setText("解除时间: "+"2016-3-3");
                            user.setText("操作员: "+userNames.get(position));
                            confirmTime.setText("确认时间: "+"2015-3-3");

                            materialDialog.setContentView(dialogDetail)
                                .setPositiveButton("确认告警", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // 确认告警
                                        checkAlarm(view, point_ids.get(position));
                                    }
                                })
                                .setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        materialDialog.dismiss();
                                    }
                                });
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

                    Log.i("设备告警->详情", context.getString(R.string.getSuccess) + code);
                } else {
                    // 输出非201时的错误信息
                    circleProgressBar.setVisibility(View.GONE);
                    addMore.setClickable(true);
                    addMore.setText("点击重新加载");
                    Log.i("设备告警->详情", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                circleProgressBar.setVisibility(View.GONE);
                addMore.setClickable(true);
                addMore.setText("点击重新加载");
                Log.i("设备告警->详情", context.getString(R.string.linkFailed));
            }
        });
    }

    // 确认告警
    private void checkAlarm(View view, Integer point_id) {
        materialDialog.dismiss();
        showLoadingDialog("正在确认...");
        final IconTextView icon = (IconTextView) view.findViewById(R.id.device_detail_checked);
        mEngine.checkAlarm(point_id).enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Response<Results> response) {
                if (response.code() == 200) {
                    dismissLoadingDialog();
                    icon.setTextColor(context.getResources().getColor(R.color.gray));
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
