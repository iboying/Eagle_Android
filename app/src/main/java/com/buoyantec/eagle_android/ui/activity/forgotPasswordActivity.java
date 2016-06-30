package com.buoyantec.eagle_android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.buoyantec.eagle_android.model.User;
import com.buoyantec.eagle_android.ui.base.BaseActivity;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class forgotPasswordActivity extends BaseActivity {
    private Button modifyButton;
    private Button getSms;
    private AutoCompleteTextView mPhone;
    private EditText mSms;
    private EditText mNewPassword;
    private EditText mConfirmPassword;

    private Context context;
    private CountDownTimer timer;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forgot_password);
        modifyButton = getViewById(R.id.update_password_button);
        getSms = getViewById(R.id.get_sms_button);
        mPhone = getViewById(R.id.phone);
        mSms = getViewById(R.id.sms);
        mNewPassword = getViewById(R.id.newPassword);
        mConfirmPassword = getViewById(R.id.confirmPassword);

        context = this;
    }

    @Override
    protected void setListener() {
        modifyButton.setOnClickListener(this);
        getSms.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        // 补全手机号
        phoneAutoComplete();

        // 获取验证码计时器
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getSms.setText(millisUntilFinished/1000 + "秒");
            }

            @Override
            public void onFinish() {
                getSms.setEnabled(true);
                getSms.setText("重新获取");
            }
        };
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_sms_button:
                getSms();
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
        mSms.setError(null);
        mNewPassword.setError(null);
        mConfirmPassword.setError(null);
        // 保存表单数据
        String phone = mPhone.getText().toString();
        String password = mNewPassword.getText().toString();
        String sms = mSms.getText().toString();
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
        } else if (!confirmPassword.equals(password)) {
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
        if (TextUtils.isEmpty(sms)) {
            mSms.setError(getString(R.string.error_field_required));
            focusView = mSms;
            cancel = true;
        } else if (!isCaptchaValid(sms)) {
            mSms.setError("验证码不正确");
            focusView = mSms;
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

        // 如果表单数据合法,执行操作
        if (cancel) {
            // 无效的数据, 返回焦点控件
            focusView.requestFocus();
        } else {
            // 有效的数据, 开始修改密码
            beginUpdatePassword(phone, password, sms);
        }
    }

    // 开始修改密码
    private void beginUpdatePassword(final String phone, String password, String sms) {
        mNoHeaderEngine.updatePassword(phone, password, sms).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                setNetworkState(true);
                // 加载框
                showLoadingDialog("正在修改...");
                if (response.code() == 200) {
                    User user = response.body();
                    String errors = user.getErrors();
                    if (errors == null) {
                        dismissLoadingDialog();
                        // 正确提示框
                        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("修改成功")
                            .setConfirmText("返回登录")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    // 写入SharePreferences
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("phone", phone);
                                    editor.apply();
                                    Intent i = new Intent(forgotPasswordActivity.this, LoginActivity.class);
                                    startActivity(i);
                                }
                            })
                            .show();
                    } else {
                        showToast(errors);
                    }
                } else {
                    showToast("修改失败:"+ response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                setNetworkState(false);
            }
        });
    }

    // 获取验证码事件
    private void getSms() {
        mPhone.setError(null);
        String phone = mPhone.getText().toString();
        // 登录状态标识: true:无效登录, false: 有效登录
        boolean cancel = false;
        // 焦点控件
        View focusView = null;

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

        // 如果表单数据合法,执行操作
        if (cancel) {
            // 无效的数据, 返回焦点控件
            focusView.requestFocus();
        } else {
            // 有效的手机号码, 开始获取验证码
            beginGetSms(phone);

        }
    }

    // 开始获取验证码
    private void beginGetSms(String phone) {
        mNoHeaderEngine.getSms(phone).enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if (response.code() == 200) {
                    HashMap<String, String> result = response.body();
                    getSms.setEnabled(false);
                    timer.start();
                    showToast(result.get("result"));
                } else {
                    showToast("获取失败");
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                setNetworkState(false);
            }
        });
    }

    // 后去登陆页的手机号,补全
    private void phoneAutoComplete() {
        String phone = getIntent().getStringExtra("phone");
        if (phone != null) {
            mPhone.setText(phone);
            mSms.requestFocus();
        }
    }

    /**
     * 验证手机号格式
     */
    private boolean isPhoneValid(String phone) {
        boolean flag;
        try{
            Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
            Matcher matcher = pattern.matcher(phone);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
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
        return captcha.length() >= 4;
    }

}
