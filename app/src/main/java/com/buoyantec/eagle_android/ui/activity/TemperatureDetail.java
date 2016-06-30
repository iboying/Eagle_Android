package com.buoyantec.eagle_android.ui.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.adapter.TemperatureListAdapter;
import com.buoyantec.eagle_android.model.DeviceDetail;
import com.buoyantec.eagle_android.ui.base.BaseTimerActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TemperatureDetail extends BaseTimerActivity {
    private CircleProgressBar circleProgressBar;
    private Toolbar toolbar;
    private SimpleDraweeView myImage;
    private TextView subToolbarTitle;
    private ListView listView;
    private Context context;

    private Integer room_id;
    private Integer device_id;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_temperature_detail);
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        myImage = getViewById(R.id.temperature_detail_image);
        listView = getViewById(R.id.temperature_detail_listView);
        circleProgressBar = getViewById(R.id.progressBar);
        context = this;

        room_id = sp.getInt("current_room_id", 1);
        device_id = getIntent().getIntExtra("device_id", 1);
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
        mEngine.getDeviceDataHash(room_id, device_id).enqueue(new Callback<DeviceDetail>() {
            @Override
            public void onResponse(Call<DeviceDetail> call, Response<DeviceDetail> response) {
                setNetworkState(true);
                int code = response.code();
                if (code == 200) {
                    ArrayList<String> tem = new ArrayList<>();
                    ArrayList<String> hum = new ArrayList<>();
                    ArrayList<String> temColor = new ArrayList<>();
                    ArrayList<String> humColor = new ArrayList<>();

                    // 获取图片
                    String path = response.body().getPic();
                    if (path != null) {
                        Uri uri = Uri.parse(path);
                        myImage.setImageURI(uri);
                    }

                    // 循环list,存入数组
                    List<HashMap<String, String>> points = response.body().getPoints();
                    for (HashMap<String, String> point : points) {
                        if (point.get("name").contains("温度")) {
                            tem.add(point.get("value"));
                            temColor.add(point.get("color"));
                        } else if (point.get("name").contains("湿度")) {
                            hum.add(point.get("value"));
                            humColor.add(point.get("color"));
                        } else if (point.get("name") == null) {
                            hum.add("-");
                            humColor.add("-");
                        }
                    }
                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);

                    // 加载列表
                    listView.setAdapter(new TemperatureListAdapter(context, tem, temColor, hum, humColor));

                    Log.i("温湿度系统->详情", context.getString(R.string.getSuccess) + code);
                } else {
                    showToast(context.getString(R.string.getDataFailed));
                    Log.i("温湿度系统->详情", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Call<DeviceDetail> call, Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                setNetworkState(false);
                Log.i("温湿度系统->详情", context.getString(R.string.linkFailed));
            }
        });
    }
}
