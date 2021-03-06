package com.buoyantec.eagle_android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.buoyantec.eagle_android.adapter.ToolbarMenuAdapter;
import com.buoyantec.eagle_android.model.Room;
import com.buoyantec.eagle_android.model.Rooms;
import com.buoyantec.eagle_android.model.User;
import com.buoyantec.eagle_android.ui.base.BaseActivity;
import com.buoyantec.eagle_android.ui.base.BaseTimerActivity;
import com.buoyantec.eagle_android.ui.customView.BadgeView;
import com.buoyantec.eagle_android.adapter.MainGridAdapter;
import com.buoyantec.eagle_android.model.SubSystemAlarm;
import com.buoyantec.eagle_android.model.RoomAlarm;
import com.facebook.drawee.view.SimpleDraweeView;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orhanobut.logger.Logger;
import com.pgyersdk.update.PgyUpdateManager;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseTimerActivity implements NavigationView.OnNavigationItemSelectedListener {
    private List<String> roomNames;
    private List<Integer> roomIds;
    private List<String> roomPics;
    private HashMap<String, Integer> systemAlarmCount;
    private static Boolean isExit = false;
    private Context context;

    private Integer current_room_id;
    private String current_room;
    private String deviceToken;
    // 组件
    private Toolbar toolbar;
    private TextView subToolbarTitle;
    private CircleProgressBar circleProgressBar;
    private SimpleDraweeView myImage;
    private DrawerLayout drawer;
    private Button signOutButton;
    private NavigationView navigationView;
    private GridView gridView;
    private BadgeView badge;
    private ImageView warnMessage;

    @Override
    protected void initView(Bundle savedInstanceState) {
        /**
         * 用户信息是否存在
         * 是: 加载主页面
         * 否: 加载登陆页
         */
        String token = sp.getString("token", "");
        current_room_id = sp.getInt("current_room_id", 0);
        String rooms = sp.getString("rooms", null);
        current_room = sp.getString("current_room", null);
        context = getApplicationContext();

        if (token.isEmpty() || current_room_id==0 || rooms==null || current_room==null) {
            finish();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        } else {
            setContentView(R.layout.activity_main);
            // 加载字体图标
            Iconify.with(new FontAwesomeModule());
            // 初始化变量
            circleProgressBar = getViewById(R.id.progressBar);
            myImage = getViewById(R.id.room_image);
            toolbar = getViewById(R.id.toolbar);
            subToolbarTitle = getViewById(R.id.toolbar_title);
            drawer = getViewById(R.id.drawer_layout);
            signOutButton = getViewById(R.id.sign_out_button);
            navigationView = getViewById(R.id.nav_view);
            gridView = getViewById(R.id.grid_view);
            badge = null;
            warnMessage = null;

            roomIds = new ArrayList<>();
            roomNames = new ArrayList<>();
            roomPics = new ArrayList<>();

            mainTask();
        }
    }

    /**
     * 登录成功后执行
     */
    private void mainTask() {
        // 初始化toolbar
        initToolBar();
        // 初始化侧边栏
        initDrawer();
        // 首页机房图片
        roomImage();
        // 初始化GridView
        initGridView();
        // 检测更新版本
        PgyUpdateManager.register(this);
        // 设备注册信鸽推送,上传设备token
        // 用户如果设置为接收推送,注册信鸽推送
        SharedPreferences.Editor editor = sp.edit();
        if (sp.getString("push", "").equals("")) {
            editor.putString("push", "");
            registerXgPush();
        }
        // 获取告警数
        getSubSystemAlarmCount();
    }

    @Override
    protected void setListener() {}

    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }

    @Override
    protected void beginTimerTask() {
        getSubSystemAlarmCount();
    }

    // 为后退键绑定关闭侧边菜单功能
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = getViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    // 物理菜单键绑定功能
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            DrawerLayout drawer = getViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            //调用双击退出函数
            exitByDoubleClick();
        }
        return super.onKeyDown(keyCode, event);
    }

    // 侧边菜单响应函数
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user_number) {
            Intent i = new Intent(this, MyActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_message) {
            Intent i = new Intent(this, InfoActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_setting) {
            Intent i = new Intent(this, SettingActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_about) {
            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = getViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // 选择机房下拉菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_room) {
            getUserRooms();
        }
        return super.onOptionsItemSelected(item);
    }

    // 初始化toolbar
    public void initToolBar(){
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        subToolbarTitle.setText(current_room);

        // 获取机房图片
        String sp_paths = sp.getString("pic_paths", null);
        if (sp_paths != null) {
            String[] paths = sp_paths.split("##");
            Collections.addAll(roomPics, paths);
        }
    }

    // 添加侧边菜单,并绑定ToolBar菜单按钮
    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // 显示姓名
        View headLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView name = (TextView) headLayout.findViewById(R.id.user_name);
        name.setText(sp.getString("name", null));

        // 退出按钮
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutConfirm();
            }
        });
    }

    // 退出确认
    private void signOutConfirm() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("确定退出吗?")
                .setContentText("退出登录后您将无法收到告警推送!")
                .setCancelText("取消")
                .setConfirmText("退出")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        // 退出时删除用户信息
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("token", "");
                        editor.apply();
                        finish();
                        // 退回登录页
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                })
                .show();
    }

    // 显示机房图片
    private void roomImage() {
        String current_room_pic = sp.getString("current_room_pic", null);
        if (current_room_pic != null) {
            Uri uri = Uri.parse(current_room_pic);
            myImage.setImageURI(uri);
        }
    }

    // 初始化栅格布局
    private void initGridView(){
        Integer[] images = {
                R.drawable.icon_system_status, R.drawable.icon_info,
                R.drawable.icon_work_order, R.drawable.icon_power_manager,
                R.drawable.icon_phone, R.drawable.icon_other
        };

        final String[] texts = { "系统状态", "告警信息", "工作安排", "能效管理", "IT管理", "其他" };

        gridView.setAdapter(new MainGridAdapter(gridView, this, images, texts));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) {
                    Intent i = new Intent(MainActivity.this, SystemStatus.class);
                    i.putExtra("title", texts[position]);
                    startActivity(i);
                } else if (position == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("systemAlarmCount", systemAlarmCount);
                    Intent i = new Intent(MainActivity.this, WarnSystems.class);
                    i.putExtra("room_id", current_room_id);
                    i.putExtra("title", texts[position]);
                    i.putExtras(bundle);
                    startActivity(i);
                } else if (position == 2) {
                    Intent i = new Intent(MainActivity.this, WorkPlan.class);
                    i.putExtra("title", texts[position]);
                    startActivity(i);
                    showToast("暂未开通");
                } else if (position == 3) {
                    Intent i = new Intent(MainActivity.this, PowerManage.class);
                    i.putExtra("title", texts[position]);
                    startActivity(i);
                } else if (position == 4) {
                    Intent i = new Intent(MainActivity.this, ItManage.class);
                    i.putExtra("title", texts[position]);
                    startActivity(i);
                    showToast("暂未开通");
                } else if (position == 5) {
                    Intent i = new Intent(MainActivity.this, Other.class);
                    i.putExtra("title", texts[position]);
                    startActivity(i);
                    showToast("暂未开通");
                }
            }
        });
    }

    // 两次点击返回键退出程序
    private void exitByDoubleClick() {
        Timer tExit;
        if (!isExit) {
            isExit = true; // 准备退出
            showToast("再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            System.exit(0);
        }
    }

    // 异步任务,获取机房总告警数
    public void getSubSystemAlarmCount() {
        circleProgressBar.setVisibility(View.VISIBLE);
        setEngine(sp);
        // 请求服务
        mEngine.getSubSystemAlarmCount(current_room_id).enqueue(new Callback<RoomAlarm>() {
            @Override
            public void onResponse(Call<RoomAlarm> call, Response<RoomAlarm> response) {
                setNetworkState(true);
                int code = response.code();
                if (code == 200) {
                    // 计数
                    Integer count = 0;
                    systemAlarmCount = new HashMap<>();

                    List<SubSystemAlarm> subSystemAlarms = response.body().getSubSystemAlarms();
                    for (SubSystemAlarm subSystemAlarm : subSystemAlarms) {
                        count += subSystemAlarm.getSubSystemCount();
                        systemAlarmCount.put(subSystemAlarm.getSubSystemName(), subSystemAlarm.getSubSystemCount());
                    }
                    // 获取告警信息按钮
                    warnMessage = getViewById(R.id.grid_warn_message_image);
                    if (warnMessage != null) {
                        if (badge == null) {
                            badge = new BadgeView(MainActivity.this, warnMessage);
                            badge.setBadgeMargin(0, 5);
                        } else {
                            if (count == 0) {
                                badge.hide();
                            } else {
                                if (count >= 100) {
                                    badge.setText("99+");
                                } else {
                                    badge.setText(count.toString());
                                }
                                badge.show();
                            }
                        }
                    }

                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.INVISIBLE);
                    Log.i("获取子系统告警数", context.getString(R.string.getSuccess) + code);
                } else {
                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(context, context.getString(R.string.getDataFailed), Toast.LENGTH_SHORT).show();
                    Log.i("获取子系统告警数", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Call<RoomAlarm> call, Throwable t) {
                circleProgressBar.setVisibility(View.INVISIBLE);
                setNetworkState(false);
                Log.i("获取子系统告警数", context.getString(R.string.linkFailed));
            }
        });
    }

    // 显示下拉菜单
    private void displayPopupWindow(View anchorView) {
        // 获取所有机房([id1, room1, id2, room2, ...])
        roomIds.clear();
        roomNames.clear();
        String sp_rooms = sp.getString("rooms", null);
        if (sp_rooms != null) {
            // [1,name,2,name2,...]
            String[] rooms = sp_rooms.split("#");
            // 存储机房id和机房名称
            for (int i = 0; i < rooms.length; i++) {
                if (i%2==0) {
                    roomIds.add(Integer.parseInt(rooms[i]));
                } else {
                    roomNames.add(rooms[i]);
                }
            }
        } else {
            subToolbarTitle.setText("无可管理机房");
        }

        // 实例化popWindow,并获取菜单
        final PopupWindow popup = new PopupWindow(context);
        View layout = getLayoutInflater().inflate(R.layout.toolbar_menu, null);
        // 加载机房列表
        ListView listView = (ListView) layout.findViewById(R.id.toolbar_room_list);
        listView.setAdapter(new ToolbarMenuAdapter(context, roomNames));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("current_room_id", roomIds.get(position));
                editor.putString("current_room", roomNames.get(position));
                editor.putString("current_room_pic", roomPics.get(position));
                editor.apply();
                // 切记,切换activity时,清除popupWindow
                popup.dismiss();
                // 重新加载UI组件
                current_room_id = sp.getInt("current_room_id", 0);
                // 更新机房信息,机房图片
                subToolbarTitle.setText(roomNames.get(position));
                roomImage();
                // 异步任务: 检测告警数量
                getSubSystemAlarmCount();
            }
        });
        // 把菜单模块加入popWindow中
        popup.setContentView(layout);
        // 设置高度和宽度
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(350);
        // 失去焦点时,关闭popWindow
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // 给控件加上popWindow, setBackgroundDrawable可以取消popupWindow的边框
        popup.setBackgroundDrawable(new BitmapDrawable());
        // 设置相对于父级控件的位置
        popup.showAsDropDown(anchorView, -250, 0);
    }

    // 获取机房: Y: 切换机房, N: 回到登陆页,显示错误信息
    private void getUserRooms() {
        setEngine(sp);
        mEngine.getRooms().enqueue(new Callback<Rooms>() {
            @Override
            public void onResponse(Call<Rooms> call, Response<Rooms> response) {
                setNetworkState(true);
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
                        // 退出时删除用户信息
                        editor.putString("token", "");
                        editor.apply();
                        finish();
                        // 退回登录页
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                    } else {
                        // 保存当前机房信息
                        editor.putString("rooms", result);
                        editor.putString("pic_paths", pic_path);
                        editor.apply();
                    }
                    View room = getViewById(R.id.toolbar_room);
                    displayPopupWindow(room);

                    Log.i("机房列表", context.getString(R.string.getSuccess) + code);
                } else {
                    showToast(context.getString(R.string.getDataFailed));
                    Log.i("机房列表", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Call<Rooms> call, Throwable t) {
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
        // XGPushConfig.enableDebug(this, true);
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
                Logger.i("信鸽推送注册(成功), token:" + o);
            }

            @Override
            public void onFail(Object o, int i, String s) {
                showToast("推送服务注册失败,尝试重新登陆");
                Logger.w("信鸽推送注册(失败), token:" + o + ", errCode:" + i + ",msg:" + s);
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
    private void uploadDeviceInfo() {
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

                    Logger.i("上传设备token成功" + ", 返回码:" + response.code());
                } else {
                    showToast("注册设备失败,尝试重新登录");
                    Logger.i("上传设备token失败" + ", 返回码:" + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                setNetworkState(false);
            }
        });
    }
}
