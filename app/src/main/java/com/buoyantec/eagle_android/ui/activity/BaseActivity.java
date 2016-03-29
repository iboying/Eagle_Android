package com.buoyantec.eagle_android.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.buoyantec.eagle_android.App;
import com.buoyantec.eagle_android.engine.Engine;
import com.buoyantec.eagle_android.util.ToastUtil;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by kang on 16/3/3.
 * 全局操作
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    protected String TAG;
    protected App mApp;
    protected static Engine mEngine;
    protected Engine mNoHeaderEngine;
    private SweetAlertDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        mApp = App.getInstance();
        // 初始化Fresco
        Fresco.initialize(this);
        // 在登录成功后初始化通用链接

        mNoHeaderEngine = mApp.getNoHeaderEngine();
        initView(savedInstanceState);
        setListener();
        processLogic(savedInstanceState);
    }

    @Override
    protected void onStop() {
        stopElement();
        super.onStop();
    }

    /**
     * stop事件
     */
    protected void stopElement() {}

    /**
     * 全局查找View
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
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
     * 需要处理点击事件时，重写该方法
     */
    public void onClick(View v) {
    }

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
}
