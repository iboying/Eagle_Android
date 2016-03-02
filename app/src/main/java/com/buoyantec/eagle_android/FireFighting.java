package com.buoyantec.eagle_android;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.buoyantec.eagle_android.adapter.StandardListAdapter;
import com.buoyantec.eagle_android.model.Device;
import com.buoyantec.eagle_android.model.Devices;
import com.buoyantec.eagle_android.myService.ApiRequest;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FireFighting extends AppCompatActivity {
    private SharedPreferences sp;
    private Integer room_id;
    private String sub_sys_name;
    private Context context;
    private CircleProgressBar circleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_fighting);
        // 初始化变量
        init();
        // 加载字体图标
        Iconify.with(new FontAwesomeModule());
        // 初始化toolbar
        initToolbar();
        // 加载list
        initListView();
    }

    private void init() {
        sp = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        // TODO: 16/2/7 默认值的问题
        room_id = sp.getInt("current_room_id", 1);

        Intent i = getIntent();
        sub_sys_name = i.getStringExtra("sub_sys_name");

        context = getApplicationContext();
        // 进度条
        circleProgressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        circleProgressBar.setVisibility(View.VISIBLE);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sub_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView subToolbarTitle = (TextView) findViewById(R.id.sub_toolbar_title);
        subToolbarTitle.setText(sub_sys_name);
    }

    private void initListView() {
        ApiRequest apiRequest = new ApiRequest(this);
        // 获取指定链接数据
        Call<Devices> call = apiRequest.getService().getDevices(room_id, sub_sys_name);
        call.enqueue(new Callback<Devices>() {
            @Override
            public void onResponse(Response<Devices> response) {
                int code = response.code();
                if (code == 200) {
                    List<String> names = new ArrayList<>();
                    final ArrayList<Integer> ids = new ArrayList<>();
                    List<Integer> images = new ArrayList<>();

                    // 获取用户
                    List<Device> devices = response.body().getDevices();
                    for (Device device : devices) {
                        names.add(device.getName());
                        ids.add(device.getId());
                        if (device.getName().equals("烟感")) {
                            images.add(R.drawable.system_status_smoke);
                        } else {
                            images.add(R.drawable.system_status_door);
                        }
                    }

                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);

                    // 加载列表
                    ListView listView = (ListView) findViewById(R.id.fire_fighting_listView);
                    listView.setAdapter(new StandardListAdapter(listView, context, images, names));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            TextView title = (TextView) v.findViewById(R.id.list_item_standard_list_text);
                            Intent i = new Intent(FireFighting.this, FireFightingDetail.class);
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
                Log.i(sub_sys_name, context.getString(R.string.linkFailed));
                Toast.makeText(context, context.getString(R.string.netWorkFailed), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
