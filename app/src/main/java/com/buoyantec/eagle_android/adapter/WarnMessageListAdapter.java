package com.buoyantec.eagle_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.ui.customView.BadgeView;
import com.buoyantec.eagle_android.ui.activity.R;

import java.util.List;

/**
 * Created by kang on 16/1/7.
 * 告警信息页面适配器, 不含有第二级跳转, 只显示状态信息
 */
public class WarnMessageListAdapter extends BaseAdapter{
    private Context mContext;
    private List<Integer> images;
    private List<String> texts;
    private List<Integer> count;
    private ListView listView;
    private LayoutInflater mInflater;

    public WarnMessageListAdapter(ListView listView, Context c, List<Integer> images, List<String> texts,
                                  List<Integer> count) {
        this.listView = listView;
        this.mContext = c;
        this.images = images;
        this.texts = texts;
        this.count = count;
        mInflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return count.size();
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
        SystemAlarmViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new SystemAlarmViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.list_item_text);
            holder.icon = (ImageView) convertView.findViewById(R.id.list_item_image);
            holder.status = (TextView) convertView.findViewById(R.id.list_item_status);
            holder.badge = new BadgeView(mContext, holder.status);
            convertView.setTag(holder);
        } else {
            holder = (SystemAlarmViewHolder) convertView.getTag();
        }

        if (images.get(position) != null) {
            holder.icon.setBackgroundResource(images.get(position));
        }else{
            holder.icon.setBackgroundResource(R.drawable.system_status_air);
        }

        holder.text.setText(texts.get(position));

        if (count.get(position) != 0) {
            holder.status.setText("");
            holder.badge.setText(count.get(position).toString());
            holder.badge.show();
        } else {
            holder.status.setText("正常");
            holder.badge.hide();
        }

        return convertView;
    }
}
