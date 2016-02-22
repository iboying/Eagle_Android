package com.buoyantec.eagle_android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.API.MyService;
import com.buoyantec.eagle_android.adapter.CabinetListAdapter;
import com.buoyantec.eagle_android.adapter.PowerUpsGridViewAdapter;
import com.buoyantec.eagle_android.model.Devices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PowerDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_detail);
        initToolbar();
        initListView();
        //初始化GridView
//        initPowerGridView();
//        initVoltageGridView();
//        initElectricityGridView();
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
        String title = i.getStringExtra("title");
        subToolbarTitle.setText(title);
    }

    private void initListView() {
        // 获取device_id 和 room_id
        final SharedPreferences sp = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        Intent i = getIntent();
        Integer device_id = i.getIntExtra("device_id", 1);
        Integer room_id = sp.getInt("current_room_id", 1);
        final Context context = this;

        // 调用接口,获取设备状态
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("X-User-Token", sp.getString("token", ""))
                        .addHeader("X-User-Phone", sp.getString("phone", ""))
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        // 创建Retrofit实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://139.196.190.201/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        // 创建所有链接
        MyService myService = retrofit.create(MyService.class);

        // 获取指定链接数据
        Call<ResponseBody> call = myService.getDeviceData(room_id, device_id);
        call.enqueue(new Callback<ResponseBody>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Response<ResponseBody> response) {
                if (response.code() == 200) {
                    // 数据变量
                    ArrayList<String> nameArray = new ArrayList<>();
                    ArrayList<String> statusArray = new ArrayList<>();
                    try {
                        String jsonString = response.body().string();
                        ApplicationHelper helper = new ApplicationHelper();
                        HashMap<String, String> datas = helper.jsonToHash(jsonString);

                        // 循环hash,存入数组
                        for (Map.Entry<String, String> entry : datas.entrySet()) {
                            nameArray.add(entry.getKey());
                            statusArray.add(entry.getValue());
                        }

                        // item数据
                        String[] names = nameArray.toArray(new String[nameArray.size()]);
                        String[] status = statusArray.toArray(new String[statusArray.size()]);

                        // 加载列表
                        ListView listView = (ListView) findViewById(R.id.power_detail_listView);
                        listView.setAdapter(new CabinetListAdapter(listView, context, names, status));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("========谁被数据获取失败========");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("设备数据链接失败");
                // TODO: 16/2/22 错误处理
            }
        });
    }


    /**
     * 设计图页面
     * 数据不匹配,暂时不用
     */

//    private void initPowerGridView() {
//        int item_layout = R.layout.grid_item_green;
//        String[] texts = { "有功功率", "无功功率", "视在功率", "频率" };
//        Integer[] data = {556,556,556,556};
//
//        GridView gridview = (GridView) findViewById(R.id.power_detail_power_gridView);
//        gridview.setAdapter(new PowerUpsGridViewAdapter(gridview, this, item_layout, texts, data));
//    }
//
//    private void initVoltageGridView() {
//        int item_layout = R.layout.grid_item_orange;
//        String[] texts = { "A相电压", "B相电压", "C相电压", "AB相电压", "BC相电压", "CA相电压" };
//        Integer[] data = {556,556,556,556,556,556};
//
//        GridView gridview = (GridView) findViewById(R.id.power_detail_voltage_gridView);
//        gridview.setAdapter(new PowerUpsGridViewAdapter(gridview, this, item_layout, texts, data));
//    }
//
//    private void initElectricityGridView() {
//        int item_layout = R.layout.grid_item_purple;
//        String[] texts = { "A相电流", "B相电流", "C相电流" };
//        Integer[] data = {556,556,556};
//
//        GridView gridview = (GridView) findViewById(R.id.power_detail_electricity_gridView);
//        gridview.setAdapter(new PowerUpsGridViewAdapter(gridview, this, item_layout, texts, data));
//    }
}
