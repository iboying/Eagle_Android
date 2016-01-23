package com.buoyantec.eagle_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.buoyantec.eagle_android.adapter.MainGridAdapter;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载字体图标
        Iconify.with(new FontAwesomeModule());
        //加载布局文件
        setContentView(R.layout.activity_main);
        //初始化toolbar和侧边栏
        initToolBar();
        //图片轮播
        initCarousel();
        //初始化GridView
        initGridView();
    }

    public void initToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        TextView subToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        subToolbarTitle.setText("中国地质大学");

        //添加侧边菜单,并绑定ToolBar菜单按钮
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

        String[] description = {"数据主机房", "上海机房"};
        for (int i = 0; i<2; i++){
            TextSliderView textSliderView = new TextSliderView(this);

            textSliderView
                    .description(description[i])
                    .image(R.drawable.image_room);
            sliderShow.addSlider(textSliderView);
        }
        sliderShow.setDuration(8000);
    }
}
