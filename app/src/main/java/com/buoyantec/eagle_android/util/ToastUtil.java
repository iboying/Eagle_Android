package com.buoyantec.eagle_android.util;

import android.support.annotation.StringRes;
import android.widget.Toast;

import com.buoyantec.eagle_android.App;

/**
 * Created by kang on 16/3/3.
 * Toast工具
 */
public class ToastUtil {

    private ToastUtil() {
    }

    public static void show(CharSequence text) {
        if (text.length() < 10) {
            Toast.makeText(App.getInstance(), text, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(App.getInstance(), text, Toast.LENGTH_LONG).show();
        }
    }

    public static void show(@StringRes int resId) {
        show(App.getInstance().getString(resId));
    }
}
