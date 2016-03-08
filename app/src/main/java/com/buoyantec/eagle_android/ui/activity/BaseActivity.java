package com.buoyantec.eagle_android.ui.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.buoyantec.eagle_android.App;
import com.buoyantec.eagle_android.engine.Engine;
import com.buoyantec.eagle_android.util.ToastUtil;

import cn.pedant.SweetAlert.SweetAlertDialog;

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
        mEngine = mApp.getEngine();
        mLoginEngine = mApp.getLoginEngine();
        initView(savedInstanceState);
        setListener();
        processLogic(savedInstanceState);
    }

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
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
     *
     * @param savedInstanceState
     */
    protected abstract void processLogic(Bundle savedInstanceState);

    /**
     * 需要处理点击事件时，重写该方法
     *
     * @param v
     */
    public void onClick(View v) {
    }

    protected void showToast(String text) {
        ToastUtil.show(text);
    }

    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            mLoadingDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.setTitleText("数据加载中...");
        }
        mLoadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}
