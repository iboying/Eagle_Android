package com.buoyantec.eagle_android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import com.buoyantec.eagle_android.model.User;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView name;
    private String room;
    private Integer room_id;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断用户
        isPresentUser();
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
        // 异步任务
        new mainAsynTask().execute();
    }

    /**
     * 用户信息是否存在
     * 是: 加载主页面
     * 否: 加载登陆页
     */
    private void isPresentUser() {
        mPreferences = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        String pwd = mPreferences.getString("password", "");
        if (pwd.isEmpty()) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void initToolBarAndDrawer(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        TextView subToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        // 获取所有机房([id1, room1, id2, room2, ...])
        String sp_room = mPreferences.getString("room", null);
        if (sp_room != null){
            String[] rooms = sp_room.split("#");
            room_id = Integer.parseInt(rooms[0]);
            room = rooms[1];
            subToolbarTitle.setText(room);
        } else{
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
        this.name = (TextView)headLayout.findViewById(R.id.user_name);
        String mName = mPreferences.getString("name", null);
        name.setText(mName);

        // 退出登录功能
        Button signOutButton = (Button) findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString("password", "");
                editor.apply();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    //为后退键绑定关闭侧边菜单功能
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //为物理菜单键绑定折叠侧边菜单功能
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_MENU) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //侧边菜单响应函数
    @SuppressWarnings("StatementWithEmptyBody")
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

    //---------------------私有方法------------------------
    //初始化栅格布局
    private void initGridView(){
        // references to our images
        Integer[] images = {
                R.drawable.icon_system_status, R.drawable.icon_info,
                R.drawable.icon_work_order, R.drawable.icon_power_manager,
                R.drawable.icon_phone, R.drawable.icon_other
        };
        // texts of images
        String[] texts = { "系统状态", "告警信息", "工作安排", "能效管理", "IT管理", "其他" };

        GridView gridview = (GridView) findViewById(R.id.grid_view);
        gridview.setAdapter(new MainGridAdapter(gridview, this, images, texts));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) {
                    Intent i = new Intent(MainActivity.this, SystemStatus.class);
                    startActivity(i);
                } else if (position == 1) {
                    Intent i = new Intent(MainActivity.this, WarnMessages.class);
                    startActivity(i);
                } else if (position == 2) {
                    Intent i = new Intent(MainActivity.this, WorkPlan.class);
                    startActivity(i);
                } else if (position == 3) {
                    Intent i = new Intent(MainActivity.this, PowerManage.class);
                    startActivity(i);
                } else if (position == 4) {
                    Intent i = new Intent(MainActivity.this, ItManage.class);
                    startActivity(i);
                } else if (position == 5) {
                    Intent i = new Intent(MainActivity.this, Other.class);
                    startActivity(i);
                }
            }
        });
    }
    //初始化轮播控件
    private void initCarousel() {
        SliderLayout sliderShow = (SliderLayout) findViewById(R.id.slider);
        sliderShow.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));

        final String[] description = {"数据主机房", "上海机房"};
        for (int i = 0; i<2; i++){
            MySliderView mySliderView = new MySliderView(this);

            final int finalI = i;
            mySliderView
                    .description(description[i])
                    .image(R.drawable.image_room)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            Toast.makeText(MainActivity.this, description[finalI],
                                            Toast.LENGTH_SHORT).show();
                        }
                    });
            sliderShow.addSlider(mySliderView);
        }
        sliderShow.setDuration(8000);
    }

    /**
     * 异步任务: 后台检测告警信息
     */
    class mainAsynTask extends AsyncTask<TextView, Integer, Integer> {
        // 耗时的后台操作
        @Override
        protected Integer doInBackground(TextView... params) {
            // 异步获取告警信息数量
            return 53;
        }

        // doInBackground之前调用,可用于实例化等操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // doInBackground之后调用,用于处理doInBackground的返回结果
        // 输入 == doInBackground的输出
        @Override
        protected void onPostExecute(Integer count) {
            super.onPostExecute(count);
            ImageView warnMessage = (ImageView) findViewById(R.id.grid_warn_message_image);
            BadgeView badge = new BadgeView(MainActivity.this, warnMessage);
            badge.setText(count.toString());
            badge.setBadgeMargin(0);
            badge.show();
        }
    }
}
