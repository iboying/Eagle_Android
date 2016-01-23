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

import com.buoyantec.eagle_android.adapter.SystemStatusListAdapter;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class PowerDistribution extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载字体图标
        Iconify.with(new FontAwesomeModule());
        //加载布局文件
        setContentView(R.layout.activity_power_distribution);
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
        subToolbarTitle.setText("配电系统");
    }

    private void initListView() {
        // 配电柜图片
        Integer image = R.drawable.power_distribution;
        // 配电柜名称
        String[] texts = { "配电柜1", "配电柜2" };
        // 配电柜数据
        Integer[][] datas = {{60, 40, 80, 50}, {60, 40, 80, 50}};
        ListView listView = (ListView) findViewById(R.id.system_status_listView);
        listView.setAdapter(new SystemStatusListAdapter(listView, this, image, texts, datas));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TextView title = (TextView) v.findViewById(R.id.list_item_power_ups_text);
                Intent i = new Intent(PowerDistribution.this, PowerDetail.class);
                i.putExtra("title", title.getText());
                startActivity(i);
            }
        });
    }
}
