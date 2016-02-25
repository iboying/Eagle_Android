package com.buoyantec.eagle_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.API.MyService;
import com.buoyantec.eagle_android.adapter.WarnDetailListAdapter;
import com.buoyantec.eagle_android.model.Alarm;
import com.buoyantec.eagle_android.model.PointAlarm;
import com.buoyantec.eagle_android.myService.ApiRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WarnDetail extends AppCompatActivity {
    private String title;
    private Integer device_id;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn_detail);
        //初始化
        init();
        // 加载工具条
        initToolbar();
        //加载告警信息列表
        initListView();
    }

    private void init() {
        Intent i = getIntent();
        device_id = i.getIntExtra("device_id", 1);
        title = i.getStringExtra("title");
        context = this;
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sub_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView subToolbarTitle = (TextView) findViewById(R.id.sub_toolbar_title);
        subToolbarTitle.setText(title);
    }

    private void initListView() {
        ApiRequest apiRequest = new ApiRequest(this);
        // 告警是否已经解除(0:全部，1:已经确认, 2:未结束。默认为2)
        Call<Alarm> call = apiRequest.getService().getWarnMessages(device_id, 2);
        // 发送请求
        call.enqueue(new Callback<Alarm>() {
            @Override
            public void onResponse(Response<Alarm> response) {
                int code = response.code();
                if (code == 200) {
                    ArrayList<String> comment = new ArrayList<>();
                    ArrayList<Integer> state = new ArrayList<>();
                    // 获取数据
                    List<PointAlarm> pointAlarms = response.body().getPointAlarms();
                    Iterator<PointAlarm> itr = pointAlarms.iterator();
                    while (itr.hasNext()) {
                        PointAlarm pointAlarm = itr.next();
                        comment.add(pointAlarm.getComment());
                        state.add(pointAlarm.getState());
                    }

                    String[] texts = comment.toArray(new String[comment.size()]);
                    Integer[] data = state.toArray(new Integer[state.size()]);

                    ListView listView = (ListView) findViewById(R.id.warn_detail_listView);
                    listView.setAdapter(new WarnDetailListAdapter(listView, context, texts, data));

                    Log.i("设备告警->详情", context.getString(R.string.getSuccess) + code);
                } else {
                    // 输出非201时的错误信息
                    Log.i("设备告警->详情", context.getString(R.string.getFailed) + code);
                }
                System.out.println("设备告警列表接口调用完成");
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("设备告警->详情", context.getString(R.string.linkFailed));
                // TODO: 16/2/19 错误处理
            }
        });
    }
}
