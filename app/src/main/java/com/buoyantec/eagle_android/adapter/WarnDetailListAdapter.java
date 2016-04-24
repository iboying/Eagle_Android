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
    private List<String> pointNames;
    private List<String> meanings;
    private List<String> reportedAt;
    private List<Boolean> checks;

    public WarnDetailListAdapter(Context c,
                                 List<String> pointNames,
                                 List<String> meanings,
                                 List<String> reportedAt,
                                 List<Boolean> checks) {
        this.mContext = c;
        this.meanings = meanings;
        this.pointNames = pointNames;
        this.reportedAt = reportedAt;
        this.checks = checks;
    }
    @Override
    public int getCount() {
        return meanings.size();
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
        TextView point = BaseViewHolder.get(convertView, R.id.warn_detail_name);
        TextView text_textView = BaseViewHolder.get(convertView, R.id.warn_detail_comment);
        TextView data_textView = BaseViewHolder.get(convertView, R.id.warn_detail_time);
        TextView checked_icon = BaseViewHolder.get(convertView, R.id.device_detail_checked);

        point.setText(pointNames.get(position));
        text_textView.setText("描述:  " + meanings.get(position));
        data_textView.setText("时间: " + reportedAt.get(position));

        if (checks.get(position)) {
            checked_icon.setTextColor(mContext.getResources().getColor(R.color.gray));
        } else {
            checked_icon.setTextColor(mContext.getResources().getColor(R.color.list_dot_red));
        }

        return convertView;
    }
}
