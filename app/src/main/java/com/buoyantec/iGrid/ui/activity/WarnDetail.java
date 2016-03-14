package com.buoyantec.iGrid.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.iGrid.adapter.WarnDetailListAdapter;
import com.buoyantec.iGrid.model.Alarm;
import com.buoyantec.iGrid.model.PointAlarm;
import com.buoyantec.iGrid.myService.ApiRequest;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarnDetail extends AppCompatActivity {
    private String title;
    private Integer device_id;
    private Context context;
    // 组件
    private CircleProgressBar circleProgressBar;
    private ListView listView;
    private Button addMore;
    // 列表数据
    private List<String> comments = new ArrayList<>();
    private List<String> types = new ArrayList<>();
    private List<String> times = new ArrayList<>();
    private List<String> alarms = new ArrayList<>();
    private List<String> status = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn_detail);
        //初始化
        init();
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
        // 进度条
        circleProgressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        circleProgressBar.setVisibility(View.VISIBLE);
        // 列表
        listView = (ListView) findViewById(R.id.warn_detail_listView);
        View footer = getLayoutInflater().inflate(R.layout.list_view_footer, null);
        listView.addFooterView(footer);
        addMore = (Button) findViewById(R.id.list_more);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sub_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView subToolbarTitle = (TextView) findViewById(R.id.sub_toolbar_title);
        subToolbarTitle.setText(title);
    }

    private void initListView(Integer page) {
        ApiRequest apiRequest = new ApiRequest(this);
        // 告警是否已经解除(0:全部，1:已经确认, 2:未结束。默认为2)
        Call<Alarm> call = apiRequest.getService().getWarnMessages(device_id, 2, page);
        // 发送请求
        call.enqueue(new Callback<Alarm>() {
            @Override
            public void onResponse(Response<Alarm> response) {
                // 初始化变量
                Alarm alarm = response.body();
                int code = response.code();

                final Integer total_pages = alarm.getTotalPages();
                final Integer current_page = alarm.getCurrentPage();
                List<String> data = new ArrayList<>();

                if (code == 200) {
                    // 获取数据
                    List<PointAlarm> pointAlarms = alarm.getPointAlarms();
                    for (PointAlarm pointAlarm : pointAlarms) {
                        comments.add(pointAlarm.getComment());
                        times.add(pointAlarm.getUpdatedAt());
                        alarms.add(pointAlarm.getAlarmValue());
                        status.add(pointAlarm.getMeaning());
                        if (pointAlarm.getType() == null) {
                            types.add("告警");
                        } else {
                            types.add(pointAlarm.getType());
                        }
                    }

                    circleProgressBar.setVisibility(View.GONE);

                    // ListView填装数据
                    BaseAdapter adapter = new WarnDetailListAdapter(context, comments, types, times, alarms, status);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);

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
}
