package com.buoyantec.eagle_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.buoyantec.eagle_android.ui.activity.R;

import java.util.List;

/**
 * Created by kang on 16/1/19.
 * 告警信息详情页ListView适配器
 */
public class WarnDetailListAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> comments;
    private List<String> types;
    private List<String> updated_at;
    private List<String> alarms;
    private List<String> status;

    public WarnDetailListAdapter(Context c,
                                 List<String> comments,
                                 List<String> types,
                                 List<String> updated_at,
                                 List<String> alarms,
                                 List<String> status) {
        this.mContext = c;
        this.comments = comments;
        this.types = types;
        this.updated_at = updated_at;
        this.alarms = alarms;
        this.status = status;
    }

    @Override
    public int getCount() {
        return comments.size();
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
        TextView status_textView = BaseViewHolder.get(convertView, R.id.warn_detail_status);
        TextView data_textView = BaseViewHolder.get(convertView, R.id.warn_detail_time);
        TextView type = BaseViewHolder.get(convertView, R.id.warn_detail_type);
        TextView alarm = BaseViewHolder.get(convertView, R.id.warn_detail_alarm);

        text_textView.setText("信息:  "+comments.get(position));
        status_textView.setText("状态:  "+status.get(position));
        data_textView.setText(updated_at.get(position));
        type.setText("类型:  "+types.get(position));
        if (alarms.get(position) == null || alarms.get(position).equals("")){
            alarm.setText("备注:  无");
        } else {
            alarm.setText("备注:  "+alarms.get(position));
        }

        return convertView;
    }
}
