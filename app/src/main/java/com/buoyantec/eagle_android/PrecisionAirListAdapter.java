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
 * 精密空调ListView适配器
 */
public class PrecisionAirListAdapter extends BaseAdapter {
    private Context mContext;
    private Integer image;
    private String[] texts;
    private Integer[][] data;
    private ListView listView;

    public PrecisionAirListAdapter(ListView listView, Context c, Integer image,
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
                    .inflate(R.layout.list_item_precision_air, parent, false);
        }
        ImageView iv = BaseViewHolder.get(convertView, R.id.list_item_precision_air_image);
        TextView tv = BaseViewHolder.get(convertView, R.id.list_item_precision_air_text);
        TextView degree = BaseViewHolder.get(convertView, R.id.list_item_precision_air_degree);
        TextView humidity = BaseViewHolder.get(convertView, R.id.list_item_precision_air_humidity);

        iv.setBackgroundResource(image);
        tv.setText(texts[position]);
        degree.setText(data[position][0].toString()+"℃");
        humidity.setText(data[position][1].toString()+"%");

        return convertView;
    }
}
