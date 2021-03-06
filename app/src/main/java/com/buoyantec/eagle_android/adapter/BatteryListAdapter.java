package com.buoyantec.eagle_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.ui.activity.R;

import java.util.List;

/**
 * Created by kang on 16/1/18.
 * 电池检测ListView适配器
 */
public class BatteryListAdapter extends BaseAdapter{
    private Context mContext;
    private Integer image;
    private List<String> names;
    private List<String> alarms;
    private List<List<String>> keys;
    private List<List<String>> values;
    private ListView listView;

    public BatteryListAdapter(ListView listView,
                              Context c,
                              Integer image,
                              List<String> names,
                              List<String> alarms,
                              List<List<String>> keys,
                              List<List<String>> values) {
        this.listView = listView;
        this.mContext = c;
        this.image = image;
        this.names = names;
        this.alarms = alarms;
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
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.list_item_battery, parent, false);
        }
        ImageView iv = BaseViewHolder.get(convertView, R.id.list_item_battery_image);
        TextView tv = BaseViewHolder.get(convertView, R.id.list_item_battery_text);

//        TextView oneKey = BaseViewHolder.get(convertView, R.id.list_item_one_key);
//        TextView twoKey = BaseViewHolder.get(convertView, R.id.list_item_two_key);
//        TextView threeKey = BaseViewHolder.get(convertView, R.id.list_item_three_key);
//        TextView fourKey = BaseViewHolder.get(convertView, R.id.list_item_four_key);
//        // values
//        TextView oneValue = BaseViewHolder.get(convertView, R.id.list_item_one_value);
//        TextView twoValue = BaseViewHolder.get(convertView, R.id.list_item_two_value);
//        TextView threeValue = BaseViewHolder.get(convertView, R.id.list_item_three_value);
//        TextView fourValue = BaseViewHolder.get(convertView, R.id.list_item_four_value);

        tv.setText(names.get(position));
        iv.setImageResource(image);

//        if (keys.size() == 0) {
//            // keys
//            oneKey.setText(" ");
//            twoKey.setText(" ");
//            threeKey.setText(" ");
//            fourKey.setText(" ");
//            // values
//            oneValue.setText(" ");
//            twoValue.setText(" ");
//            threeValue.setText(" ");
//            fourValue.setText(" ");
//        } else {
//            // keys
//            oneKey.setText(keys.get(position).get(0));
//            twoKey.setText(keys.get(position).get(1));
//            threeKey.setText(keys.get(position).get(2));
//            fourKey.setText(keys.get(position).get(3));
//            // values
//            oneValue.setText(values.get(position).get(0));
//            twoValue.setText(values.get(position).get(1));
//            threeValue.setText(values.get(position).get(2));
//            fourValue.setText(values.get(position).get(3));
//        }

        ImageView imageStatus = BaseViewHolder.get(convertView, R.id.list_item_battery_status_image);
        TextView textStatus = BaseViewHolder.get(convertView, R.id.list_item_battery_status_text);

        if (alarms.get(position).equals("false")){
            imageStatus.setBackgroundResource(R.drawable.box_true);
            textStatus.setText("工作正常");
        } else if (alarms.get(position).equals("true")) {
            imageStatus.setBackgroundResource(R.drawable.box_false);
            textStatus.setText("工作失常");
        } else {
            imageStatus.setBackgroundResource(R.drawable.box_false);
            textStatus.setText("暂无数据");
        }

        return convertView;
    }
}
