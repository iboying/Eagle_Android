package com.buoyantec.eagle_android.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class FireFightingDetail extends BaseActivity {
    private CircleProgressBar circleProgressBar;
    private Toolbar toolbar;
    private SimpleDraweeView myImage;
    private Context context;
    private SharedPreferences sp;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fire_fighting_detail);
        toolbar = getViewById(R.id.sub_toolbar);
        myImage = getViewById(R.id.fire_fighting_detail_image);
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
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        subToolbarTitle.setText(getIntent().getStringExtra("title"));
    }

    private void initListView() {
        // 进度条
        circleProgressBar = getViewById(R.id.progressBar);
        circleProgressBar.setVisibility(View.VISIBLE);

        // 获取device_id 和 room_id
        Integer room_id = sp.getInt("current_room_id", 1);
        Integer device_id = getIntent().getIntExtra("device_id", 1);

        // 以下两句不是必须的,只是以防万一的bug,我菜,你咬我
        SharedPreferences mPreferences = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        setEngine(mPreferences);
        mEngine.getDeviceDataHashV2(room_id, device_id).enqueue(new Callback<DeviceDetail>() {
            @Override
            public void onResponse(Response<DeviceDetail> response) {
                int code = response.code();
                if (code == 200) {
                    ArrayList<String> names = new ArrayList<>();
                    ArrayList<String> status = new ArrayList<>();
                    // 获取图片
                    String path = response.body().getPic();
                    if (path != null) {
                        Uri uri = Uri.parse(path);
                        myImage.setImageURI(uri);
                    }

                    // 循环list,存入数组
                    // 循环list,存入数组
                    List<HashMap<String, String>> points =  response.body().getAlarms();
                    for (HashMap<String, String> point: points) {
                        names.add(point.get("name"));
                        status.add(point.get("value"));
                    }

                    // 调用helper,生成ListView
                    ListView listView = getViewById(R.id.fire_fighting_detail_listView);
                    // 加载列表
                    listView.setAdapter(new WaterListAdapter(listView, context, names, status));

                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);
                    Log.i("消防系统->详情", context.getString(R.string.getSuccess) + code);
                } else {
                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);
                    showToast(context.getString(R.string.getDataFailed));
                    Log.i("消防系统->详情", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                showToast(context.getString(R.string.netWorkFailed));
                Log.i("消防系统->详情", context.getString(R.string.linkFailed));
            }
        });
    }
}
