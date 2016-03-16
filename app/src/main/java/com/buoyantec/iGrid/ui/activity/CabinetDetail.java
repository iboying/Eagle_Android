package com.buoyantec.iGrid.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.iGrid.adapter.DeviceDetailListAdapter;
import com.buoyantec.iGrid.model.DeviceDetail;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class CabinetDetail extends BaseActivity {
    private CircleProgressBar circleProgressBar;
    private SharedPreferences sharedPreferences;
    private Toolbar toolbar;
    private TextView subToolbarTitle;
    private Context context;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cabinet_detail);

        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        circleProgressBar = getViewById(R.id.progressBar);
        circleProgressBar.setVisibility(View.VISIBLE);
        sharedPreferences = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        context = this;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initToolbar();
        initListView();
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        subToolbarTitle.setText(getIntent().getStringExtra("title"));
    }

    private void initListView() {
        // 获取device_id 和 room_id
        Integer room_id = sharedPreferences.getInt("current_room_id", 1);
        Integer device_id = getIntent().getIntExtra("device_id", 1);

        // 获取指定链接数据
        mEngine.getDeviceDataHash(room_id, device_id).enqueue(new Callback<DeviceDetail>() {
            @Override
            public void onResponse(Response<DeviceDetail> response) {
                int code = response.code();

                if (code == 200) {
                    ArrayList<String> names = new ArrayList<>();
                    ArrayList<String> values = new ArrayList<>();
                    // 循环list,存入数组
                    List<HashMap<String, String>> points = response.body().getPoints();
                    for (HashMap<String, String> point : points) {
                        names.add(point.get("name"));
                        values.add(point.get("value"));
                    }
                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);
                    // 加载列表
                    ListView listView = getViewById(R.id.cabinet_detail_listView);
                    listView.setAdapter(new DeviceDetailListAdapter(listView, context, names, values));

                    Log.i("机柜环境->详情", context.getString(R.string.getSuccess) + code);
                } else {
                    showToast(context.getString(R.string.getDataFailed));
                    Log.i("机柜环境->详情", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                showToast(context.getString(R.string.netWorkFailed));
                Log.i("机柜环境->详情", context.getString(R.string.linkFailed));
            }
        });
    }
}
