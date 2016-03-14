package com.buoyantec.iGrid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.iGrid.ui.activity.R;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;

/**
 * Created by kang on 16/2/23.
 * 漏水系统list适配器
 */
public class WaterListAdapter extends BaseAdapter {
    private Context mContext;
    private ListView listView;
    private ArrayList<String> names;
    private ArrayList<String> status;

    public WaterListAdapter(ListView listView, Context c, ArrayList<String> names, ArrayList<String> status) {
        this.listView = listView;
        this.mContext = c;
        this.names = names;
        this.status = status;
    }

    @Override
    public int getCount() {
        return names.size();
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
        name.setText(names.get(position));

        if (status.get(position).equals("true")) {
            st.setTextColor(mContext.getResources().getColor(R.color.list_dot_red));
        }

        return convertView;
    }
}
