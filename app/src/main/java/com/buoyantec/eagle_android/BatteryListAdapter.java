package com.buoyantec.eagle_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by kang on 16/1/18.
 * 电池检测ListView适配器
 */
public class BatteryListAdapter extends BaseAdapter{
    private Context mContext;
    private Integer image;
    private String[] texts;
    private Integer[][] data;
    private ListView listView;

    public BatteryListAdapter(ListView listView, Context c, Integer image,
                                   String[] texts, Integer[][] data) {
        this.listView = listView;
        this.mContext = c;
        this.image = image;
        this.texts = texts;
        this.data = data;
    }

    @Override
    public int getCount() {
        return texts.length;
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
                    .inflate(R.layout.list_item_battery, parent, false);
        }
        ImageView iv = BaseViewHolder.get(convertView, R.id.list_item_battery_image);
        TextView tv = BaseViewHolder.get(convertView, R.id.list_item_battery_text);
        TextView v = BaseViewHolder.get(convertView, R.id.list_item_battery_v);
        TextView a = BaseViewHolder.get(convertView, R.id.list_item_battery_a);
        TextView t1 = BaseViewHolder.get(convertView, R.id.list_item_battery_t1);
        TextView t2 = BaseViewHolder.get(convertView, R.id.list_item_battery_t2);

        iv.setBackgroundResource(image);
        tv.setText(texts[position]);
        v.setText(data[position][0].toString()+"v");
        a.setText(data[position][1].toString()+"a");
        t1.setText(data[position][2].toString()+"度");
        t2.setText(data[position][3].toString()+"度");

        return convertView;
    }
}
