package com.buoyantec.iGrid.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.buoyantec.iGrid.ui.activity.R;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;

/**
 * Created by kang on 16/1/24.
 * 重定义轮播组件
 */
public class MySliderView extends BaseSliderView {
    private Context context;

    public MySliderView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Bundle getBundle() {
        return super.getBundle();
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.slider_images,null);
        ImageView target = (ImageView)v.findViewById(R.id.main_slider_image);
        TextView description = (TextView)v.findViewById(R.id.main_description);
        description.setText(getDescription());
        bindEventAndShow(v, target);
        return v;
    }
}
