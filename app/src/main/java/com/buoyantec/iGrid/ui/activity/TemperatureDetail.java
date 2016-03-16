package com.buoyantec.iGrid.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.iGrid.adapter.TemperatureListAdapter;
import com.buoyantec.iGrid.model.DeviceDetail;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class TemperatureDetail extends BaseActivity {
    private CircleProgressBar circleProgressBar;
    private SharedPreferences sp;
    private Toolbar toolbar;
    private TextView subToolbarTitle;
    private ListView listView;
    private Context context;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_temperature_detail);
        sp = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        listView = getViewById(R.id.temperature_detail_listView);
        circleProgressBar = getViewById(R.id.progressBar);
        context = this;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        // 初始化toolbar
        initToolbar();
        // 初始化list
        initListView();

        String current_room = sp.getString("current_room", null);
        assert current_room != null;
        if (current_room.equals("青海银监局")) {
            ImageView imageView = getViewById(R.id.temperature_detail_image);
            imageView.setImageResource(R.drawable.temperature_detail);
        }
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
        // 进度条
        circleProgressBar.setVisibility(View.VISIBLE);
        // 获取device_id 和 room_id
        Integer room_id = sp.getInt("current_room_id", 1);
        Integer device_id = getIntent().getIntExtra("device_id", 1);

        mEngine.getDeviceDataHash(room_id, device_id).enqueue(new Callback<DeviceDetail>() {
            @Override
            public void onResponse(Response<DeviceDetail> response) {
                int code = response.code();
                if (code == 200) {
                    ArrayList<String> tem = new ArrayList<>();
                    ArrayList<String> hum = new ArrayList<>();

                    // 循环list,存入数组
                    List<HashMap<String, String>> points = response.body().getPoints();
                    int i = 1;
                    for (HashMap<String, String> point : points) {
                        if (i % 2 == 1) {
                            tem.add(point.get("value"));
                        } else {
                            hum.add(point.get("value"));
                        }
                        i++;
                    }
                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);

                    // 加载列表
                    listView.setAdapter(new TemperatureListAdapter(listView, context, tem, hum));

                    Log.i("温湿度系统->详情", context.getString(R.string.getSuccess) + code);
                } else {
                    showToast(context.getString(R.string.getDataFailed));
                    Log.i("温湿度系统->详情", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                showToast(context.getString(R.string.netWorkFailed));
                Log.i("温湿度系统->详情", context.getString(R.string.linkFailed));
            }
        });
    }
}
