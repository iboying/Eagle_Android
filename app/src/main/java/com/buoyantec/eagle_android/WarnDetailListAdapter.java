package com.buoyantec.eagle_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by kang on 16/1/19.
 * 告警信息详情页ListView适配器
 */
public class WarnDetailListAdapter extends BaseAdapter {
    private Context mContext;
    private String[] texts;
    private Integer[] data;
    private ListView listView;

    public WarnDetailListAdapter(ListView listView, Context c, String[] texts, Integer[] data) {
        this.listView = listView;
        this.mContext = c;
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
                    .inflate(R.layout.list_item_warn_detail, parent, false);
        }
        TextView text_textView = BaseViewHolder.get(convertView, R.id.list_item_warn_detail_text);
        TextView data_textView = BaseViewHolder.get(convertView, R.id.list_item_warn_detail_data);

        text_textView.setText(texts[position]);
        data_textView.setText(data[position].toString()+"%");

        return convertView;
    }
}
