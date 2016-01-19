package com.buoyantec.eagle_android;

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

public class Box extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载字体图标
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_box);
        //初始化toolbar
        initToolbar();
        //初始化list
        initListView();
    }

    private void initListView() {
        // item数据
        Integer image = R.drawable.power_distribution;
        String[] texts = {"列头柜1", "列头柜2"};
        Integer[] data = {1,0};
        ListView listView = (ListView) findViewById(R.id.box_listView);
        listView.setAdapter(new BoxListAdapter(listView, this, image, texts, data));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View title = view.findViewById(R.id.list_item_box_text);
//                Intent i = new Intent(Box.this, BoxDetail.class);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sub_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView subToolbarTitle = (TextView) findViewById(R.id.sub_toolbar_title);
        subToolbarTitle.setText("列头柜");
    }
}
