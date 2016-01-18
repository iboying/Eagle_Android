package com.buoyantec.eagle_android;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class Battery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载字体图标
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_battery);
        //初始化toolbar
        initToolbar();
        //初始化list
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
        subToolbarTitle.setText("电池检测");
    }

    private void initListView() {
        // references to our images
        Integer image = R.drawable.battery;
        // texts of images
        String[] texts = { "电池组1", "电池组2" };
        Integer[][] data = {{400,100,26,30}, {400,100,26,30}};
        ListView listView = (ListView) findViewById(R.id.battery_listView);
        listView.setAdapter(new BatteryListAdapter(listView, this, image, texts, data));
    }
}
