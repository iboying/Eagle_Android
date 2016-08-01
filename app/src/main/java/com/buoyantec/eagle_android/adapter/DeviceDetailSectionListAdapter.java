package com.buoyantec.eagle_android.adapter;

import java.util.ArrayList;
import java.util.TreeSet;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buoyantec.eagle_android.ui.activity.R;
import com.joanzapata.iconify.widget.IconTextView;

/**
 * 带有头部分类的ListView
 */
public class DeviceDetailSectionListAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mStatus = new ArrayList<>();
    private ArrayList<String> mColors = new ArrayList<>();
    private TreeSet<Integer> sectionHeader = new TreeSet<>();

    private LayoutInflater mInflater;

    public DeviceDetailSectionListAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final String name, final String status, final String color) {
        // 给正常的item赋值
        mNames.add(name);
        mStatus.add(status);
        mColors.add(color);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        // header时 , 随便给变量赋值,占位
        mNames.add(item);
        mStatus.add(item);
        mColors.add(item);
        sectionHeader.add(mNames.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mNames.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // 定义行背景色
        Integer[] colors = {R.color.white, R.color.stripListViewGrayColor};

        ViewHolder holder;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.list_section_device_item, null);
//                    holder.icon = (IconTextView) convertView.findViewById(R.id.list_item_device_detail_icon);
                    holder.textView = (TextView) convertView.findViewById(R.id.list_item_device_detail_name);
                    holder.status = (TextView) convertView.findViewById(R.id.list_item_device_detail_status);
                    holder.item = (LinearLayout) convertView.findViewById(R.id.device_detail_list_item);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.list_section_device_header, null);
//                    holder.icon = (IconTextView) convertView.findViewById(R.id.list_item_device_list_icon);
                    holder.textView = (TextView) convertView.findViewById(R.id.device_detail_list_title);
                    holder.status = (TextView) convertView.findViewById(R.id.device_detail_list_status);
                    holder.item = (LinearLayout) convertView.findViewById(R.id.device_detail_list_header);
                    break;
            }
            if (convertView != null)
                convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(mNames.get(position));
        holder.status.setText(mStatus.get(position));
        if (!sectionHeader.contains(position)) {
            holder.item.setBackgroundResource(colors[position % 2]);
        }
        if (mColors.get(position).equals("blue")) {
            holder.textView.setTextColor(Color.parseColor("#6F7179"));
            holder.status.setTextColor(Color.parseColor("#105DF7"));
        } else if (mColors.get(position).equals("red")) {
            holder.textView.setTextColor(Color.parseColor("#6F7179"));
            holder.status.setTextColor(Color.parseColor("#FF2600"));
        } else if (mColors.get(position).equals("green")){
            holder.textView.setTextColor(Color.parseColor("#6F7179"));
            holder.status.setTextColor(Color.parseColor("#44DB35"));
        } else if (mColors.get(position).equals("black")){
            holder.textView.setTextColor(Color.parseColor("#6F7179"));
            holder.status.setTextColor(Color.parseColor("#6F7179"));
        } else {
            // 此处判断,是为了设置header的文字颜色
            holder.textView.setTextColor(Color.parseColor("#ffffff"));
            holder.status.setTextColor(Color.parseColor("#ffffff"));
        }
        return convertView;
    }

    public static class ViewHolder {
//        public IconTextView icon;
        public TextView textView;
        public TextView status;
        public LinearLayout item;
    }
}
