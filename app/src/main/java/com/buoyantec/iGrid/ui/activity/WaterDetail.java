package com.buoyantec.iGrid.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
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

import com.buoyantec.iGrid.adapter.WaterListAdapter;
import com.buoyantec.iGrid.model.DeviceDetail;
import com.buoyantec.iGrid.myService.ApiRequest;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaterDetail extends BaseActivity {
    private CircleProgressBar circleProgressBar;
    private Toolbar toolbar;
    private TextView subToolbarTitle;
    private ListView listView;
    private ImageView imageView;
    private SharedPreferences sp;
    private Context context;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_water_detail);
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        imageView = getViewById(R.id.water_detail_image);
        listView = getViewById(R.id.water_detail_listView);
        sp = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
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
            imageView.setImageResource(R.drawable.water_detail);
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
        circleProgressBar = getViewById(R.id.progressBar);
        circleProgressBar.setVisibility(View.VISIBLE);

        // 获取device_id 和 room_id
        Integer room_id = sp.getInt("current_room_id", 1);
        Integer device_id = getIntent().getIntExtra("device_id", 1);

        mEngine.getDeviceDataHash(room_id, device_id).enqueue(new Callback<DeviceDetail>() {
            @Override
            public void onResponse(Response<DeviceDetail> response) {
                int code = response.code();

                if (code == 200) {
                    ArrayList<String> names = new ArrayList<>();
                    ArrayList<String> status = new ArrayList<>();

                    // 循环list,存入数组
                    List<HashMap<String, String>> points = response.body().getAlarms();
                    for (HashMap<String, String> point : points) {
                        names.add(point.get("name"));
                        status.add(point.get("value"));
                    }

                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);

                    // 加载列表
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
