package com.buoyantec.eagle_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buoyantec.eagle_android.adapter.MainGridAdapter;
import com.buoyantec.eagle_android.adapter.MySliderView;
import com.buoyantec.eagle_android.model.Result;
import com.buoyantec.eagle_android.model.Results;
import com.buoyantec.eagle_android.myService.ApiRequest;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences mPreferences;
    private String[] rooms;
    private HashMap<String, Integer> systemAlarmCount;
    private CircleProgressBar circleProgressBar;
    private static Boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 用户信息是否存在
         * 是: 加载主页面
         * 否: 加载登陆页
         */
        mPreferences = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        String token = mPreferences.getString("token", "");
        Integer current_room_id = mPreferences.getInt("current_room_id", 0);
        String rooms = mPreferences.getString("rooms", null);
        String current_room = mPreferences.getString("current_room", null);

        if (token.isEmpty() || current_room_id==0 || rooms==null || current_room==null) {
            finish();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        } else {
            // 加载字体图标
            Iconify.with(new FontAwesomeModule());
            // 加载布局文件
            setContentView(R.layout.activity_main);
            // 初始化toolbar和侧边栏
            initToolBarAndDrawer();
            // 图片轮播
            initCarousel();
            // 初始化GridView
            initGridView();
            // 异步任务: 检测告警信息
            getSubSystemAlarmCount();
        }
    }

    // 初始化toolbar和侧边栏
    public void initToolBarAndDrawer(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        TextView subToolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        // 获取所有机房([id1, room1, id2, room2, ...])
        String sp_rooms = mPreferences.getString("rooms", null);
        if (sp_rooms != null) {
            rooms = sp_rooms.split("#");
            String current_room = mPreferences.getString("current_room", null);
            subToolbarTitle.setText(current_room);
        } else {
            subToolbarTitle.setText("无可管理机房");
        }


        //添加侧边菜单,并绑定ToolBar菜单按钮
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 更新数据
        View headLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView name = (TextView) headLayout.findViewById(R.id.user_name);
        String mName = mPreferences.getString("name", null);
        name.setText(mName);

        // 退出登录功能
        Button signOutButton = (Button) findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString("token", "");
                editor.putString("current_room", null);
                editor.putInt("current_room_id", 0);
                editor.putString("rooms", null);
                editor.apply();
                finish();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    //为后退键绑定关闭侧边菜单功能
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    // 物理菜单键绑定功能
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_MENU) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByDoubleClick();      //调用双击退出函数
        }
        return super.onKeyDown(keyCode, event);
    }

    // 两次点击返回键退出程序
    private void exitByDoubleClick() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    //侧边菜单响应函数
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user_number) {
            // Handle the camera action
        } else if (id == R.id.nav_message) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //初始化轮播控件
    private void initCarousel() {
        SliderLayout sliderShow = (SliderLayout) findViewById(R.id.slider);
        sliderShow.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));

        for (int i = 0; i<rooms.length; i+=2){
            MySliderView mySliderView = new MySliderView(this);

            final int finalI = i;
            mySliderView
                    .description(rooms[i+1])
                    .image(R.drawable.image_room)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            SharedPreferences.Editor editor = mPreferences.edit();
                            editor.putInt("current_room_id", Integer.parseInt(rooms[finalI]));
                            editor.putString("current_room", rooms[finalI + 1]);
                            editor.apply();
                            finish();
                            Intent i = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                    });
            sliderShow.addSlider(mySliderView);
        }
        sliderShow.setDuration(8000);
    }

    //---------------------私有方法------------------------
    //初始化栅格布局
    private void initGridView(){
        final Context context = this;
        // references to our images
        Integer[] images = {
                R.drawable.icon_system_status, R.drawable.icon_info,
                R.drawable.icon_work_order, R.drawable.icon_power_manager,
                R.drawable.icon_phone, R.drawable.icon_other
        };
        // texts of images
        final String[] texts = { "系统状态", "告警信息", "工作安排", "能效管理", "IT管理", "其他" };

        GridView gridview = (GridView) findViewById(R.id.grid_view);
        gridview.setAdapter(new MainGridAdapter(gridview, this, images, texts));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) {
                    Intent i = new Intent(MainActivity.this, SystemStatus.class);
                    i.putExtra("title", texts[position]);
                    startActivity(i);
                } else if (position == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("systemAlarmCount", systemAlarmCount);
                    Intent i = new Intent(MainActivity.this, WarnSystems.class);
                    i.putExtra("title", texts[position]);
                    i.putExtras(bundle);
                    startActivity(i);
                } else if (position == 2) {
//                    Intent i = new Intent(MainActivity.this, WorkPlan.class);
//                    i.putExtra("title", texts[position]);
//                    startActivity(i);
                    Toast.makeText(context, "暂未开通", Toast.LENGTH_SHORT).show();
                } else if (position == 3) {
//                    Intent i = new Intent(MainActivity.this, PowerManage.class);
//                    i.putExtra("title", texts[position]);
//                    startActivity(i);
                    Toast.makeText(context, "暂未开通", Toast.LENGTH_SHORT).show();

                } else if (position == 4) {
//                    Intent i = new Intent(MainActivity.this, ItManage.class);
//                    i.putExtra("title", texts[position]);
//                    startActivity(i);
                    Toast.makeText(context, "暂未开通", Toast.LENGTH_SHORT).show();
                } else if (position == 5) {
//                    Intent i = new Intent(MainActivity.this, Other.class);
//                    i.putExtra("title", texts[position]);
//                    startActivity(i);
                    Toast.makeText(context, "暂未开通", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getSubSystemAlarmCount() {
        // 获取机房id
        final SharedPreferences sp = getSharedPreferences("foobar", MODE_PRIVATE);
        Integer room_id = sp.getInt("current_room_id", 1);
        final Context context = this;

        // 请求服务
        final ApiRequest apiRequest = new ApiRequest(this);
        Call<Results> call = apiRequest.getService().getSystemAlarmCount(room_id);
        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Response<Results> response) {
                circleProgressBar = (CircleProgressBar) findViewById(R.id.grid_warn_message_progress);

                int code = response.code();
                if (code == 200) {
                    // 计数
                    Integer count = 0;
                    systemAlarmCount = new HashMap<>();

                    List<Result> results = response.body().getResults();
                    for (Result result : results) {
                        count += result.getSize();
                        systemAlarmCount.put(result.getName(), result.getSize());
                    }

                    ImageView warnMessage = (ImageView) findViewById(R.id.grid_warn_message_image);
                    BadgeView badge = new BadgeView(MainActivity.this, warnMessage);
                    badge.setBadgeMargin(0);
                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);
                    if (count == 0) {
                        badge.hide();
                    } else {
                        if (count>=1000) {
                            badge.setText("···");
                        } else{
                            badge.setText(count.toString());
                        }
                        badge.show();
                    }
                    Log.i("获取子系统告警数", context.getString(R.string.getSuccess) + code);
                } else {
                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);
                    Log.i("获取子系统告警数", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                circleProgressBar = (CircleProgressBar) findViewById(R.id.grid_warn_message_progress);
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                Log.i("获取子系统告警数", context.getString(R.string.linkFailed));
                // TODO: 16/2/19 错误处理
            }
        });
    }
}
