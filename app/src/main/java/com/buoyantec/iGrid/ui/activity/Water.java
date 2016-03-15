package com.buoyantec.iGrid.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.buoyantec.iGrid.adapter.StandardListAdapter;
import com.buoyantec.iGrid.model.Device;
import com.buoyantec.iGrid.model.Devices;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class Water extends BaseActivity {
    private SharedPreferences sp;
    private Integer room_id;
    private String sub_sys_name;
    private Context context;
    private CircleProgressBar circleProgressBar;
    private Toolbar toolbar;
    private TextView subToolbarTitle;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_water);
        init();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        // 初始化toolbar
        initToolbar();
        // 加载list
        initListView();
    }

    private void init() {
        Iconify.with(new FontAwesomeModule());
        sp = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        // TODO: 16/2/7 默认值的问题
        room_id = sp.getInt("current_room_id", 1);

        Intent i = getIntent();
        sub_sys_name = i.getStringExtra("sub_sys_name");

        context = getApplicationContext();
        // 组件
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        circleProgressBar = getViewById(R.id.progressBar);
        circleProgressBar.setVisibility(View.VISIBLE);
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        subToolbarTitle.setText(sub_sys_name);
    }

    private void initListView() {
        // 获取指定链接数据
        mEngine.getDevices(room_id, sub_sys_name).enqueue(new Callback<Devices>() {
            @Override
            public void onResponse(Response<Devices> response) {
                int code = response.code();
                if (code == 200) {
                    List<String> names = new ArrayList<>();
                    final List<Integer> ids = new ArrayList<>();
                    // 获取用户
                    List<Device> devices = response.body().getDevices();
                    for (Device device : devices) {
                        names.add(device.getName());
                        ids.add(device.getId());
                    }
                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);

                    // 图标
                    Integer image = R.drawable.system_status_water;

                    ListView listView = getViewById(R.id.water_listView);
                    listView.setAdapter(new StandardListAdapter(listView, context, image, names));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            TextView title = (TextView) v.findViewById(R.id.list_item_standard_list_text);
                            Intent i = new Intent(Water.this, WaterDetail.class);
                            i.putExtra("title", title.getText());
                            i.putExtra("device_id", ids.get(position));
                            startActivity(i);
                        }
                    });
                    Log.i(sub_sys_name, context.getString(R.string.getSuccess) + code);
                } else {
                    // 输出非201时的错误信息
                    Toast.makeText(context, context.getString(R.string.getDataFailed), Toast.LENGTH_SHORT).show();
                    Log.i(sub_sys_name, context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                Toast.makeText(context, context.getString(R.string.netWorkFailed), Toast.LENGTH_SHORT).show();
                Log.i(sub_sys_name, context.getString(R.string.linkFailed));
            }
        });
    }
}
