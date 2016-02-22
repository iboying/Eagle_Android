package com.buoyantec.eagle_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.R;

/**
 * Created by kang on 16/1/12.
 * 能效管理页面的ListView适配器, 含有第二级跳转
 */
public class PowerManageListAdapter extends BaseAdapter {
    private Context mContext;
    private Integer[] images;
    private Integer image;
    private String[] texts;
    private ListView listView;

    // 动态图标
    public PowerManageListAdapter(ListView listView, Context c, Integer[] images, String[] texts) {
        this.listView = listView;
        this.mContext = c;
        this.images = images;
        this.texts = texts;
    }

    // 静态图标
    public PowerManageListAdapter(ListView listView, Context c, Integer image, String[] texts) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_power_manage, parent, false);
        }
        ImageView iv = BaseViewHolder.get(convertView, R.id.list_item_power_manage_image);
        TextView tv = BaseViewHolder.get(convertView, R.id.list_item_power_manage_text);

        if (image != null) {
            iv.setBackgroundResource(image);
        } else {
            iv.setBackgroundResource(images[position]);
        }
        tv.setText(texts[position]);

        return convertView;
    }
}
