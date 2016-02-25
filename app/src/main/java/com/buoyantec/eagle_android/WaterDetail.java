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
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.API.MyService;
import com.buoyantec.eagle_android.adapter.TemperatureListAdapter;
import com.buoyantec.eagle_android.adapter.WaterListAdapter;
import com.buoyantec.eagle_android.myService.ApiRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WaterDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_detail);
        // 初始化toolbar
        initToolbar();
        // 初始化list
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
        subToolbarTitle.setText(i.getStringExtra("title"));
    }

    private void initListView() {
        // 获取device_id 和 room_id
        final SharedPreferences sp = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        Integer room_id = sp.getInt("current_room_id", 1);
        Intent i = getIntent();
        Integer device_id = i.getIntExtra("device_id", 1);
        final Context context = this;

        ApiRequest apiRequest = new ApiRequest(this);
        Call<LinkedHashMap<String, String>> call = apiRequest
                .getService()
                .getDeviceDataHash(room_id, device_id);
        call.enqueue(new Callback<LinkedHashMap<String, String>>() {
            @Override
            public void onResponse(Response<LinkedHashMap<String, String>> response) {
                int code = response.code();
                if (code == 200) {
                    ArrayList<String> nameArray = new ArrayList<>();
                    ArrayList<Integer> statusArray = new ArrayList<>();
                    LinkedHashMap<String, String> map = response.body();;

                    map.remove("id");
                    map.remove("name");
                    // 循环hash,存入数组
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        nameArray.add(entry.getKey());
                        if (entry.getValue() == null) {
                            statusArray.add(1);
                        } else {
                            statusArray.add(0);
                        }
                    }

                    // item数据
                    String[] names = nameArray.toArray(new String[nameArray.size()]);
                    Integer[] status = statusArray.toArray(new Integer[statusArray.size()]);

                    // 加载列表
                    ListView listView = (ListView) findViewById(R.id.water_detail_listView);
                    listView.setAdapter(new WaterListAdapter(listView, context, names, status));

                    Log.i("漏水系统->详情", context.getString(R.string.getSuccess) + code);
                } else {
                    Log.i("漏水系统->详情", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("漏水系统->详情", context.getString(R.string.linkFailed));
                // TODO: 16/2/22 错误处理
            }
        });
    }

}
