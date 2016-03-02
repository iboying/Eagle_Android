package com.buoyantec.eagle_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.buoyantec.eagle_android.R;

import java.util.List;

/**
 * Created by kang on 16/1/19.
 * 告警信息详情页ListView适配器
 */
public class WarnDetailListAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> names;
    private List<String> updated_at;

    public WarnDetailListAdapter(Context c, List<String> names, List<String> updated_at) {
        this.mContext = c;
        this.names = names;
        this.updated_at = updated_at;
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
                    .inflate(R.layout.list_item_warn_detail, parent, false);
        }
        TextView text_textView = BaseViewHolder.get(convertView, R.id.warn_detail_name);
        TextView data_textView = BaseViewHolder.get(convertView, R.id.warn_detail_time);

        text_textView.setText(names.get(position));
        data_textView.setText(updated_at.get(position));

        return convertView;
    }
}
