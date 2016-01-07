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
 * Created by kang on 16/1/7.
 */
public class MainListAdapter extends BaseAdapter{
    private Context mContext;
    private Integer[] images;
    private String[] texts;
    private ListView listView;

    public MainListAdapter(ListView listView, Context c, Integer[] images, String[] texts) {
        this.listView = listView;
        this.mContext = c;
        this.images = images;
        this.texts = texts;
    }

    @Override
    public int getCount() {
        return images.length;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.list_item_text);
        ImageView iv = BaseViewHolder.get(convertView, R.id.list_item_image);

        iv.setBackgroundResource(images[position]);
        tv.setText(texts[position]);

        return convertView;
    }
}
