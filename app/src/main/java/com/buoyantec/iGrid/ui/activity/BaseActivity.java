package com.buoyantec.iGrid.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.buoyantec.iGrid.App;
import com.buoyantec.iGrid.engine.Engine;
import com.buoyantec.iGrid.util.ToastUtil;

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
    protected Engine mEngine;
    protected Engine mLoginEngine;
    private SweetAlertDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        mApp = App.getInstance();
        // 在登录成功后初始化通用链接
        setEngine();
        mLoginEngine = mApp.getLoginEngine();
        initView(savedInstanceState);
        setListener();
        processLogic(savedInstanceState);
    }

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
     * 显示alert
     */
    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            mLoadingDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.setTitleText("数据加载中...");
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

    // 设置通用链接
    private void setEngine() {
        final String token;
        final String phone;
        // 获取token和phone
        SharedPreferences sp = getApplicationContext()
                .getSharedPreferences("foobar", Context.MODE_PRIVATE);
        token = sp.getString("token", null);
        phone = sp.getString("phone", null);

        System.out.println(token+"0090909090909");
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
}
