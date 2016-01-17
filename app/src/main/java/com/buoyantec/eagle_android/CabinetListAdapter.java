package com.buoyantec.eagle_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by kang on 16/1/17.
 * (机柜温度,空调,列头柜) ListView适配器
 */
public class CabinetListAdapter extends BaseAdapter {
    private Context mContext;
    private String[] names;
    private Integer[][] datas;
    private ListView listView;

    public CabinetListAdapter(ListView listView, Context c, String[] names, Integer[][] datas) {
        this.listView = listView;
        this.mContext = c;
        this.names = names;
        this.datas = datas;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.list_item_cabinet, parent, false);
        }

        LinearLayout item = BaseViewHolder.get(convertView, R.id.cabinet_listView_item);
        TextView name = BaseViewHolder.get(convertView, R.id.list_item_cabinet_name);
        TextView temperature = BaseViewHolder.get(convertView, R.id.list_item_cabinet_temperature);
        TextView humidity = BaseViewHolder.get(convertView, R.id.list_item_cabinet_humidity);

        name.setText(names[position]);
        temperature.setText(datas[position][0].toString()+"度");
        humidity.setText(datas[position][1].toString()+"%");

        if((position % 2) == 0) {
            item.setBackgroundResource(R.color.white);
        }

        return convertView;
    }
}
