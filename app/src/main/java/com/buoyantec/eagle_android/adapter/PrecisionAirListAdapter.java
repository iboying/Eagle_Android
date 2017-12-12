package com.buoyantec.eagle_android.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.ui.activity.R;
import com.joanzapata.iconify.widget.IconTextView;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by kang on 16/1/18.
 * 精密空调ListView适配器
 */
public class PrecisionAirListAdapter extends BaseAdapter {
    private Context mContext;
    private Integer image;
    private List<String> texts;
    private List<List<String>> labels;
    private List<List<String>> datas;
    private List<Integer> status;
    private ListView listView;

    public PrecisionAirListAdapter(ListView listView,
                                   Context c,
                                   Integer image,
                                   List<String> names,
                                   List<List<String>> labels,
                                   List<List<String>> datas,
                                   List<Integer> status) {
        this.listView = listView;
        this.mContext = c;
        this.image = image;
        this.texts = names;
        this.labels = labels;
        this.datas = datas;
        this.status = status;
    }

    @Override
    public int getCount() {
        return texts.size();
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
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 没有状态类型的列表
        if (status.get(position) == 2){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_precision_air, parent, false);

            ImageView iv = BaseViewHolder.get(convertView, R.id.list_item_precision_air_image);
            TextView tv = BaseViewHolder.get(convertView, R.id.list_item_precision_air_text);

            TextView degree_label = BaseViewHolder.get(convertView, R.id.list_item_degree_label);
            TextView humidity_label = BaseViewHolder.get(convertView, R.id.list_item_humidity_label);

            TextView degree = BaseViewHolder.get(convertView, R.id.list_item_precision_air_degree);
            TextView humidity = BaseViewHolder.get(convertView, R.id.list_item_precision_air_humidity);

            iv.setBackgroundResource(image);
            tv.setText(texts.get(position));

            if (labels.get(position).size() > 0) {
                degree_label.setText("{fa-square #FF987E} "+labels.get(position).get(0));
                degree.setText(datas.get(position).get(0)+"℃");
                humidity_label.setText("{fa-square #00BDFF} "+labels.get(position).get(1));
                humidity.setText(datas.get(position).get(1)+"%");
            } else {
                degree.setText("0"+"℃");
                humidity.setText("0"+"%");
            }
        }
        // 存在状态类型的(冷水机组, 室外机)
        else {
            convertView = LayoutInflater.from(mContext)
                        .inflate(R.layout.list_item_device_status, parent, false);

            ImageView iv = BaseViewHolder.get(convertView, R.id.list_item_device_status_image);
            TextView tv = BaseViewHolder.get(convertView, R.id.list_item_device_status_text);
            ImageView imageStatus = BaseViewHolder.get(convertView, R.id.list_item_device_status_status_image);
            TextView textStatus = BaseViewHolder.get(convertView, R.id.list_item_device_status_status_text);

            iv.setImageResource(image);
            tv.setText(texts.get(position));

            if (status.get(position) == 0){
                imageStatus.setBackgroundResource(R.drawable.box_true);
                textStatus.setText("工作正常");
            } else if (status.get(position) == 1) {
                imageStatus.setBackgroundResource(R.drawable.box_false);
                textStatus.setText("工作失常");
            } else {
                imageStatus.setBackgroundResource(R.drawable.box_false);
                textStatus.setText("暂无数据");
            }
        }

        return convertView;
    }
}
