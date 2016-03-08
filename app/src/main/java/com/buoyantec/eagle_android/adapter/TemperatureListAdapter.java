package com.buoyantec.eagle_android.adapter;

import android.content.Context;
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
    private ListView listView;
    private ArrayList<String> temperature;
    private ArrayList<String> humidity;

    public TemperatureListAdapter(ListView listView, Context c, ArrayList<String> temperature, ArrayList<String> humidity) {
        this.listView = listView;
        this.mContext = c;
        this.temperature = temperature;
        this.humidity = humidity;
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
        TextView name = BaseViewHolder.get(convertView, R.id.list_item_temperature_temperature);
        TextView st = BaseViewHolder.get(convertView, R.id.list_item_temperature_humidity);

        item.setBackgroundResource(colors[position % 2]);
        num.setText(String.valueOf(position+1));
        name.setText(temperature.get(position)+"℃");
        st.setText(humidity.get(position)+"%");

        return convertView;
    }
}
