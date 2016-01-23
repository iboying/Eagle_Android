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
 * Created by kang on 16/1/15.
 * 系统状态子页list适配器( UPS系统, 配电系统 )
 */
public class SystemStatusListAdapter extends BaseAdapter {
    private Context mContext;
    private Integer image;
    private String[] texts;
    private Integer[][] datas;
    private ListView listView;

    public SystemStatusListAdapter(ListView listView, Context c, Integer image,
                                   String[] texts, Integer[][] datas) {
        this.listView = listView;
        this.mContext = c;
        this.image = image;
        this.texts = texts;
        this.datas = datas;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_power_ups, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.list_item_power_ups_text);
        ImageView iv = BaseViewHolder.get(convertView, R.id.list_item_power_ups_image);
        TextView av = BaseViewHolder.get(convertView, R.id.list_item_power_ups_av);
        TextView bv = BaseViewHolder.get(convertView, R.id.list_item_power_ups_bv);
        TextView cv = BaseViewHolder.get(convertView, R.id.list_item_power_ups_cv);
        TextView a = BaseViewHolder.get(convertView, R.id.list_item_power_ups_a);

        tv.setText(texts[position]);
        iv.setImageResource(image);
        av.setText(datas[position][0].toString()+"%");
        bv.setText(datas[position][1].toString()+"%");
        cv.setText(datas[position][2].toString()+"%");
        a.setText(datas[position][3].toString()+"%");

        return convertView;
    }
}
