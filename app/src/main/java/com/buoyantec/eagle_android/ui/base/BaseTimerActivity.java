package com.buoyantec.eagle_android.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buoyantec.eagle_android.App;
import com.buoyantec.eagle_android.engine.Engine;
import com.buoyantec.eagle_android.ui.activity.R;
import com.buoyantec.eagle_android.util.ToastUtil;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kang on 16/3/3.
 * 全局操作
 */
public abstract class BaseTimerActivity extends AppCompatActivity implements View.OnClickListener{
    protected String TAG;
    protected App mApp;
    protected static Engine mEngine;
    protected Engine mNoHeaderEngine;
    private SweetAlertDialog mLoadingDialog;
    protected SharedPreferences sp;
    private TextView networkState;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        mApp = App.getInstance();
        // 初始化Fresco
        Fresco.initialize(this);
        // 在登录成功后初始化通用链接
        sp = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        mNoHeaderEngine = mApp.getNoHeaderEngine();
        handler = new Handler();

        initView(savedInstanceState);
        // 布局加载后, 初始化网络状态组件
        networkState = getViewById(R.id.network_error);
        setListener();
        processLogic(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        runnable = new Runnable() {
            @Override
            public void run() {
                // 启动定时任务
                beginTimerTask();
                handler.postDelayed(runnable, 30000);
            }
        };
    }

    /**
     * 信鸽推送: 效果统计
     * 如果被打开的activity启动模式为SingleTop，SingleTask或SingleInstance，请根据以下在该activity重载onNewIntent方法
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句
    }


    /**
     * 初始化布局以及View控件
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 给View控件添加事件监听器
     */
    protected abstract void setListener();

    /**
     * 处理业务逻辑，状态恢复等操作
     */
    protected abstract void processLogic(Bundle savedInstanceState);


    /**
     * 启动定时任务,刷新网络数据
     */
    protected abstract void beginTimerTask();

    /**
     * 需要处理点击事件时，重写该方法
     */
    public void onClick(View v) {
    }

    /**
     * 全局查找View
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }


    /**
     * 设置网络状态
     */
    protected void setNetworkState(Boolean state) {
        if (state) {
            networkState.setVisibility(View.GONE);
        } else {
            networkState.setVisibility(View.VISIBLE);
        }
    }

    /**
     *
     */

    /**
     * 显示Toast
     */
    protected void showToast(String text) {
        ToastUtil.show(text);
    }

    /**
     * 设置通用链接,登录时调用一次,初始化全局静态变量mEngine
     * @param sp
     */
    public void setEngine(SharedPreferences sp) {
        final String token;
        final String phone;
        // 获取token和phone
        token = sp.getString("token", null);
        phone = sp.getString("phone", null);

        // 定义拦截器,添加headers
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("X-User-Token", token)
                        .addHeader("X-User-Phone", phone)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        // 创建Retrofit实例
        mEngine = new Retrofit.Builder()
                .baseUrl("http://139.196.190.201/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build().create(Engine.class);
    }

    /**
     * alert: 载入
     */
    public void showLoadingDialog(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            mLoadingDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.setTitleText(msg);
        }
        mLoadingDialog.show();
    }

    /**
     * 隐藏alert
     */
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 30000);
        Log.i("onResume:", "begin runnable");
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        Log.i("onPause:", "remove runnable");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
