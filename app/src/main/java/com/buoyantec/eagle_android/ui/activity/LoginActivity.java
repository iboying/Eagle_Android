package com.buoyantec.eagle_android.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.buoyantec.eagle_android.model.Room;
import com.buoyantec.eagle_android.model.Rooms;
import com.buoyantec.eagle_android.model.User;
import com.buoyantec.eagle_android.ui.base.BaseActivity;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orhanobut.logger.Logger;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
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
    private Context context;
    private String deviceToken;

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

        context = this;
        deviceToken = null;

        Intent i = getIntent();
        String error = i.getStringExtra("error");
        if (error != null) {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void setListener() {
        // 点击登录按钮
        mLoginButton.setOnClickListener(this);
        // 忘记密码
        forgotPassword.setOnClickListener(this);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                beginLogin();
                break;
            case R.id.forgotPasswordTextView:
                Intent i = new Intent(context, forgotPasswordActivity.class);
                i.putExtra("phone", mPhoneView.getText().toString());
                startActivity(i);
                break;
        }
        super.onClick(v);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        phoneAutoComplete();
        // 反注册（不再接收消息）：unregisterPush(context)
        XGPushManager.unregisterPush(context);
    }

    /**
     * 手机号自动补全
     */
    private void phoneAutoComplete() {
        String phone = sp.getString("phone", null);
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
        boolean flag;
        try{
            Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
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

    //  验证用户: Y: 获取机房, N: 回到登录页
    private void loginTask(final String phone, final String password) {
        mNoHeaderEngine.getUser(phone, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int code = response.code();
                if (code == 201) {
                    // 获取用户
                    User user = response.body();
                    // 写入SharePreferences
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("id", user.getId());
                    editor.putString("name", user.getName());
                    editor.putString("email", user.getEmail());
                    editor.putString("token", user.getAuthenticationToken());
                    editor.putString("phone", phone);
                    editor.apply();
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
            public void onFailure(Call<User> call, Throwable t) {
                showProgress(false);
                mPasswordView.requestFocus();
                setNetworkState(false);
                Log.i("用户登录", getResources().getString(R.string.linkFailed));
            }
        });
    }

    // 获取机房: Y: 注册信鸽服务, N: 回到登陆页,显示错误信息
    public void getUserRooms() {
        setEngine(sp);
        mEngine.getRooms().enqueue(new Callback<Rooms>() {
            @Override
            public void onResponse(Call<Rooms> call, Response<Rooms> response) {
                int code = response.code();
                if (response.body() != null && code == 200) {
                    String result = "";
                    String pic_path = "";
                    // 获得机房List
                    List<Room> roomList = response.body().getRooms();
                    // 遍历机房
                    for (Room room : roomList) {
                        if (room.getName() != null) {
                            result += (room.getId() + "");
                            result += '#';
                            result += room.getName();
                            result += '#';
                            pic_path += room.getPic();
                            pic_path += "##";
                        }
                    }

                    SharedPreferences.Editor editor = sp.edit();

                    if (result.equals("")) {
                        // 登陆页,显示错误信息
                        Intent i = new Intent(context, LoginActivity.class);
                        i.putExtra("error", "没有可管理的机房,请联系管理员");
                        startActivity(i);
                    } else {
                        // 机房信息[id,name,id,name]
                        String[] rooms = result.split("#");
                        // 机房图片路径
                        String[] paths = pic_path.split("##");
                        // 取第一个机房为默认机房
                        Integer room_id = Integer.parseInt(rooms[0]);
                        String room = rooms[1];
                        // 取第一个路径为默认路径
                        String path = paths[0];
                        // 保存当前机房信息
                        editor.putString("rooms", result);
                        editor.putString("pic_paths", pic_path);
                        editor.putString("current_room", room);
                        editor.putInt("current_room_id", room_id);
                        editor.putString("current_room_pic", path);
                        editor.apply();
                        // 用户如果设置为接收推送,注册信鸽推送(已改为在MainActivity中判断)
//                        if (sp.getString("push", "").equals("")) {
//                            editor.putString("push", "");
//                            registerXgPush();
//                        }
                    }

                    Log.i("机房列表", context.getString(R.string.getSuccess) + code);
                } else {
                    showProgress(false);
                    showToast(context.getString(R.string.getDataFailed));
                    Log.i("机房列表", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Call<Rooms> call, Throwable t) {
                showProgress(false);
                setNetworkState(false);
                Log.i("机房列表", context.getString(R.string.linkFailed));
            }
        });
    }

    // 注册信鸽服务: Y: 上传device_token, N: 回到登录页,显示错误信息
    private void registerXgPush() {
        /**
         * 注册信鸽推送
         */
        // 开启logcat输出，方便debug，发布时请关闭
//        XGPushConfig.enableDebug(this, true);
        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
        // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext
        XGPushManager.registerPush(context, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                deviceToken = o.toString();
                // 上传用户token
                uploadDeviceInfo();
                Logger.i("信鸽推送注册(成功).token:" + o);
            }

            @Override
            public void onFail(Object o, int i, String s) {
                showProgress(false);
                mPasswordView.requestFocus();
                showToast("推送服务注册失败,重新登陆");
                Logger.w("信鸽推送注册(失败).token:" + o + ", errCode:" + i + ",msg:" + s);
            }
        });

        // 2.36（不包括）之前的版本需要调用以下2行代码(新版本,一定要注释掉)
        // Intent service = new Intent(context, XGPushService.class);
        // context.startService(service);

        // 其它常用的API：
        // 绑定账号（别名）注册：registerPush(context,account)或registerPush(context,account, XGIOperateCallback)，其中account为APP账号，可以为任意字符串（qq、openid或任意第三方），业务方一定要注意终端与后台保持一致。
        // 取消绑定账号（别名）：registerPush(context,"*")，即account="*"为取消绑定，解绑后，该针对该账号的推送将失效
        // 反注册（不再接收消息）：unregisterPush(context)
        // 设置标签：setTag(context, tagName)
        // 删除标签：deleteTag(context, tagName)
    }

    // 上传device_token: Y: 跳转主页面, N: 回到登录页,显示错误信息
    public void uploadDeviceInfo() {
        // 注册设备到服务器
        setEngine(sp);
        mEngine.upLoadDeviceToken("android", deviceToken).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                setNetworkState(true);
                if (response.code() == 200) {
                    User user = response.body();
                    // 保存数据
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("device_token", user.getDeviceToken());
                    editor.apply();

                    // 进入主页
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();

                    Log.i("上传推送token", context.getString(R.string.getSuccess) + response.code());
                } else {
                    showProgress(false);
                    mPasswordView.requestFocus();
                    showToast("上传推送信息失败");
                    Log.i("上传推送token", context.getString(R.string.getFailed) + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                setNetworkState(false);
                Log.i("上传推送token", context.getString(R.string.linkFailed));
            }
        });
    }
}
