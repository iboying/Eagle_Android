package com.buoyantec.iGrid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.iGrid.ui.activity.R;

import java.util.List;

/**
 * Created by kang on 16/1/12.
 * 能效管理页面的ListView适配器, 含有第二级跳转
 */
public class StandardListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Integer> images;
    private Integer image;
    private List<String> names;
    private ListView listView;

    // 动态图标
    public StandardListAdapter(ListView listView, Context c, List<Integer> images, List<String> names) {
        this.listView = listView;
        this.mContext = c;
        this.images = images;
        this.names = names;
    }

    // 静态图标
    public StandardListAdapter(ListView listView, Context c, Integer image, List<String> names) {
        this.listView = listView;
        this.mContext = c;
        this.image = image;
        this.names = names;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_standard_list, parent, false);
        }
        ImageView iv = BaseViewHolder.get(convertView, R.id.list_item_standard_list_image);
        TextView tv = BaseViewHolder.get(convertView, R.id.list_item_standard_list_text);

        if (image != null) {
            iv.setBackgroundResource(image);
        } else {
            iv.setBackgroundResource(images.get(position));
        }

        System.out.println(names.get(position)+"=====");
        tv.setText(names.get(position));

        return convertView;
    }
}
