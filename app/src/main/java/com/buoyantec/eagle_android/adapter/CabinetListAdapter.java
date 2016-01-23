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
    //(机柜温度,列头柜)数据
    private Integer[][] datas;
    //(空调详情)数据
    private String[] status;

    //(机柜温度,列头柜)构造函数
    public CabinetListAdapter(ListView listView, Context c, String[] names, Integer[][] datas) {
        this.listView = listView;
        this.mContext = c;
        this.names = names;
        this.datas = datas;
    }
    //(空调详情)构造函数
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
        if (status == null) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext)
                        .inflate(R.layout.list_item_cabinet_detail, parent, false);
            }

            LinearLayout item = BaseViewHolder.get(convertView, R.id.cabinet_listView_item);
            TextView name = BaseViewHolder.get(convertView, R.id.list_item_cabinet_name);
            TextView temperature = BaseViewHolder.get(convertView, R.id.list_item_cabinet_temperature);
            TextView humidity = BaseViewHolder.get(convertView, R.id.list_item_cabinet_humidity);

            name.setText(names[position]);
            temperature.setText(datas[position][0].toString());
            humidity.setText(datas[position][1].toString());
            if((position % 2) == 0) {
                item.setBackgroundResource(R.color.white);
            }
        }else{
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext)
                        .inflate(R.layout.list_item_precision_air_detail, parent, false);
            }

            LinearLayout item = BaseViewHolder.get(convertView, R.id.precision_air_detail_listView_item);
            TextView name = BaseViewHolder.get(convertView, R.id.list_item_precision_air_detail_name);
            TextView st = BaseViewHolder.get(convertView, R.id.list_item_precision_air_detail_status);

            name.setText(names[position]);
            st.setText(status[position]);
            if((position % 2) == 0) {
                item.setBackgroundResource(R.color.white);
            }
        }

        return convertView;
    }
}
