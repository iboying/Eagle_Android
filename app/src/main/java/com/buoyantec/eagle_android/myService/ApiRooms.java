package com.buoyantec.eagle_android.myService;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.buoyantec.eagle_android.R;
import com.buoyantec.eagle_android.model.Room;
import com.buoyantec.eagle_android.model.Rooms;

import java.io.IOException;
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
    private SharedPreferences sp;

    //初始化
    public ApiRooms(Context context){
        this.context = context;
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

                    Integer current_room_id = sp.getInt("current_room_id", 0);
                    SharedPreferences.Editor editor = sp.edit();

                    if (current_room_id == 0) {
                        if (result.equals("")) {
                            editor.putString("rooms", null);
                        } else {
                            String[] rooms = result.split("#");
                            Integer room_id = Integer.parseInt(rooms[0]);
                            String room = rooms[1];

                            // 保存当前机房信息
                            editor.putString("rooms", result);
                            editor.putString("current_room", room);
                            editor.putInt("current_room_id", room_id);
                        }
                    }
                    editor.apply();

                    Log.i("机房列表", context.getString(R.string.getSuccess) + code);
                } else {
                    Log.i("机房列表", context.getString(R.string.getFailed) + code);
                    try {
                        String error = response.errorBody().string();
                        System.out.println(error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // TODO: 16/2/25 错误处理
                Log.i("机房列表", context.getString(R.string.linkFailed));
            }
        });
    }
}
