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
 * Created by kang on 16/2/23.
 * 设备状态列表适配器
 * 配电系统
 * {工作正常, 工作失常}
 */
public class DeviceStatusListAdapter extends BaseAdapter {
    private Context mContext;
    private Integer image;
    private List<String> names;
    private List<Integer> status;
    private ListView listView;

    public DeviceStatusListAdapter(ListView listView,
                                   Context c,
                                   Integer image,
                                   List<String> names,
                                   List<Integer> status) {
        this.listView = listView;
        this.mContext = c;
        this.image = image;
        this.names = names;
        this.status = status;
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
                    .inflate(R.layout.list_item_device_status, parent, false);
        }

        ImageView iv = BaseViewHolder.get(convertView, R.id.list_item_device_status_image);
        TextView tv = BaseViewHolder.get(convertView, R.id.list_item_device_status_text);
        ImageView imageStatus = BaseViewHolder.get(convertView, R.id.list_item_device_status_status_image);
        TextView textStatus = BaseViewHolder.get(convertView, R.id.list_item_device_status_status_text);

        iv.setImageResource(image);
        tv.setText(names.get(position));
        if (status.get(position) == 1) {
            imageStatus.setBackgroundResource(R.drawable.box_false);
            textStatus.setText("工作失常");
        } else if (status.get(position) == 0){
            imageStatus.setBackgroundResource(R.drawable.box_true);
            textStatus.setText("工作正常");
        } else {
            imageStatus.setBackgroundResource(R.drawable.box_false);
            textStatus.setText("暂无数据");
        }

        return convertView;
    }
}
