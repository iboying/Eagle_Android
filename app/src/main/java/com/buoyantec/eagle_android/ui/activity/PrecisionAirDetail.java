package com.buoyantec.eagle_android.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.model.DeviceDetail;
import com.buoyantec.eagle_android.ui.base.BaseTimerActivity;
import com.buoyantec.eagle_android.ui.helper.DeviceDetailList;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrecisionAirDetail extends BaseTimerActivity {
    private CircleProgressBar circleProgressBar;
    private Toolbar toolbar;
    private TextView subToolbarTitle;
    private Context context;

    private Integer room_id;
    private Integer device_id;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_precision_air_detail);
        Iconify.with(new FontAwesomeModule());
        toolbar = (Toolbar) findViewById(R.id.sub_toolbar);
        subToolbarTitle = (TextView) findViewById(R.id.sub_toolbar_title);
        circleProgressBar = (CircleProgressBar) findViewById(R.id.progressBar);

        context = this;

        room_id = sp.getInt("current_room_id", 1);
        device_id = getIntent().getIntExtra("device_id", 1);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        //初始化toolbar
        initToolbar();
        //初始化list
        initListView();
    }

    @Override
    protected void beginTimerTask() {
        initListView();
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        subToolbarTitle.setText(getIntent().getStringExtra("title"));
    }

    private void initListView() {
        setEngine(sp);

        circleProgressBar.setVisibility(View.VISIBLE);
        // 获取指定链接数据
        mEngine.getDeviceDataHash(room_id, device_id).enqueue(new Callback<DeviceDetail>() {
            @Override
            public void onResponse(Call<DeviceDetail> call, Response<DeviceDetail> response) {
                setNetworkState(true);
                int code = response.code();
                if (code == 200) {
                    // 循环list,存入数组
                    List<HashMap<String, String>> numbers = response.body().getNumberType();
                    List<HashMap<String, String>> status = response.body().getStatusType();
                    List<HashMap<String, String>> alarms = response.body().getAlarmType();

                    // 调用helper,生成ListView
                    ListView listView = getViewById(R.id.precision_air_detail_listView);
                    DeviceDetailList deviceDetailList = new DeviceDetailList(context, listView, numbers, status, alarms);
                    deviceDetailList.setListView();

                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);
                    Log.i("空调系统->详情", context.getString(R.string.getSuccess) + code);
                } else {
                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);
                    showToast(context.getString(R.string.getDataFailed));
                    Log.i("空调系统->详情", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Call<DeviceDetail> call, Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                setNetworkState(false);
                Log.i("空调系统->详情", context.getString(R.string.linkFailed));
            }
        });
    }
}
