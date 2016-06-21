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

import com.buoyantec.eagle_android.adapter.WaterListAdapter;
import com.buoyantec.eagle_android.model.DeviceDetail;
import com.buoyantec.eagle_android.ui.base.BaseActivity;
import com.buoyantec.eagle_android.ui.base.BaseTimerActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaterDetail extends BaseTimerActivity {
    private CircleProgressBar circleProgressBar;
    private Toolbar toolbar;
    private TextView subToolbarTitle;
    private ListView listView;
    private SimpleDraweeView waterImage;
    private Context context;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_water_detail);
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        waterImage = getViewById(R.id.water_detail_image);
        listView = getViewById(R.id.water_detail_listView);
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
        // 进度条
        circleProgressBar = getViewById(R.id.progressBar);
        circleProgressBar.setVisibility(View.VISIBLE);

        // 获取device_id 和 room_id
        Integer room_id = sp.getInt("current_room_id", 1);
        Integer device_id = getIntent().getIntExtra("device_id", 1);

        setEngine(sp);

        mEngine.getDeviceDataHashV2(room_id, device_id).enqueue(new Callback<DeviceDetail>() {
            @Override
            public void onResponse(Call<DeviceDetail> call, Response<DeviceDetail> response) {
                int code = response.code();

                if (code == 200) {
                    ArrayList<String> names = new ArrayList<>();
                    ArrayList<String> status = new ArrayList<>();
                    // 获取图片
                    String path = response.body().getPic();
                    if (path != null) {
                        Uri uri = Uri.parse(path);
                        waterImage.setImageURI(uri);
                    }

                    // 循环list,存入数组
                    List<HashMap<String, String>> points =  response.body().getAlarms();
                    for (HashMap<String, String> point: points) {
                        if (point.get("name") == null) {
                            names.add("-");
                            status.add("-");
                        } else {
                            names.add(point.get("name"));
                            status.add(point.get("value"));
                        }
                    }

                    // 加载列表
                    ListView listView = (ListView) findViewById(R.id.water_detail_listView);
                    assert listView != null;
                    listView.setAdapter(new WaterListAdapter(listView, context, names, status));

                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);

                    Log.i("漏水系统->详情", context.getString(R.string.getSuccess) + code);
                } else {
                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);
                    showToast(context.getString(R.string.getDataFailed));
                    Log.i("漏水系统->详情", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Call<DeviceDetail> call, Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                showToast(context.getString(R.string.netWorkFailed));
                Log.i("漏水系统->详情", context.getString(R.string.linkFailed));
            }
        });
    }

}
