package com.buoyantec.eagle_android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.ui.activity.R;

import java.util.ArrayList;

/**
 * Created by kang on 16/2/23.
 * 温湿度系统list适配器
 */
public class TemperatureListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> temperature;
    private ArrayList<String> humidity;
    private ArrayList<String> temColor;
    private ArrayList<String> humColor;

    public TemperatureListAdapter(Context c,
                                  ArrayList<String> temperature,
                                  ArrayList<String> temColor,
                                  ArrayList<String> humidity,
                                  ArrayList<String> humColor) {
        this.mContext = c;
        this.temperature = temperature;
        this.temColor = temColor;
        this.humidity = humidity;
        this.humColor = humColor;
    }

    @Override
    public int getCount() {
        return temperature.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 定义行背景色
        Integer[] colors = {R.color.white, R.color.stripListViewGrayColor};

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.list_item_temperature, parent, false);
        }

        LinearLayout item = BaseViewHolder.get(convertView, R.id.device_detail_listView_item);
        TextView num = BaseViewHolder.get(convertView, R.id.list_item_temperature_num);
        TextView tem = BaseViewHolder.get(convertView, R.id.list_item_temperature_temperature);
        TextView hum = BaseViewHolder.get(convertView, R.id.list_item_temperature_humidity);

        item.setBackgroundResource(colors[position % 2]);
        num.setText(String.valueOf(position + 1));

        if (temperature.size() > position) {
            tem.setText(temperature.get(position) + " ℃");
            if (temColor.get(position).equals("blue")) {
                tem.setTextColor(Color.parseColor("#105DF7"));
            } else if (temColor.get(position).equals("red")) {
                tem.setTextColor(Color.parseColor("#FF2600"));
            } else {
                tem.setTextColor(Color.parseColor("#44DB35"));
            }
        }

        if (humidity.size() > position) {
            hum.setText(humidity.get(position) + " %");
            if (humColor.get(position).equals("blue")) {
                hum.setTextColor(Color.parseColor("#105DF7"));
            } else if (humColor.get(position).equals("red")) {
                hum.setTextColor(Color.parseColor("#FF2600"));
            } else {
                hum.setTextColor(Color.parseColor("#44DB35"));
            }
        }

        return convertView;
    }
}
