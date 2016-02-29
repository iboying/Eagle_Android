package com.buoyantec.eagle_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.adapter.DeviceDetailListAdapter;
import com.buoyantec.eagle_android.model.DeviceDetail;
import com.buoyantec.eagle_android.myService.ApiRequest;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PowerDetail extends AppCompatActivity {
    private CircleProgressBar circleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_detail);
        initToolbar();
        initListView();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sub_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView subToolbarTitle = (TextView) findViewById(R.id.sub_toolbar_title);
        Intent i = getIntent();
        String title = i.getStringExtra("title");
        subToolbarTitle.setText(title);
    }

    private void initListView() {
        // 进度条
        circleProgressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        circleProgressBar.setVisibility(View.VISIBLE);

        // 获取device_id 和 room_id
        final SharedPreferences sp = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        Intent i = getIntent();
        Integer device_id = i.getIntExtra("device_id", 1);
        Integer room_id = sp.getInt("current_room_id", 1);
        final Context context = this;

        // 获取指定链接数据
        ApiRequest apiRequest = new ApiRequest(this);
        Call<DeviceDetail> call = apiRequest.getService().getDeviceDataHash(room_id, device_id);
        call.enqueue(new Callback<DeviceDetail>() {
            @Override
            public void onResponse(Response<DeviceDetail> response) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                int code = response.code();
                if (code == 200) {
                    DeviceDetail map = response.body();

                    ArrayList<String> names = new ArrayList<>();
                    ArrayList<String> values = new ArrayList<>();

                    // 循环list,存入数组
                    List<HashMap<String, String>> points =  response.body().getPoints();
                    for (HashMap<String, String> point: points) {
                        names.add(point.get("name"));
                        values.add(point.get("value"));
                    }

                    // 加载列表
                    ListView listView = (ListView) findViewById(R.id.power_detail_listView);
                    listView.setAdapter(new DeviceDetailListAdapter(listView, context, names, values));
                    Log.i("配电系统->详情", context.getString(R.string.getSuccess) + code);
                } else {
                    Log.i("配电系统->详情", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                Log.i("配电系统->详情", context.getString(R.string.linkFailed));
                // TODO: 16/2/22 错误处理
            }
        });
    }
}
