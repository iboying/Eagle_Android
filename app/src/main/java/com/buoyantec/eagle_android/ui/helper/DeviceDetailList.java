package com.buoyantec.eagle_android.ui.helper;

import android.content.Context;
import android.widget.ListView;

import com.buoyantec.eagle_android.adapter.DeviceDetailSectionListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kang on 16/3/25.
 * 生成设备详情列表
 */
public class DeviceDetailList {
    private List<HashMap<String, String>> numbers;
    private List<HashMap<String, String>> status;
    private List<HashMap<String, String>> alarms;
    private Context context;
    private ListView listView;

    public DeviceDetailList(Context context,
                            ListView listView,
                            List<HashMap<String, String>> numbers,
                            List<HashMap<String, String>> status,
                            List<HashMap<String, String>> alarms) {
        this.context = context;
        this.listView = listView;
        this.numbers = numbers;
        this.status = status;
        this.alarms = alarms;
    }

    public void setListView() {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> meanings = new ArrayList<>();
        ArrayList<String> colors = new ArrayList<>();

        names.add("参数点");
        meanings.add("");
        colors.add("");
        if (numbers.size() == 0) {
            names.add("无");
            meanings.add("无");
            colors.add("empty");
        }
        for (HashMap<String,String> number : numbers) {
            names.add(number.get("name"));
            meanings.add(number.get("meaning"));
            colors.add(number.get("color"));
        }

        names.add("状态点");
        meanings.add("");
        colors.add("");
        if (status.size() == 0) {
            names.add("无");
            meanings.add("无");
            colors.add("empty");
        }
        for (HashMap<String,String> st : status) {
            names.add(st.get("name"));
            meanings.add(st.get("meaning"));
            colors.add(st.get("color"));
        }

        names.add("告警点");
        meanings.add("");
        colors.add("");
        if (alarms.size() == 0) {
            names.add("无");
            meanings.add("无");
            colors.add("empty");
        }
        for (HashMap<String,String> alarm : alarms) {
            names.add(alarm.get("name"));
            meanings.add(alarm.get("meaning"));
            colors.add(alarm.get("color"));
        }

        // 加载列表
        DeviceDetailSectionListAdapter mAdapter = new DeviceDetailSectionListAdapter(context);
        for (int i = 0; i<names.size(); i++) {
            String name = names.get(i);
            if (name.equals("参数点") || name.equals("状态点") || name.equals("告警点")) {
                mAdapter.addSectionHeaderItem(name);
            } else {
                mAdapter.addItem(names.get(i), meanings.get(i), colors.get(i));
            }
        }
        listView.setAdapter(mAdapter);
    }
}
