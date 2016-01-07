package com.buoyantec.eagle_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by kang on 15/12/31.
 * 自定义适配器
 */
public class MyGridAdapter extends BaseAdapter {
    private Context mContext;

    public MyGridAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.grid_view_text);
        ImageView iv = BaseViewHolder.get(convertView, R.id.grid_view_image);
        iv.setBackgroundResource(mThumbIds[position]);
        tv.setText(mTexts[position]);

        return convertView;
    }

    // references to our images
    private Integer[] mThumbIds = {
        R.drawable.icon_system_status, R.drawable.icon_info,
        R.drawable.icon_work_order, R.drawable.icon_power_manager,
        R.drawable.icon_phone, R.drawable.icon_other
    };
    private String[] mTexts = { "系统状态", "告警信息", "工作安排", "能效管理", "IT管理", "其他" };
}