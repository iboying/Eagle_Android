package com.buoyantec.eagle_android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.R;
import com.joanzapata.iconify.widget.IconTextView;

/**
 * Created by kang on 16/2/23.
 * 漏水系统list适配器
 */
public class WaterListAdapter extends BaseAdapter {
    private Context mContext;
    private ListView listView;
    private String[] names;
    private Integer[] status;

    public WaterListAdapter(ListView listView, Context c, String[] names, Integer[] status) {
        this.listView = listView;
        this.mContext = c;
        this.names = names;
        this.status = status;
    }

    @Override
    public int getCount() {
        return names.length;
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
                    .inflate(R.layout.list_item_water, parent, false);
        }

        LinearLayout item = BaseViewHolder.get(convertView, R.id.device_detail_listView_item);
        TextView name = BaseViewHolder.get(convertView, R.id.list_item_water_name);
        IconTextView st = BaseViewHolder.get(convertView, R.id.list_item_water_status);

        item.setBackgroundResource(colors[position % 2]);
        name.setText(names[position]);
        if (status[position] == 1) {
            st.setTextColor(mContext.getResources().getColor(R.color.list_dot_green));
        }

        return convertView;
    }
}
