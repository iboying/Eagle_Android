package com.buoyantec.iGrid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.buoyantec.iGrid.ui.activity.R;

/**
 * Created by kang on 16/1/20.
 * (UPS系统详情页面,配电系统详情页面)GridView适配器
 */
public class PowerUpsGridViewAdapter extends BaseAdapter {
    private Context mContext;
    //使用的item模板
    private int item_layout;
    private String[] texts;
    private Integer[] data;
    private GridView gridView;

    public PowerUpsGridViewAdapter(GridView gridView, Context c, int item_layout,
                                   String[] texts, Integer[] data){
        this.mContext = c;
        this.item_layout = item_layout;
        this.texts = texts;
        this.data = data;
        this.gridView = gridView;
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
            convertView = LayoutInflater.from(mContext).inflate(item_layout, parent, false);
        }
        TextView tx = BaseViewHolder.get(convertView, R.id.grid_item_green_text);
        TextView dt = BaseViewHolder.get(convertView, R.id.grid_item_green_data);

        tx.setText(texts[position]);
        dt.setText(data[position].toString());

        return convertView;
    }
}
