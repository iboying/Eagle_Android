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

import com.buoyantec.eagle_android.API.MyService;
import com.buoyantec.eagle_android.model.AllRoom;
import com.buoyantec.eagle_android.model.User;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

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
        mPhoneView = (AutoCompleteTextView) findViewById(R.id.phone);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginButton = (Button) findViewById(R.id.sign_in_button);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mPreferences = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
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
     * 账号表单自动补全功能
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
    private void loginTask(String phone, String password) {
        // 创建Retrofit实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://139.196.190.201")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 创建所有链接
        MyService myService = retrofit.create(MyService.class);

        // 获取指定链接数据
        Call<User> call = myService.getUser(phone, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response) {
                int statusCode = response.code();
                if (statusCode == 201) {
                    // 获取用户
                    User user = response.body();
                    // 获取用户列表
                    getUserRooms(user.getPhone(), user.getAuthenticationToken());
                    // 暂时使用SharedPreference进行应用信息持久化
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putInt("id", user.getId());
                    editor.putString("name", user.getName());
                    editor.putString("email", user.getEmail());
                    editor.putString("token", user.getAuthenticationToken());
                    editor.putString("phone", user.getPhone());
                    editor.putString("password", mPasswordView.getText().toString());
                    editor.apply();
                    // 加载主页面
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    Log.v("user_info", "Success api client.");
                } else {
                    showProgress(false);
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                    Log.v("user_info", "Failed api client.");
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * 如果用户身份合法,获取用户机房列表
     */
    private void getUserRooms(final String phone, final String token) {
        // 定义拦截器,添加headers
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
//            @Override
//            public okhttp3.Response intercept(Chain chain) throws IOException {
//                Request newRequest = chain.request().newBuilder()
//                        .addHeader("X-User-Token", token)
//                        .addHeader("X-User-Phone", phone)
//                        .build();
//                return chain.proceed(newRequest);
//            }
//        }).build();
//
//        // 创建Retrofit实例
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://139.196.190.201")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build();
//
//        // 创建所有链接
//        MyService myService = retrofit.create(MyService.class);
//        // 获取指定链接数据
//        Call<AllRoom> call = myService.getRooms();
//        call.enqueue(new Callback<AllRoom>() {
//            @Override
//            public void onResponse(Response<AllRoom> response) {
//                AllRoom room = response.body();
//                System.out.println("=========================");
//                System.out.println(room.toString());
//                System.out.println("=========================");
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                showProgress(false);
//                mPhoneView.setError("您没有可管理的机房");
//                mPhoneView.requestFocus();
//                Log.v("AllRoom", "Failed api client.");
//            }
//        });
    }
}

