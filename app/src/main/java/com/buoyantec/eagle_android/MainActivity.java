package com.buoyantec.eagle_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //home_toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //侧边菜单
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //图片轮播
        initCarousel();

        //GridView
        initGridView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

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
        GridView gridview = (GridView) findViewById(R.id.grid_view);
        gridview.setAdapter(new ImageAdapter(this));
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
        TextSliderView textSliderView = new TextSliderView(this);
        textSliderView
                .description("数据主机房")
                .image(R.drawable.image_room);
        sliderShow.addSlider(textSliderView);
        sliderShow.setDuration(8000);
    }
}
