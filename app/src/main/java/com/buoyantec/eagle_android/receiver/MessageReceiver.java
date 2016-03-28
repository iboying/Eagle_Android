package com.buoyantec.eagle_android.receiver;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

/**
 * Created by kang on 16/3/28.
 * 自定义receiver,处理消息
 */
public class MessageReceiver extends XGPushBaseReceiver {
    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {
        // TODO Auto-generated method stub
        if (context == null || xgPushRegisterResult == null) {
            return;
        }
        String text;
        if (i == XGPushBaseReceiver.SUCCESS) {
            text = xgPushRegisterResult + "注册成功";
            // 在这里拿token
            String token = xgPushRegisterResult.getToken();
            Log.d("onRegisterResult", token);
        } else {
            text = xgPushRegisterResult + "注册失败，错误码：" + i;
        }
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUnregisterResult(Context context, int i) {

    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {

    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {

    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        String msg = "收到消息" + xgPushTextMessage.toString();
        Log.i("推送", msg);
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

    }
}
