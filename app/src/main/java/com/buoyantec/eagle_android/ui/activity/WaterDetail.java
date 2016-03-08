package com.buoyantec.eagle_android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.buoyantec.eagle_android.adapter.WaterListAdapter;
import com.buoyantec.eagle_android.model.DeviceDetail;
import com.buoyantec.eagle_android.myService.ApiRequest;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaterDetail extends AppCompatActivity {
    private CircleProgressBar circleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_detail);
        // 初始化toolbar
        initToolbar();
        // 初始化list
        initListView();

        SharedPreferences sp = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        String current_room = sp.getString("current_room", null);
        assert current_room != null;
        if (current_room.equals("青海银监局")) {
            ImageView imageView = (ImageView) findViewById(R.id.water_detail_image);
            imageView.setImageResource(R.drawable.water_detail);
        }
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
        subToolbarTitle.setText(i.getStringExtra("title"));
    }

    private void initListView() {
        // 进度条
        circleProgressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        circleProgressBar.setVisibility(View.VISIBLE);

        // 获取device_id 和 room_id
        final SharedPreferences sp = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        Integer room_id = sp.getInt("current_room_id", 1);
        Intent i = getIntent();
        Integer device_id = i.getIntExtra("device_id", 1);
        final Context context = this;

        ApiRequest apiRequest = new ApiRequest(this);
        Call<DeviceDetail> call = apiRequest.getService().getDeviceDataHash(room_id, device_id);
        call.enqueue(new Callback<DeviceDetail>() {
            @Override
            public void onResponse(Response<DeviceDetail> response) {
                int code = response.code();

                if (code == 200) {
                    ArrayList<String> names = new ArrayList<>();
                    ArrayList<String> status = new ArrayList<>();

                    // 循环list,存入数组
                    List<HashMap<String, String>> points =  response.body().getAlarms();
                    for (HashMap<String, String> point: points) {
                        names.add(point.get("name"));
                        status.add(point.get("value"));
                    }

                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);

                    // 加载列表
                    ListView listView = (ListView) findViewById(R.id.water_detail_listView);
                    listView.setAdapter(new WaterListAdapter(listView, context, names, status));

                    Log.i("漏水系统->详情", context.getString(R.string.getSuccess) + code);
                } else {
                    Toast.makeText(context, context.getString(R.string.getDataFailed), Toast.LENGTH_SHORT).show();
                    Log.i("漏水系统->详情", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                Toast.makeText(context, context.getString(R.string.netWorkFailed), Toast.LENGTH_SHORT).show();
                Log.i("漏水系统->详情", context.getString(R.string.linkFailed));
            }
        });
    }

}
