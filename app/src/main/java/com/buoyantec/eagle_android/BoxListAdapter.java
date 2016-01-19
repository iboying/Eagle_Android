package com.buoyantec.eagle_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by kang on 16/1/19.
 * 列头柜ListView适配器
 */
public class BoxListAdapter extends BaseAdapter {
    private Context mContext;
    private Integer image;
    private String[] texts;
    private Integer[] data;
    private ListView listView;

    public BoxListAdapter(ListView listView, Context c, Integer image,
                                   String[] texts, Integer[] data) {
        this.listView = listView;
        this.mContext = c;
        this.image = image;
        this.texts = texts;
        this.data = data;
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
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.list_item_box, parent, false);
        }
        ImageView iv = BaseViewHolder.get(convertView, R.id.list_item_box_image);
        TextView tv = BaseViewHolder.get(convertView, R.id.list_item_box_text);
        ImageView st_img = BaseViewHolder.get(convertView, R.id.list_item_box_status_image);
        TextView st_text = BaseViewHolder.get(convertView, R.id.list_item_box_status_text);

        iv.setBackgroundResource(image);
        tv.setText(texts[position]);
        if (data[position] == 0){
            st_text.setText("工作失常");
            st_img.setImageResource(R.drawable.box_false);
        }else {
            st_text.setText("工作正常");
            st_img.setImageResource(R.drawable.box_true);
        }

        return convertView;
    }
}
