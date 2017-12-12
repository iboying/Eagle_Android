package com.buoyantec.eagle_android.receiver;

import android.content.Context;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kang on 16/3/28.
 * 自定义receiver,处理 -> 消息命令
 * 注: 现版本只使用了推送通知,没有使用消息命令
 */
public class MessageReceiver extends XGPushBaseReceiver {
    public static final String LogTag = "TPushReceiver";

    /**
     * 注册结果
     * @param context
     * @param errorCode
     * @param xgPushRegisterResult
     */
    @Override
    public void onRegisterResult(Context context, int errorCode, XGPushRegisterResult xgPushRegisterResult) {
        if (context == null || xgPushRegisterResult == null) {
            return;
        }
        String text;
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "MessageReceiver,注册成功: " + xgPushRegisterResult;
            // 在这里拿token
            String token = xgPushRegisterResult.getToken();
            Logger.i(text + "token: " + token);
        } else {
            text = "MessageReceiver,注册失败: " + "错误码:" + errorCode + "结果:" + xgPushRegisterResult;
            Logger.i(text);
        }
    }

    /**
     * 反注册结果
     * @param context
     * @param errorCode
     */
    @Override
    public void onUnregisterResult(Context context, int errorCode) {
        if (context == null) {
            return;
        }
        String text;
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "反注册成功";
        } else {
            text = "反注册失败" + errorCode;
        }
        Logger.i(text);
    }

    /**
     * 设置标签结果
     * @param context
     * @param errorCode
     * @param tagName
     */
    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text;
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"设置成功";
        } else {
            text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
        }
        Logger.i(text);
    }

    /**
     * 删除标签结果
     * @param context
     * @param errorCode
     * @param tagName
     */
    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text;
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"删除成功";
        } else {
            text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
        }
        Logger.i(text);
    }

    /**
     * 收到消息命令
     * @param context
     * @param xgPushTextMessage
     */
    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        // TODO Auto-generated method stub
        String text = "收到消息:" + xgPushTextMessage.toString();
        // 获取自定义key-value.
        String customContent = xgPushTextMessage.getCustomContent();
        if (customContent != null && customContent.length() != 0) {
            try {
                JSONObject obj = new JSONObject(customContent);
                // key1为前台配置的key
                if (!obj.isNull("key")) {
                    String value = obj.getString("key");
                    Log.d(LogTag, "get custom value:" + value);
                }
                // ...
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // APP自主处理消息的过程...
        Log.d(LogTag, "====================");
        Log.d(LogTag, text);
        Log.d(LogTag, "====================");
    }

    /**
     * 通知被打开触发的结果
     */
    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
//        if (context == null || xgPushClickedResult == null) {
//            return;
//        }
//        String text = "";
//        if (xgPushClickedResult.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
//            // 通知在通知栏被点击啦。。。。。
//            // APP自己处理点击的相关动作
//            // 这个动作可以在activity的onResume也能监听，请看第3点相关内容
//            text = "通知被打开 :" + xgPushClickedResult;
//        } else if (xgPushClickedResult.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
//            // 通知被清除啦。。。。
//            // APP自己处理通知被清除后的相关动作
//            text = "通知被清除 :" + xgPushClickedResult;
//        }
//        Toast.makeText(context, "广播接收到通知被点击:" + xgPushClickedResult.toString(),
//                Toast.LENGTH_SHORT).show();
//        // 获取自定义key-value
//        String customContent = xgPushClickedResult.getCustomContent();
//        if (customContent != null && customContent.length() != 0) {
//            try {
//                JSONObject obj = new JSONObject(customContent);
//                // key1为前台配置的key
//                if (!obj.isNull("key")) {
//                    String value = obj.getString("key");
//                    Log.d(LogTag, "get custom value:" + value);
//                }
//                // ...
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        // APP自主处理的过程。。。
//        Log.d(LogTag, text);
//        show(context, text);
    }

    /**
     * 通知被展示触发的结果，可以在此保存APP收到的通知到数据库中:
     * 需要在本地存储被展示过的通知内容,其中，XGPushShowedResult对象提供读取通知内容的接口
     */
    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
//        if (context == null || xgPushShowedResult == null) {
//            return;
//        }
//        XGNotification msg = new XGNotification();
//        msg.setMsg_id(xgPushShowedResult.getMsgId());
//        msg.setTitle(xgPushShowedResult.getTitle());
//        msg.setContent(xgPushShowedResult.getContent());
//        // msgationActionType==1为Activity，2为url，3为intent
//        msg.setNotificationActionType(xgPushShowedResult.getNotificationActionType());
//        // Activity,url,intent都可以通过getActivity()获得
//        msg.setActivity(xgPushShowedResult.getActivity());
//        msg.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
//        NotificationService.getInstance(context).save(msg);
//        context.sendBroadcast(intent);
//        show(context, "您有1条新消息, " + "通知被展示 ， " + xgPushShowedResult.toString());
    }
}
