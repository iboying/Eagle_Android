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
    private Integer device_id;
    private Context context;
    // 组件
    private CircleProgressBar circleProgressBar;
    private ListView listView;
    private Button addMore;
    private Toolbar toolbar;
    private TextView subToolbarTitle;
    // 列表数据
    private List<Integer> ids = new ArrayList<>();
    private List<String> pointNames = new ArrayList<>();
    private List<String> comments = new ArrayList<>();
    private List<String> types = new ArrayList<>();
    private List<String> alarmTimes = new ArrayList<>();
    private List<String> checkedAt = new ArrayList<>();
    private List<String> alarms = new ArrayList<>();
    private List<String> meanings = new ArrayList<>();
    private List<Boolean> isChecked = new ArrayList<>();
    private List<String> checkedUser = new ArrayList<>();
    private List<Integer> state = new ArrayList<>();
    private List<String> confirmDate = new ArrayList<>();
    // 模态框
    private MaterialDialog materialDialog;
    private View dialogDetail;
    private TextView alarmPoint;
    private TextView info;
    private TextView alarmStatus;
    private TextView type;
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
        alarmPoint = (TextView) dialogDetail.findViewById(R.id.push_point_name);
        alarmStatus = (TextView) dialogDetail.findViewById(R.id.push_alarm_status);
        type = (TextView) dialogDetail.findViewById(R.id.push_alarm_type);
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

    /**
     * 解除时间：
     * 如果state不为0，则空白，因为此时告警未解除。
     * 如果state为0，则取updated_at，作为告警解除时间
     */
    private void initListView(Integer page) {
        setEngine(sp);
        mEngine.getWarnMessages(device_id, 0, page).enqueue(new Callback<Alarm>() {
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
                        // 告警点id(id)
                        ids.add(pointAlarm.getId());
                        // 告警点名称(point_name)
                        pointNames.add(pointAlarm.getPointName());
                        // 信息(comment)
                        comments.add(pointAlarm.getComment());
                        // 告警时间(updated_at)
                        alarmTimes.add(pointAlarm.getUpdatedAt());
                        // 告警解除时间(checked_at)
                        checkedAt.add(pointAlarm.getCheckedAt());
                        // 详情(alarm_value)
                        alarms.add(pointAlarm.getAlarmValue());
                        // 状态(meaning)
                        meanings.add(pointAlarm.getMeaning());
                        // 操作员(checked_user)
                        checkedUser.add(pointAlarm.getCheckedUser());
                        // 是否已确认(is_checked)
                        if (pointAlarm.getCheckedUser().equals("")) {
                            isChecked.add(false);
                        } else {
                            isChecked.add(true);
                        }
                        // 标识(state)
                        state.add(pointAlarm.getState());
                        // 类型(type)
                        types.add(pointAlarm.getType());
                        // 确认时间(此字段根据state和update判断,此处声明用于不获取数据的情况下,确认后修改确认时间)
                        confirmDate.add("");

                    }

                    circleProgressBar.setVisibility(View.GONE);

                    // ListView填装数据
                    BaseAdapter adapter = new WarnDetailListAdapter(context, pointNames, comments, alarmTimes, isChecked);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                            materialDialog = new MaterialDialog(context);
                            dialogDetail = View.inflate(context, R.layout.alarm_detail, null);
                            info = (TextView) dialogDetail.findViewById(R.id.push_alarm_info);
                            alarmPoint = (TextView) dialogDetail.findViewById(R.id.push_point_name);
                            alarmStatus = (TextView) dialogDetail.findViewById(R.id.push_alarm_status);
                            type = (TextView) dialogDetail.findViewById(R.id.push_alarm_type);
                            alarmTime = (TextView) dialogDetail.findViewById(R.id.push_alarm_time);
                            finishTime = (TextView) dialogDetail.findViewById(R.id.push_alarm_finish_time);
                            user = (TextView) dialogDetail.findViewById(R.id.push_alarm_user);
                            confirmTime = (TextView) dialogDetail.findViewById(R.id.push_alarm_confirm_time);
                            // 传入数据
                            alarmPoint.setText(pointNames.get(position));
                            info.setText(comments.get(position));
                            alarmStatus.setText(meanings.get(position));
                            type.setText(types.get(position));
                            alarmTime.setText(alarmTimes.get(position));
                            user.setText(checkedUser.get(position));
                            if (checkedAt.get(position) == null) {
                                confirmTime.setText(confirmDate.get(position));
                            } else {
                                confirmTime.setText(checkedAt.get(position));
                            }
                            if (state.get(position) == 0) {
                                finishTime.setText(alarmTimes.get(position));
                            } else {
                                finishTime.setText("");
                            }
                            materialDialog.setContentView(dialogDetail)
                                    .setNegativeButton("取消", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            materialDialog.dismiss();
                                        }
                                    });

                            String checkedName = checkedUser.get(position);
                            if (checkedName.equals("")) {
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
                    // 改变确认时间
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date currentDate = new Date(System.currentTimeMillis());
                    String str = simpleDateFormat.format(currentDate);
                    confirmDate.set(position, str);
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
