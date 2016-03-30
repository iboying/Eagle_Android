package com.buoyantec.eagle_android;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.buoyantec.eagle_android.engine.Engine;
import com.buoyantec.eagle_android.ui.activity.R;
import com.buoyantec.eagle_android.ui.activity.ReceiverPush;
import com.tencent.android.tpush.XGNotifaction;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushNotifactionCallback;

import java.util.List;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by kang on 16/3/3.
 * 自定义application
 */
public class App extends Application {
    private static App sInstance;
    private Engine noHeaderEngine;
    private Context context;

    // 判断是否是主线程
    public boolean isMainProcess() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        setLoginEngine();

        /**
         * 处理 -> 推送通知
         * 透传消息命令没有拦截,(未研究)
         */
        context = getApplicationContext();
        // 在主进程设置信鸽相关的内容
        if (isMainProcess()) {
            // 为保证弹出通知前一定调用本方法，需要在application的onCreate注册
            // 收到通知时，会调用本回调函数。
            // 相当于这个回调会拦截在信鸽的弹出通知之前被截取
            // 一般上针对需要获取通知内容、标题，设置通知点击的跳转逻辑等等
            XGPushManager.setNotifactionCallback(new XGPushNotifactionCallback() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void handleNotify(XGNotifaction xGNotifaction) {
                    Log.i("App.java ->", "拦截处理信鸽通知：" + xGNotifaction);
                    // 获取标签、内容、自定义内容
                    String title = xGNotifaction.getTitle();
                    String content = xGNotifaction.getContent();
                    String customContent = xGNotifaction.getCustomContent();
                    // 如果还要弹出通知，可直接调用以下代码或自己创建Notifaction，否则，本通知将不会弹出在通知栏中。
                    // xGNotifaction.doNotify();

                    // TODO: 16/3/29 其他处理, 可自定义Notification显示推送通知的内容
                    int NOTIFICATION_ID = 1;
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Intent intent = new Intent(context, ReceiverPush.class);
                    intent.putExtra("custom_content", customContent);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                    Notification notification = new Notification.Builder(context)
                            // 设置打开该通知,通知自动消失
                            .setAutoCancel(true)
                            // 设置显示在状态栏的通知提示信息
                            .setTicker("有新消息")
                            // 设置通知的图标
                            .setSmallIcon(R.mipmap.ic_launcher)
                            // 设置通知内容的标题
                            .setContentTitle(title)
                            // 设置通知内容
                            .setContentText(content)
                            // 设置使用系统默认的声音,默认LED灯
                            . setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                            //设置通知的自定义声音
                            //.setSound(Uri.parse("..."))
                            .setWhen(System.currentTimeMillis())
                            // 设置通知将要启动程序的Intent
                            .setContentIntent(pendingIntent)
                            .build();
                    // 发送通知
                    notificationManager.notify(NOTIFICATION_ID, notification);
                }
            });
        }
    }

    /**
     * 应用
     */
    public static App getInstance() {
        return sInstance;
    }

    /**
     * 返回登录链接
     */
    public Engine getNoHeaderEngine() {
        return noHeaderEngine;
    }

    // 设置登录链接
    private void setLoginEngine() {
        noHeaderEngine = new Retrofit.Builder()
                .baseUrl("http://139.196.190.201/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Engine.class);
    }
}
