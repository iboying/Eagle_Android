package com.buoyantec.eagle_android.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kang on 16/1/28.
 * 在SharedPreferences自定义存储和读取字符串数组
 */
public class MySharedPreferences {
    private Context context;

    public MySharedPreferences(Context c) {
        this.context = c;
    }

    // 读取数组
    public String[] getSharedPreference(String key) {
        String regularEx = "#";
        String[] str = null;
        SharedPreferences sp = context.getSharedPreferences("foobar", Context.MODE_PRIVATE);
        String values;
        values = sp.getString(key, "");
        str = values.split(regularEx);

        return str;
    }

    // 存储数组
    public void setSharedPreference(String key, String[] values) {
        String regularEx = "#";
        String str = "";
        SharedPreferences sp = context.getSharedPreferences("foobar", Context.MODE_PRIVATE);
        if (values != null && values.length > 0) {
            for (String value : values) {
                str += value;
                str += regularEx;
            }
            SharedPreferences.Editor et = sp.edit();
            et.putString(key, str);
            et.apply();
        }
    }
}
