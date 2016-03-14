package com.buoyantec.iGrid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.buoyantec.iGrid.ui.activity.R;

/**
 * Created by kang on 16/3/4.
 * 菜单选项
 */
public class ToolbarMenuAdapter extends BaseAdapter {
    private Context context;
    private String[] rooms;

    public ToolbarMenuAdapter(Context c, String[] rooms) {
        this.context = c;
        this.rooms = rooms;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.list_item_toolbar_menu, parent, false);
        }

        TextView  room = BaseViewHolder.get(convertView, R.id.list_item_toolbar_menu_name);

        room.setText(rooms[position]);

        return convertView;
    }
}
