package com.buoyantec.eagle_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buoyantec.eagle_android.API.MyService;
import com.buoyantec.eagle_android.adapter.SystemStatusGridAdapter;
import com.buoyantec.eagle_android.model.MySystem;
import com.buoyantec.eagle_android.model.MySystems;
import com.buoyantec.eagle_android.model.SubSystem;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SystemStatus extends AppCompatActivity {
    private HashMap<String, Integer> systemIcon;
    private HashMap<String, Class> systemClass;
    private HashMap<String, String[]> kindSystems;
    private Integer statusCode;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_status);
        // 初始化变量
        init();
        // sub_toolbar
        initToolbar();
        // 获取接口,刷新UI
        initSystems();
    }

    //-------------------私有方法--------------------
    private void init(){
        systemIcon = new HashMap<>();
        systemClass = new HashMap<>();
        kindSystems = new LinkedHashMap<>();
        context = this;
        // 动力
        systemIcon.put("UPS系统", R.drawable.system_status_ups);
        systemIcon.put("电量仪系统", R.drawable.system_status_box);
        systemIcon.put("配电系统", R.drawable.system_status_power);
        systemIcon.put("电池检测", R.drawable.system_status_battery);
        systemIcon.put("发电机系统", R.drawable.system_status_engine);
        // 环境
        systemIcon.put("温湿度系统", R.drawable.system_status_temperature);
        systemIcon.put("机柜环境", R.drawable.system_status_cabinet);
        systemIcon.put("空调系统", R.drawable.system_status_air);
        systemIcon.put("漏水系统", R.drawable.system_status_water);
        // 联动
        // 安全
        systemIcon.put("消防系统", R.drawable.system_status_video);
        systemIcon.put("氢气检测", R.drawable.system_status_smoke);
        // 远程
        // 能效
        // 部署

        // 动力
        systemClass.put("UPS系统", UpsSystem.class);
        systemClass.put("电量仪系统", Meter.class);
        systemClass.put("配电系统", PowerDistribution.class);
        systemClass.put("电池检测", Battery.class);
        systemClass.put("发电机系统", Meter.class);//// TODO: 16/2/2  无页面
        // 环境
        systemClass.put("温湿度系统", Temperature.class);
        systemClass.put("机柜环境", Cabinet.class);
        systemClass.put("空调系统", PrecisionAir.class);
        systemClass.put("漏水系统", Water.class);
        // 联动
        // 安全
        systemClass.put("消防系统", VideoSystem.class);
        systemClass.put("氢气检测", Smoke.class);
        // 远程
        // 能效
        // 部署
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sub_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView subToolbarTitle = (TextView) findViewById(R.id.sub_toolbar_title);
        subToolbarTitle.setText("系统状态");
    }

    /**
     * 动态获取系统列表
     */
    private void initSystems() {
        // 获取数据
        final SharedPreferences sp = getSharedPreferences("foobar", MODE_PRIVATE);
        final String token = sp.getString("token", null);
        final String phone = sp.getString("phone", null);
        // 定义拦截器,添加headers
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("X-User-Token", token)
                        .addHeader("X-User-Phone", phone)
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

        // 建立http请求
        MyService myService = retrofit.create(MyService.class);
        Call<MySystems> call = myService.getSystems();
        // 发送请求
        call.enqueue(new Callback<MySystems>() {
            @Override
            public void onResponse(Response<MySystems> response) {
                statusCode = response.code();
                if (response.body() != null && statusCode == 200) {
                    // 定义动态数组,用于保存子系统
                    ArrayList<String> subSystemList = new ArrayList<>();

                    //  获取所有的分类系统(比如: 动力,环境..)
                    List<MySystem> mySystems = response.body().getMySystems();
                    for (MySystem mySystem : mySystems) {
                        String systemName = mySystem.getName();
                        System.out.println(systemName + "---------------------------");
                        subSystemList.clear();
                        // 获取所有的子系统( 比如: ups, 配电..)
                        for (SubSystem subSystem : mySystem.getSubSystem()) {
                            String subName = subSystem.getSubSystemName();
                            System.out.println("-->" + subName);
                            subSystemList.add(subName);
                        }
                        // 把分类名和对应的子系统列表写入HashMap
                        if (subSystemList.size() > 0) {
                            kindSystems.put(systemName,
                                    subSystemList.toArray(new String[subSystemList.size()]));
                        }
                    }

                    // 按照kindSystems加载UI
                    LinearLayout container = (LinearLayout)
                            findViewById(R.id.system_status_linearLayout);
                    for (HashMap.Entry<String, String[]> entry : kindSystems.entrySet()) {
                        // 加载分类标题
                        View titleLayout = View.inflate(context, R.layout.system_status_text_view, null);
                        container.addView(titleLayout);
                        TextView title = (TextView) titleLayout.findViewById(R.id.system_status_title);
                        title.setText(entry.getKey());

                        // 加载GridView
                        String[] systems = entry.getValue();
                        System.out.println("读取>>>>>>>>>>>>>>>>>" + entry.getKey());
                        final ArrayList<String> texts = new ArrayList<>();
                        final ArrayList<Integer> images = new ArrayList<>();
                        for (int i = 0; i < systems.length; i++) {
                            texts.add(systems[i]);
                            images.add(systemIcon.get(systems[i]));
                        }
                        final String[] grid_texts = texts.toArray(new String[texts.size()]);
                        Integer[] grid_images = images.toArray(new Integer[images.size()]);

                        // 动态加载gridView
                        View gridLayout = View.inflate(context, R.layout.system_status_grid_view, null);
                        container.addView(gridLayout);
                        GridView gridView = (GridView) gridLayout.findViewById(R.id.system_status_grid);
                        gridView.setAdapter(new SystemStatusGridAdapter(gridView, context, grid_images, grid_texts));
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                for (int j = 0; j < grid_texts.length; j++) {
                                    if (position == j) {
                                        Intent i = new Intent(context, systemClass.get(grid_texts[j]));
                                        // 获取子系统名称
                                        TextView tv = (TextView) v.findViewById(R.id.sub_grid_view_text);
                                        String sub_sys_name = (String) tv.getText();
                                        i.putExtra("sub_sys_name", sub_sys_name);
                                        startActivity(i);
                                    }
                                }
                            }
                        });
                    }

                } else {
                    try {
                        String error = response.errorBody().string();
                        System.out.println(error + "8888888888888888888888888888888");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("system_status_status_code", statusCode);
                    editor.apply();
                    System.out.println(">>>>>>>>>>获取系统状态列表失败>>>>>>>>>>>>");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println(">>>>>>>>>>系统状态接口链接失败>>>>>>>>>>>>");
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("system_status_status_code", 2222);
                editor.apply();
            }
        });
    }
}
