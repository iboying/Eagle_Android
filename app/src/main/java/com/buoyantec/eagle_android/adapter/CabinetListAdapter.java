package com.buoyantec.eagle_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.R;

/**
 * Created by kang on 16/1/17.
 * (机柜温度,空调,列头柜) ListView适配器
 */
public class CabinetListAdapter extends BaseAdapter {
    private Context mContext;
    private ListView listView;
    private String[] names;
    //(空调详情)数据
    private String[] status;


    //(设备详情)构造函数
    public CabinetListAdapter(ListView listView, Context c, String[] names, String[] status) {
        this.listView = listView;
        this.mContext = c;
        this.names = names;
        this.status = status;
    }

    @Override
    public int getCount() {
        return names.length;
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
                    .inflate(R.layout.list_item_device_detail, parent, false);
        }

        LinearLayout item = BaseViewHolder.get(convertView, R.id.device_detail_listView_item);
        TextView name = BaseViewHolder.get(convertView, R.id.list_item_device_detail_name);
        TextView st = BaseViewHolder.get(convertView, R.id.list_item_device_detail_status);

        name.setText(names[position]);
        st.setText(status[position]);
        if((position % 2) == 0) {
            item.setBackgroundResource(R.color.white);
        }

        return convertView;
    }
}
