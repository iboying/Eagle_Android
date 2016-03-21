package com.buoyantec.eagle_android.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;

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
import android.widget.Toast;

import com.buoyantec.eagle_android.model.Room;
import com.buoyantec.eagle_android.model.Rooms;
import com.buoyantec.eagle_android.model.User;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kang on 16/1/25.
 * 描述: 登录
 */
public class LoginActivity extends BaseActivity {
    // UI组件
    private AutoCompleteTextView mPhoneView;
    private EditText mPasswordView;
    private CircleProgressBar mProgressView;
    private View mLoginFormView;
    private Button mLoginButton;
    private TextView forgotPassword;
    // 数据
    private SharedPreferences mPreferences;
    private Context context;

    @Override
    protected void initView(Bundle savedInstanceState) {
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_login);
        mPhoneView = getViewById(R.id.phone);
        mPasswordView = getViewById(R.id.password);
        mLoginButton = getViewById(R.id.sign_in_button);
        mLoginFormView = getViewById(R.id.login_form);
        mProgressView = getViewById(R.id.progressBar);
        forgotPassword = getViewById(R.id.forgotPasswordTextView);

        mPreferences = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        context = this;

        Intent i = getIntent();
        String error = i.getStringExtra("error");
        if (error != null) {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void setListener() {
        // 点击登录按钮
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                beginLogin();
            }
        });

        // 密码编辑响应
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

        // 忘记密码
        forgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, forgotPasswordActivity.class);
                startActivity(i);
            }
        });

        // 表单获得焦点改变样式
        final TextView pIcon = getViewById(R.id.login_phone_icon);
        final TextView pwdIcon = getViewById(R.id.login_password_icon);
        // 输入框获取焦点时,图标变蓝
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
        // 密码框获取焦点时,图标变蓝
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

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        phoneAutoComplete();
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
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // 验证: 手机号是否为空/手机号是否符合自定义规则
        if (TextUtils.isEmpty(phone)) {
            mPhoneView.setError(getString(R.string.error_field_required));
            focusView = mPhoneView;
            cancel = true;
        }else if (!isPhoneValid(phone)) {
            mPhoneView.setError(getString(R.string.error_invalid_phone));
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
        return phone.length() == 11;
    }

    /**
     * 验证密码长度
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    /**
     * 登录时显示进度条
     */
    private void showProgress(final boolean show) {
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
        mLoginEngine.getUser(phone, password).enqueue(new Callback<User>() {
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
                    editor.apply();
                    /**
                     * 初始化全局静态变量mEngine(登录时初始化第一次)
                     */
                    setEngine(mPreferences);
                    // 获取用户机房列表,并跳转页面
                    getUserRooms();

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
                showProgress(false);
                mPasswordView.requestFocus();
                showToast(context.getString(R.string.netWorkFailed));
                Log.i("用户登录", getResources().getString(R.string.linkFailed));
            }
        });
    }

    // 获取用户成功后,后去机房信息
    public void getUserRooms() {
        mEngine.getRooms().enqueue(new Callback<Rooms>() {
            @Override
            public void onResponse(Response<Rooms> response) {
                int code = response.code();
                if (response.body() != null && code == 200) {
                    String result = "";
                    // 获得机房List
                    List<Room> roomList = response.body().getRooms();
                    // 遍历机房
                    for (Room room : roomList) {
                        if (room.getName() != null) {
                            result += (room.getId() + "");
                            result += '#';
                            result += room.getName();
                            result += '#';
                        }
                    }

                    SharedPreferences.Editor editor = mPreferences.edit();

                    if (result.equals("")) {
                        // 登陆页,显示错误信息
                        Intent i = new Intent(context, LoginActivity.class);
                        i.putExtra("error", "没有可管理的机房,请联系管理员");
                        startActivity(i);
                    } else {
                        String[] rooms = result.split("#");
                        Integer room_id = Integer.parseInt(rooms[0]);
                        String room = rooms[1];
                        // 保存当前机房信息
                        editor.putString("rooms", result);
                        editor.putString("current_room", room);
                        editor.putInt("current_room_id", room_id);
                        editor.apply();
                        // 进入主页
                        Intent i = new Intent(context, MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                    Log.i("机房列表", context.getString(R.string.getSuccess) + code);
                } else {
                    showToast(context.getString(R.string.getDataFailed));
                    Log.i("机房列表", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showToast(context.getString(R.string.netWorkFailed));
                Log.i("机房列表", context.getString(R.string.linkFailed));
            }
        });
    }
}
