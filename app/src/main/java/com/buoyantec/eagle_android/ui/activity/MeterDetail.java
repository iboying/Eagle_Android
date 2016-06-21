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
import com.buoyantec.eagle_android.ui.base.BaseActivity;
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

/**
 * 当前: 系统状态 -> 电量仪系统 -> 详情
 */
public class MeterDetail extends BaseTimerActivity {
    private CircleProgressBar circleProgressBar;
    private Toolbar toolbar;
    private TextView subToolbarTitle;
    private ListView listView;

    private Context context;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_box_detail);

        Iconify.with(new FontAwesomeModule());
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        circleProgressBar = getViewById(R.id.progressBar);
        listView = getViewById(R.id.meter_detail_listView);
        context = this;

        //初始化toolbar
        initToolbar();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
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

        String title = getIntent().getStringExtra("title");
        subToolbarTitle.setText(title);
    }

    private void initListView() {
        circleProgressBar.setVisibility(View.VISIBLE);
        // 获取device_id 和 room_id
        Integer room_id = sp.getInt("current_room_id", 1);
        Integer device_id = getIntent().getIntExtra("device_id", 1);

        setEngine(sp);
        // 获取指定链接数据
        mEngine.getDeviceDataHashV2(room_id, device_id).enqueue(new Callback<DeviceDetail>() {
            @Override
            public void onResponse(Call<DeviceDetail> call, Response<DeviceDetail> response) {
                int code = response.code();
                if (code == 200) {
                    // 循环list,存入数组
                    List<HashMap<String, String>> numbers = response.body().getNumberType();
                    List<HashMap<String, String>> status = response.body().getStatusType();
                    List<HashMap<String, String>> alarms = response.body().getAlarmType();

                    // 调用helper,生成ListView
                    DeviceDetailList deviceDetailList = new DeviceDetailList(context, listView, numbers, status, alarms);
                    deviceDetailList.setListView();

                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);
                    Log.i("电量仪系统->详情", context.getString(R.string.getSuccess) + code);
                } else {
                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);
                    showToast(context.getString(R.string.getDataFailed));
                    Log.i("电量仪系统->详情", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Call<DeviceDetail> call, Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                showToast(context.getString(R.string.netWorkFailed));
                Log.i("电量仪系统->详情", context.getString(R.string.linkFailed));
            }
        });
    }
}
