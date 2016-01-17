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
 * Created by kang on 16/1/15.
 * 系统状态子页list适配器( UPS系统, 配电系统 )
 */
public class SystemStatusListAdapter extends BaseAdapter {
    private Context mContext;
    private Integer image;
    private String[] texts;
    private ListView listView;

    public SystemStatusListAdapter(ListView listView, Context c, Integer image, String[] texts) {
        this.listView = listView;
        this.mContext = c;
        this.image = image;
        this.texts = texts;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_system_status, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.power_distribution_text);

        tv.setText(texts[position]);
        return convertView;
    }
}
