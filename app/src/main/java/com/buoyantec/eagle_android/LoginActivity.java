package com.buoyantec.eagle_android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.buoyantec.eagle_android.model.User;
import com.buoyantec.eagle_android.myService.ApiRequest;
import com.buoyantec.eagle_android.myService.ApiRooms;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kang on 16/1/25.
 * 描述: 登录控制器
 */
public class LoginActivity extends AppCompatActivity {
    // UI组件
    private AutoCompleteTextView mPhoneView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mLoginButton;
    // 数据
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 加载字体图标
        Iconify.with(new FontAwesomeModule());
        // 加载内容
        setContentView(R.layout.activity_login);
        // 获取组件,数据
        init();
        // 手机号自动补全
        phoneAutoComplete();
        // 密码编辑响应
        passwordEditListener();
        //表单获得焦点改变样式
        inputFocusedStyle();
        // 点击登录
        clickLoginButton();
    }

    /**
     * 初始化控件和变量
     */
    private void init() {
        mPhoneView = (AutoCompleteTextView) findViewById(R.id.phone);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginButton = (Button) findViewById(R.id.sign_in_button);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mPreferences = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
    }

    /**
     * 点击登录按钮
     */
    private void clickLoginButton() {
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                beginLogin();
            }
        });
    }

    /**
     * 开始登录
     */
    private void beginLogin() {
        // 初始化错误信息
        mPhoneView.setError(null);
        mPasswordView.setError(null);
        // 保存表单数据
        String phone = mPhoneView.getText().toString();
        String password = mPasswordView.getText().toString();
        // 登录状态标识: true:无效登录, false: 有效登录
        boolean cancel = false;
        // 焦点控件
        View focusView = null;

        // 验证: 密码是否为空/密码是否符合自定义规则
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // 验证: 手机号是否为空/手机号是否符合自定义规则
        if (TextUtils.isEmpty(phone) && !isPhoneValid(phone)) {
            mPhoneView.setError(getString(R.string.error_field_required));
            focusView = mPhoneView;
            cancel = true;
        }

        if (cancel) {
            // 无效的数据, 返回焦点控件
            focusView.requestFocus();
        } else {
            // 有效的数据, 开始登录
            showProgress(true);
            //调用接口,处理回调
            loginTask(phone, password);
        }
    }

    /**
     * 验证手机号格式
     */
    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.length() == 11;
    }

    /**
     * 验证密码长度
     */
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }

    /**
     * 密码表单编辑监听
     */
    private void passwordEditListener() {
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    beginLogin();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 手机号自动补全
     */
    private void phoneAutoComplete() {
        String phone = mPreferences.getString("phone", null);
        if (phone != null) {
            mPhoneView.setText(phone);
            mPasswordView.requestFocus();
        }
    }

    /**
     * 输入框的激活样式
     */
    private void inputFocusedStyle() {
        final TextView pIcon = (TextView) findViewById(R.id.login_phone_icon);
        final TextView pwdIcon = (TextView) findViewById(R.id.login_password_icon);
        //输入框获取焦点时,图标变蓝
        mPhoneView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    pIcon.setTextColor(ContextCompat
                            .getColor(LoginActivity.this, R.color.loginFocusedBorder));
                    mPhoneView.setTextColor(ContextCompat
                            .getColor(LoginActivity.this, R.color.loginFocusedBorder));
                } else {
                    pIcon.setTextColor(ContextCompat
                            .getColor(LoginActivity.this, R.color.loginNormalBorder));
                    mPhoneView.setTextColor(ContextCompat
                            .getColor(LoginActivity.this, R.color.loginNormalBorder));
                }
            }
        });
        //密码框获取焦点时,图标变蓝
        mPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    pwdIcon.setTextColor(ContextCompat
                            .getColor(LoginActivity.this, R.color.loginFocusedBorder));
                    mPasswordView.setTextColor(ContextCompat
                            .getColor(LoginActivity.this, R.color.loginFocusedBorder));
                } else {
                    pwdIcon.setTextColor(ContextCompat
                            .getColor(LoginActivity.this, R.color.loginNormalBorder));
                    mPasswordView.setTextColor(ContextCompat
                            .getColor(LoginActivity.this, R.color.loginNormalBorder));
                }
            }
        });
    }

    /**
     * 登录时显示进度条
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * 调用接口,处理数据
     */
    private void loginTask(final String phone, final String password) {
        // 获取指定链接数据
        ApiRequest apiRequest = new ApiRequest();
        Call<User> call = apiRequest.getLoginService().getUser(phone, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response) {
                int code = response.code();
                if (code == 201) {
                    // 获取用户
                    User user = response.body();
                    // 写入SharePreferences
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putInt("id", user.getId());
                    editor.putString("name", user.getName());
                    editor.putString("email", user.getEmail());
                    editor.putString("token", user.getAuthenticationToken());
                    editor.putString("phone", phone);
                    editor.putString("password", password);
                    editor.apply();
                    // 获取用户机房列表
                    ApiRooms apiRooms = new ApiRooms(getApplicationContext());
                    apiRooms.getUserRooms();
                    // 跳转到主页面
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                    Log.i("用户登录", getResources().getString(R.string.getSuccess) + code);
                } else {
                    showProgress(false);
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();

                    Log.i("用户登录", getResources().getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // TODO: 16/2/25 网络错误处理
                Log.i("用户登录", getResources().getString(R.string.linkFailed));
            }
        });
    }
}

