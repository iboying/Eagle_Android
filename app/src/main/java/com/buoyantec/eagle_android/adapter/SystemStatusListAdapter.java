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

import java.util.List;

/**
 * Created by kang on 16/1/15.
 * 系统状态子页list适配器( UPS系统, 电量仪系统 )
 */
public class SystemStatusListAdapter extends BaseAdapter {
    private Context mContext;
    private Integer image;
    private List<String> names;
    private List<List<String>> keys;
    private List<List<String>> values;
    private ListView listView;

    public SystemStatusListAdapter(ListView listView,
                                   Context c,
                                   Integer image,
                                   List<String> names,
                                   List<List<String>> keys,
                                   List<List<String>> values) {
        this.listView = listView;
        this.mContext = c;
        this.image = image;
        this.names = names;
        this.keys = keys;
        this.values = values;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_power_ups, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.list_item_power_ups_text);
        ImageView iv = BaseViewHolder.get(convertView, R.id.list_item_power_ups_image);
        // keys
        TextView oneKey = BaseViewHolder.get(convertView, R.id.list_item_one_key);
        TextView twoKey = BaseViewHolder.get(convertView, R.id.list_item_two_key);
        TextView threeKey = BaseViewHolder.get(convertView, R.id.list_item_three_key);
        TextView fourKey = BaseViewHolder.get(convertView, R.id.list_item_four_key);
        // values
        TextView oneValue = BaseViewHolder.get(convertView, R.id.list_item_one_value);
        TextView twoValue = BaseViewHolder.get(convertView, R.id.list_item_two_value);
        TextView threeValue = BaseViewHolder.get(convertView, R.id.list_item_three_value);
        TextView fourValue = BaseViewHolder.get(convertView, R.id.list_item_four_value);

        tv.setText(names.get(position));
        iv.setImageResource(image);

        // keys
        oneKey.setText(keys.get(position).get(0));
        twoKey.setText(keys.get(position).get(1));
        threeKey.setText(keys.get(position).get(2));
        fourKey.setText(keys.get(position).get(3));
        // values
        oneValue.setText(values.get(position).get(0));
        twoValue.setText(values.get(position).get(1));
        threeValue.setText(values.get(position).get(2));
        fourValue.setText(values.get(position).get(3));

        return convertView;
    }
}
