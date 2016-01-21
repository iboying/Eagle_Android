package com.buoyantec.eagle_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class UpsSystem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载字体图标
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_ups_system);
        //sub_toolbar
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
        subToolbarTitle.setText("UPS系统");
    }

    private void initListView() {
        // references to our images
        Integer image = R.drawable.ups_system;
        // texts of images
        String[] texts = { "UPS1", "UPS2" };
        // UPS数据
        Integer[][] datas = {{75, 75, 75, 75}, {60, 40, 80, 50}};

        ListView listView = (ListView) findViewById(R.id.system_status_listView);
        listView.setAdapter(new SystemStatusListAdapter(listView, this, image, texts, datas));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TextView title = (TextView) v.findViewById(R.id.list_item_power_ups_text);
                Intent i = new Intent(UpsSystem.this, UpsDetail.class);
                i.putExtra("title", title.getText());
                startActivity(i);
            }
        });
    }
}
