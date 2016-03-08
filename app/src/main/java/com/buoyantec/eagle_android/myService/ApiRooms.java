package com.buoyantec.eagle_android.myService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.buoyantec.eagle_android.ui.activity.MainActivity;
import com.buoyantec.eagle_android.ui.activity.R;
import com.buoyantec.eagle_android.model.Room;
import com.buoyantec.eagle_android.model.Rooms;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kang on 16/1/27.
 * 获取所有机房
 * 参数: token, phone
 * 用户身份验证成功后调用
 */
public class ApiRooms {
    private Context context;
    private Activity loginActivity;
    private SharedPreferences sp;

    //初始化
    public ApiRooms(Context context, Activity loginActivity){
        this.context = context;
        this.loginActivity = loginActivity;
        sp = context.getSharedPreferences("foobar", Activity.MODE_PRIVATE);
    }

    /**
     * 如果用户身份合法,获取用户机房列表
     */
    public void getUserRooms() {
        ApiRequest apiRequest = new ApiRequest(context);
        Call<Rooms> call = apiRequest.getService().getRooms();
        // 发送请求(使用同步加载)
        call.enqueue(new Callback<Rooms>() {
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

                    SharedPreferences.Editor editor = sp.edit();

                    if (result.equals("")) {
                        // 登陆页,显示错误信息
                        Intent i = new Intent(loginActivity, loginActivity.getClass());
                        i.putExtra("error", "没有可管理的机房,请联系管理员");
                        loginActivity.startActivity(i);
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
                        Intent i = new Intent(loginActivity, MainActivity.class);
                        loginActivity.startActivity(i);
                        loginActivity.finish();
                    }

                    Log.i("机房列表", context.getString(R.string.getSuccess) + code);
                } else {
                    Toast.makeText(context, context.getString(R.string.getDataFailed), Toast.LENGTH_SHORT).show();
                    Log.i("机房列表", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(context, context.getString(R.string.netWorkFailed), Toast.LENGTH_SHORT).show();
                Log.i("机房列表", context.getString(R.string.linkFailed));
            }
        });
    }
}
