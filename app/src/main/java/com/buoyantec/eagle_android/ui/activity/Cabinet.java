package com.buoyantec.eagle_android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.adapter.StandardListAdapter;
import com.buoyantec.eagle_android.model.Device;
import com.buoyantec.eagle_android.model.Devices;
import com.buoyantec.eagle_android.ui.base.BaseActivity;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cabinet extends BaseActivity {
    private Integer room_id;
    private String sub_sys_name;
    private Context context;
    private CircleProgressBar circleProgressBar;
    private Toolbar toolbar;
    private TextView subToolbarTitle;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cabinet);
        Iconify.with(new FontAwesomeModule());
        init();
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

    private void init() {
        room_id = sp.getInt("current_room_id", 1);
        sub_sys_name = getIntent().getStringExtra("sub_sys_name");
        if (sub_sys_name == null) {
            sub_sys_name = "";
        }
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
        // 以下两句不是必须的,只是以防万一的bug,我菜,你咬我
        setEngine(sp);
        mEngine.getDevices(room_id, sub_sys_name).enqueue(new Callback<Devices>() {

            @Override
            public void onResponse(Call<Devices> call, Response<Devices> response) {
                int code = response.code();
                if (code == 200) {
                    ArrayList<String> names = new ArrayList<>();
                    final ArrayList<Integer> ids = new ArrayList<>();
                    // 获取用户
                    List<Device> devices = response.body().getDevices();
                    for (Device device : devices) {
                        names.add(device.getName());
                        ids.add(device.getId());
                    }

                    // 图标
                    Integer image = R.drawable.system_status_cabinet;

                    // 加载列表
                    ListView listView = (ListView) getViewById(R.id.cabinet_listView);
                    listView.setAdapter(new StandardListAdapter(listView, context, image, names));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            TextView title = (TextView) v.findViewById(R.id.list_item_standard_list_text);
                            Intent i = new Intent(Cabinet.this, CabinetDetail.class);
                            i.putExtra("title", title.getText());
                            i.putExtra("device_id", ids.get(position));
                            startActivity(i);
                        }
                    });

                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);
                    Log.i(sub_sys_name, context.getString(R.string.getSuccess) + code);
                } else {
                    // 输出非201时的错误信息
                    circleProgressBar.setVisibility(View.GONE);
                    showToast(context.getString(R.string.getDataFailed));
                    Log.i(sub_sys_name, context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Call<Devices> call, Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                showToast(context.getString(R.string.netWorkFailed));
                Log.i(sub_sys_name, context.getString(R.string.linkFailed));
            }
        });
    }
}
