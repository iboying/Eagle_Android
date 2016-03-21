package com.buoyantec.eagle_android.ui.activity;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.buoyantec.eagle_android.ui.activity.R;

public class forgotPasswordActivity extends BaseActivity {
    private Button modifyButton;
    private Button getCaptcha;
    private AutoCompleteTextView mPhone;
    private EditText mCaptcha;
    private EditText mNewPassword;
    private EditText mConfirmPassword;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forgot_password);
        modifyButton = getViewById(R.id.update_password_button);
        getCaptcha = getViewById(R.id.get_captcha_button);
        mPhone = getViewById(R.id.phone);
        mCaptcha = getViewById(R.id.captcha);
        mNewPassword = getViewById(R.id.newPassword);
        mConfirmPassword = getViewById(R.id.confirmPassword);
    }

    @Override
    protected void setListener() {
        modifyButton.setOnClickListener(this);
        getCaptcha.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_captcha_button:
                captchaElement();
                break;
            case R.id.update_password_button:
                updatePassword();
                break;
        }
        super.onClick(v);
    }

    // 确认修改密码按钮点击事件
    private void updatePassword() {
        // 初始化错误信息
        mPhone.setError(null);
        mCaptcha.setError(null);
        mNewPassword.setError(null);
        mConfirmPassword.setError(null);
        // 保存表单数据
        String phone = mPhone.getText().toString();
        String password = mNewPassword.getText().toString();
        String captcha = mCaptcha.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();
        // 登录状态标识: true:无效登录, false: 有效登录
        boolean cancel = false;
        // 焦点控件
        View focusView = null;

        // 验证: 确认密码
        if (TextUtils.isEmpty(confirmPassword)) {
            mConfirmPassword.setError(getString(R.string.error_field_required));
            focusView = mConfirmPassword;
            cancel = true;
        } else if (confirmPassword.equals(password)) {
            mConfirmPassword.setError("两次输入密码不同");
            focusView = mConfirmPassword;
            cancel = true;
        }
        // 验证: 密码是否为空/密码是否符合自定义规则
        if (TextUtils.isEmpty(password)) {
            mNewPassword.setError(getString(R.string.error_field_required));
            focusView = mNewPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mNewPassword.setError(getString(R.string.error_invalid_password));
            focusView = mNewPassword;
            cancel = true;
        }
        // 验证: 验证码
        if (TextUtils.isEmpty(captcha)) {
            mCaptcha.setError(getString(R.string.error_field_required));
            focusView = mCaptcha;
            cancel = true;
        } else if (!isCaptchaValid(captcha)) {
            mCaptcha.setError("验证码不正确");
            focusView = mCaptcha;
            cancel = true;
        }
        // 验证: 手机号是否为空/手机号是否符合自定义规则
        if (TextUtils.isEmpty(phone)) {
            mPhone.setError(getString(R.string.error_field_required));
            focusView = mPhone;
            cancel = true;
        }else if (!isPhoneValid(phone)) {
            mPhone.setError(getString(R.string.error_invalid_phone));
            focusView = mPhone;
            cancel = true;
        }

        if (cancel) {
            // 无效的数据, 返回焦点控件
            focusView.requestFocus();
        } else {
            // 有效的数据, 开始登录
            showToast("正在修改");

        }
    }

    // 获取验证码事件
    private void captchaElement() {

    }

    /**
     * 验证手机号格式
     */
    private boolean isPhoneValid(String phone) {
        return phone.length() == 11;
    }

    /**
     * 验证密码长度
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    /**
     * 验证码验证
     */
    private boolean isCaptchaValid(String captcha) {
        return captcha.equals("1234");
    }

}
